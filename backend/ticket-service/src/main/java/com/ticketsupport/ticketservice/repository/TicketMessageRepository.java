package com.ticketsupport.ticketservice.repository;

import com.ticketsupport.ticketservice.model.entity.TicketMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketMessageRepository extends JpaRepository<TicketMessage, Long> {
    List<TicketMessage> findByTicketIdOrderByCreatedAtAsc(Long ticketId);

    Optional<TicketMessage> findTopByOrderByIdDesc();
}
