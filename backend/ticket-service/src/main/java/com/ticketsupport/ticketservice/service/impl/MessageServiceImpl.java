package com.ticketsupport.ticketservice.service.impl;

import com.ticketsupport.ticketservice.exception.ForbiddenException;
import com.ticketsupport.ticketservice.exception.ResourceNotFoundException;
import com.ticketsupport.ticketservice.model.dto.request.AddMessageRequest;
import com.ticketsupport.ticketservice.model.dto.response.TicketAttachmentResponse;
import com.ticketsupport.ticketservice.model.dto.response.TicketMessageResponse;
import com.ticketsupport.ticketservice.model.entity.Ticket;
import com.ticketsupport.ticketservice.model.entity.TicketAttachment;
import com.ticketsupport.ticketservice.model.entity.TicketMessage;
import com.ticketsupport.ticketservice.repository.TicketAttachmentRepository;
import com.ticketsupport.ticketservice.repository.TicketMessageRepository;
import com.ticketsupport.ticketservice.repository.TicketRepository;
import com.ticketsupport.ticketservice.service.MessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private final TicketRepository ticketRepository;
    private final TicketMessageRepository messageRepository;
    private final TicketAttachmentRepository attachmentRepository;

    public MessageServiceImpl(TicketRepository ticketRepository,
                              TicketMessageRepository messageRepository,
                              TicketAttachmentRepository attachmentRepository) {
        this.ticketRepository = ticketRepository;
        this.messageRepository = messageRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public TicketMessageResponse addMessage(Long ticketId, AddMessageRequest request, Long senderId, String role) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (ticket.isDeleted()) {
            throw new ResourceNotFoundException("Ticket not found");
        }

        boolean isAdminOrAgent = role.equals("ADMIN") || role.equals("AGENT");
        if (!isAdminOrAgent && !ticket.getCreatedBy().equals(senderId)) {
            throw new ForbiddenException("Not allowed to reply to this ticket");
        }
        if (request.isInternal() && !isAdminOrAgent) {
            throw new ForbiddenException("Only AGENT or ADMIN can post internal notes");
        }

        TicketMessage message = new TicketMessage();
        message.setTicketId(ticketId);
        message.setSenderId(senderId);
        message.setSenderRole(role);
        message.setContent(request.getContent());
        message.setCreatedAt(LocalDateTime.now());
        message.setInternal(request.isInternal());
        TicketMessage saved = messageRepository.save(message);

        List<TicketAttachmentResponse> attachments = List.of();
        if (request.getFileName() != null && !request.getFileName().isBlank()) {
            TicketAttachment attachment = new TicketAttachment();
            attachment.setTicketId(ticketId);
            attachment.setMessageId(saved.getId());
            attachment.setFileName(request.getFileName());
            attachment.setFileUrl("/files/" + request.getFileName());
            attachment.setUploadedAt(LocalDateTime.now());
            attachmentRepository.save(attachment);
            attachments = List.of(TicketAttachmentResponse.fromEntity(attachment));
        }

        return TicketMessageResponse.fromEntity(saved, attachments);
    }

    @Override
    public List<TicketMessageResponse> getMessages(Long ticketId, Long userId, String role) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (ticket.isDeleted()) {
            throw new ResourceNotFoundException("Ticket not found");
        }

        boolean isAdminOrAgent = role.equals("ADMIN") || role.equals("AGENT");
        if (!isAdminOrAgent && !ticket.getCreatedBy().equals(userId)) {
            throw new ForbiddenException("Not allowed to access this ticket");
        }

        return messageRepository.findByTicketIdOrderByCreatedAtAsc(ticketId).stream()
                .filter(m -> isAdminOrAgent || !m.isInternal())
                .map(m -> {
                    List<TicketAttachmentResponse> attachments = attachmentRepository.findByMessageId(m.getId()).stream()
                            .map(TicketAttachmentResponse::fromEntity)
                            .toList();
                    return TicketMessageResponse.fromEntity(m, attachments);
                })
                .toList();
    }
}
