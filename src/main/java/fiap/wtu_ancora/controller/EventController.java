package fiap.wtu_ancora.controller;

import fiap.wtu_ancora.dto.EventDTO;
import fiap.wtu_ancora.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<?>getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@RequestBody EventDTO eventDto) {
        return eventService.createEvent(eventDto);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editEvent(@PathVariable Long id, @RequestBody EventDTO eventDetails) {
        return eventService.updateEvent(id, eventDetails);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id){
        return eventService.deleteEvent(id);
    }

    @GetMapping("/public/{publicHash}")
    public ResponseEntity<?> getEventByPublicHash(@PathVariable String publicHash) {
         return eventService.findEventByPublicHash(publicHash);
    }

    @GetMapping("/public/{eventId}/create-public-link")
    public ResponseEntity<?> createPublicLink(@PathVariable Long eventId) {
        return eventService.createPublicLink(eventId);
    }
}
