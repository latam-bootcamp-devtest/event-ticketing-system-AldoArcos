package com.example.demo.controller;

import com.example.demo.entity.Event;
import com.example.demo.service.EventService;
import jakarta.persistence.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService){
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event){
        return ResponseEntity.status(201).body(eventService.createEvent(event));
    }

    @GetMapping
    public ResponseEntity<List<Event>> getUpcomingEvents(){
        return ResponseEntity.ok(eventService.getUpcomingEvents());
    }

}
