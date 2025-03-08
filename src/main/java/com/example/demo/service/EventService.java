package com.example.demo.service;

import com.example.demo.entity.Event;
import com.example.demo.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event){
        if(event.getAvailableSeats() <= 0 || event.getDate().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("INVALID DATE OR INVALID AVAILABLE SEATS.");
        }

        return eventRepository.save(event);
    }

    public List<Event> getUpcomingEvents(){
        return eventRepository.findByDateAfterOrderByDateAsc(LocalDate.now());
    }
}
