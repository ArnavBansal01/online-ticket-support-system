package com.ticketsupport.ticketservice.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ticketsupport.ticketservice.exception.ForbiddenException;
import com.ticketsupport.ticketservice.exception.ResourceNotFoundException;
import com.ticketsupport.ticketservice.model.dto.request.AssignTicketRequest;
import com.ticketsupport.ticketservice.model.dto.request.CreateTicketRequest;
import com.ticketsupport.ticketservice.model.dto.request.UpdateTicketRequest;
import com.ticketsupport.ticketservice.model.dto.request.UpdateTicketStatusRequest;
import com.ticketsupport.ticketservice.model.dto.response.TicketAttachmentResponse;
import com.ticketsupport.ticketservice.model.dto.response.TicketDetailsResponse;
import com.ticketsupport.ticketservice.model.dto.response.TicketListResponse;
import com.ticketsupport.ticketservice.model.dto.response.TicketMessageResponse;
import com.ticketsupport.ticketservice.model.dto.response.TicketResponse;
import com.ticketsupport.ticketservice.model.entity.Ticket;
import com.ticketsupport.ticketservice.model.entity.TicketAttachment;
import com.ticketsupport.ticketservice.repository.TicketAttachmentRepository;
import com.ticketsupport.ticketservice.repository.TicketMessageRepository;
import com.ticketsupport.ticketservice.repository.TicketRepository;
import com.ticketsupport.ticketservice.service.TicketService;
import com.ticketsupport.ticketservice.service.UserValidationService;

@Service
public class TicketServiceImpl implements TicketService {
    private static final long DEFAULT_PUBLIC_CUSTOMER_ID = 3L;

    private final TicketRepository ticketRepository;
    private final TicketMessageRepository messageRepository;
    private final TicketAttachmentRepository attachmentRepository;
    private final UserValidationService userValidationService;

    public TicketServiceImpl(TicketRepository ticketRepository,
                             TicketMessageRepository messageRepository,
                             TicketAttachmentRepository attachmentRepository,
                             UserValidationService userValidationService) {
        this.ticketRepository = ticketRepository;
        this.messageRepository = messageRepository;
        this.attachmentRepository = attachmentRepository;
        this.userValidationService = userValidationService;
    }

    @Override
    public TicketResponse createTicket(CreateTicketRequest request, Long userId, String role) {
        String effectiveRole = role == null ? "CUSTOMER" : role;
        Long effectiveUserId = userId == null ? DEFAULT_PUBLIC_CUSTOMER_ID : userId;

        if (!(effectiveRole.equals("CUSTOMER") || effectiveRole.equals("ADMIN") || effectiveRole.equals("AGENT"))) {
            throw new ForbiddenException("Invalid role");
        }

        LocalDateTime now = LocalDateTime.now();

        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(Ticket.Status.OPEN);
        ticket.setPriority(request.getPriority());
        ticket.setCategory(request.getCategory());
        ticket.setCreatedBy(effectiveUserId);
        ticket.setCreatedAt(now);
        ticket.setUpdatedAt(now);
        ticket.setDeleted(false);

        Ticket saved = ticketRepository.save(ticket);

        if (request.getFileName() != null && !request.getFileName().isBlank()) {
            TicketAttachment attachment = new TicketAttachment();
            attachment.setTicketId(saved.getId());
            attachment.setFileName(request.getFileName());
            attachment.setFileUrl("/files/" + request.getFileName());
            attachment.setUploadedAt(now);
            attachmentRepository.save(attachment);
        }

        return TicketResponse.fromEntity(saved);
    }

    @Override
    public TicketListResponse listTickets(Map<String, String> filters, Long userId, String role) {
        int page = Integer.parseInt(filters.getOrDefault("page", "0"));
        int size = Integer.parseInt(filters.getOrDefault("size", "10"));

        List<Ticket> filtered = ticketRepository.findByDeletedFalse().stream()
                .filter(t -> role.equals("ADMIN") || role.equals("AGENT") || t.getCreatedBy().equals(userId))
                .filter(t -> filters.get("status") == null || t.getStatus().name().equals(filters.get("status")))
                .filter(t -> filters.get("priority") == null || t.getPriority().name().equals(filters.get("priority")))
                .filter(t -> filters.get("category") == null || t.getCategory().name().equals(filters.get("category")))
                .filter(t -> filters.get("assignedTo") == null || (t.getAssignedTo() != null && t.getAssignedTo().equals(Long.parseLong(filters.get("assignedTo")))))
                .filter(t -> {
                    String search = filters.get("search");
                    if (search == null || search.isBlank()) {
                        return true;
                    }
                    String normalized = search.toLowerCase();
                    return t.getTitle().toLowerCase().contains(normalized) || t.getDescription().toLowerCase().contains(normalized);
                })
                .filter(t -> {
                    String from = filters.get("dateFrom");
                    if (from == null || from.isBlank()) {
                        return true;
                    }
                    return !t.getCreatedAt().toLocalDate().isBefore(LocalDate.parse(from));
                })
                .filter(t -> {
                    String to = filters.get("dateTo");
                    if (to == null || to.isBlank()) {
                        return true;
                    }
                    return !t.getCreatedAt().toLocalDate().isAfter(LocalDate.parse(to));
                })
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();

        int fromIndex = Math.min(page * size, filtered.size());
        int toIndex = Math.min(fromIndex + size, filtered.size());
        List<TicketResponse> items = filtered.subList(fromIndex, toIndex).stream().map(TicketResponse::fromEntity).toList();
        int totalPages = size == 0 ? 1 : (int) Math.ceil((double) filtered.size() / size);

        return new TicketListResponse(items, page, size, filtered.size(), totalPages);
    }

    @Override
    public TicketDetailsResponse getTicket(Long id, Long userId, String role) {
        Ticket ticket = loadVisibleTicket(id, userId, role);
        boolean isAdminOrAgent = role.equals("ADMIN") || role.equals("AGENT");

        List<TicketMessageResponse> messages = messageRepository.findByTicketIdOrderByCreatedAtAsc(id).stream()
                .filter(m -> isAdminOrAgent || !m.isInternal())
                .map(m -> TicketMessageResponse.fromEntity(
                        m,
                        attachmentRepository.findByMessageId(m.getId()).stream().map(TicketAttachmentResponse::fromEntity).toList()
                ))
                .toList();

        List<TicketAttachmentResponse> attachments = attachmentRepository.findByTicketId(id)
                .stream()
                .map(TicketAttachmentResponse::fromEntity)
                .toList();

        return new TicketDetailsResponse(TicketResponse.fromEntity(ticket), messages, attachments);
    }

    @Override
    public TicketResponse updateTicket(Long id, UpdateTicketRequest request, Long userId, String role) {
        Ticket ticket = loadVisibleTicket(id, userId, role);
        boolean canEdit = role.equals("ADMIN") || ticket.getCreatedBy().equals(userId);
        if (!canEdit) {
            throw new ForbiddenException("Only owner or admin can update ticket");
        }

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setCategory(request.getCategory());
        ticket.setUpdatedAt(LocalDateTime.now());
        return TicketResponse.fromEntity(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponse updateStatus(Long id, UpdateTicketStatusRequest request, String role) {
        if (!(role.equals("ADMIN") || role.equals("AGENT"))) {
            throw new ForbiddenException("Only AGENT or ADMIN can update status");
        }

        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (ticket.isDeleted()) {
            throw new ResourceNotFoundException("Ticket not found");
        }

        ticket.setStatus(request.getStatus());
        ticket.setUpdatedAt(LocalDateTime.now());
        if (request.getStatus() == Ticket.Status.RESOLVED) {
            ticket.setResolvedAt(LocalDateTime.now());
        }
        return TicketResponse.fromEntity(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponse assign(Long id, AssignTicketRequest request, String role, String authHeader) {
        if (!role.equals("ADMIN")) {
            throw new ForbiddenException("Only ADMIN can assign tickets");
        }

        userValidationService.validateAgent(request.getAgentId(), authHeader);

        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (ticket.isDeleted()) {
            throw new ResourceNotFoundException("Ticket not found");
        }

        ticket.setAssignedTo(request.getAgentId());
        ticket.setUpdatedAt(LocalDateTime.now());
        return TicketResponse.fromEntity(ticketRepository.save(ticket));
    }

    @Override
    public void softDelete(Long id, String role) {
        if (!role.equals("ADMIN")) {
            throw new ForbiddenException("Only ADMIN can delete ticket");
        }
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        ticket.setDeleted(true);
        ticket.setUpdatedAt(LocalDateTime.now());
        ticketRepository.save(ticket);
    }

    @Override
    public TicketResponse closeTicket(Long id, Long userId, String role) {
        if (!role.equals("CUSTOMER")) {
            throw new ForbiddenException("Only CUSTOMER can close resolved tickets");
        }

        Ticket ticket = loadVisibleTicket(id, userId, role);
        if (ticket.getStatus() != Ticket.Status.RESOLVED) {
            throw new ForbiddenException("Only resolved tickets can be closed by customer");
        }

        ticket.setStatus(Ticket.Status.CLOSED);
        ticket.setClosedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        return TicketResponse.fromEntity(ticketRepository.save(ticket));
    }

    private Ticket loadVisibleTicket(Long id, Long userId, String role) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (ticket.isDeleted()) {
            throw new ResourceNotFoundException("Ticket not found");
        }

        boolean canView = role.equals("ADMIN") || role.equals("AGENT") || ticket.getCreatedBy().equals(userId);
        if (!canView) {
            throw new ForbiddenException("Not allowed to access ticket");
        }
        return ticket;
    }
}
