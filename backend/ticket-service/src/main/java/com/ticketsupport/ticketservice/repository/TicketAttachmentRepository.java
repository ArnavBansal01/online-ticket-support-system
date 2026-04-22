package com.ticketsupport.ticketservice.repository;

import com.ticketsupport.ticketservice.model.entity.TicketAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketAttachmentRepository extends JpaRepository<TicketAttachment, Long> {
    List<TicketAttachment> findByTicketId(Long ticketId);

    List<TicketAttachment> findByMessageId(Long messageId);

    Optional<TicketAttachment> findTopByOrderByIdDesc();
}
