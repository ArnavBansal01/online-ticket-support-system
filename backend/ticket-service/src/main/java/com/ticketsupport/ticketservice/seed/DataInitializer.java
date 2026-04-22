package com.ticketsupport.ticketservice.seed;

import com.ticketsupport.ticketservice.model.entity.Ticket;
import com.ticketsupport.ticketservice.model.entity.TicketMessage;
import com.ticketsupport.ticketservice.repository.TicketMessageRepository;
import com.ticketsupport.ticketservice.repository.TicketRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements ApplicationRunner {
    private final TicketRepository ticketRepository;
    private final TicketMessageRepository messageRepository;

    public DataInitializer(TicketRepository ticketRepository, TicketMessageRepository messageRepository) {
        this.ticketRepository = ticketRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (ticketRepository.count() > 0) {
            return;
        }

        for (long i = 1; i <= 10; i++) {
            Ticket ticket = new Ticket();
            ticket.setId(i);
            ticket.setTitle("Sample Ticket " + i);
            ticket.setDescription("This is seeded ticket " + i + " for development and dashboard testing.");
            ticket.setStatus(i % 4 == 0 ? Ticket.Status.RESOLVED : i % 3 == 0 ? Ticket.Status.IN_PROGRESS : Ticket.Status.OPEN);
            ticket.setPriority(i % 4 == 0 ? Ticket.Priority.URGENT : i % 3 == 0 ? Ticket.Priority.HIGH : Ticket.Priority.MEDIUM);
            ticket.setCategory(i % 2 == 0 ? Ticket.Category.TECHNICAL : Ticket.Category.BILLING);
            ticket.setCreatedBy(3L); // Fixed convention from User Service seed
            ticket.setAssignedTo(i % 2 == 0 ? 2L : null); // Fixed convention from User Service seed
            ticket.setCreatedAt(LocalDateTime.now().minusDays(10 - i));
            ticket.setUpdatedAt(LocalDateTime.now().minusDays(10 - i));
            ticket.setResolvedAt(ticket.getStatus() == Ticket.Status.RESOLVED ? LocalDateTime.now().minusDays(1) : null);
            ticket.setDeleted(false);
            ticketRepository.save(ticket);

            TicketMessage message = new TicketMessage();
            message.setId(i);
            message.setTicketId(i);
            message.setSenderId(3L);
            message.setSenderRole("CUSTOMER");
            message.setContent("Initial issue description for ticket " + i);
            message.setCreatedAt(ticket.getCreatedAt());
            message.setInternal(false);
            messageRepository.save(message);
        }
    }
}
