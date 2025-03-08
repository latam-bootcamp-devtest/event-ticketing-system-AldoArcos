package com.example.demo.controller;

import com.example.demo.entity.Event;
import com.example.demo.entity.Ticket;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.TicketRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;

    public TicketController(TicketRepository ticketRepository, EventRepository eventRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public ResponseEntity<?> bookTicket(@RequestParam Long userId, @RequestParam Long eventId){
        Optional<Event> eventOpt = eventRepository.findById(eventId);

        if(eventOpt.isEmpty()){
            return ResponseEntity.status(404).body("EVENT NOT FOUND");
        }
        Event event = eventOpt.get();
        if(event.getAvailableSeats() <= 0){
            return ResponseEntity.status(409).body("NO AVAILABLE SEATS");
        }

        Ticket ticket = new Ticket(null, userId, event);

        ticketRepository.save(ticket);

        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventRepository.save(event);

        return ResponseEntity.status(201).body(ticket);

    }

    @DeleteMapping("/tickets/{ticketId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long ticketId){
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);

        if(ticketOpt.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Ticket ticket = ticketOpt.get();
        Event event = ticket.getEvent();

        if(event.getDate().isBefore(LocalDate.now())){
            return ResponseEntity.badRequest().body(null);
        }

        event.setAvailableSeats(event.getAvailableSeats() + 1);
        eventRepository.save(event);

        ticketRepository.delete(ticket);

        return ResponseEntity.noContent().build();
    }
}
