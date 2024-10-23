package fiap.wtu_ancora.controller;

import fiap.wtu_ancora.domain.Event;
import fiap.wtu_ancora.dto.EventDTO;
import fiap.wtu_ancora.model.ApiReponse;
import fiap.wtu_ancora.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Esse endpoint por conta do relacionamento entre as entidades do dominio, devolve tudo, inclusive informacoes que nao sao pertinetes a eventos.
    // Para filtrar isso, foi criado o endpoint /get-invites/{email} que devolve um Invite, que contém apenas as informacoes necessarias.
    @GetMapping("/get-all")
    public ResponseEntity<ApiReponse<List<Event>>>getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping("/create")
    public ResponseEntity<ApiReponse<Long>> createEvent(@RequestBody EventDTO eventDto) {
        return eventService.createEvent(eventDto);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ApiReponse<Event>> editEvent(@PathVariable Long id, @RequestBody EventDTO eventDetails) {
        return eventService.updateEvent(id, eventDetails);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiReponse<Long>> deleteEvent(@PathVariable Long id){
        return eventService.deleteEvent(id);
    }

    @GetMapping("/public/{publicHash}")
    public ResponseEntity<ApiReponse<String>> getEventByPublicHash(@PathVariable String publicHash) {
         return eventService.findEventByPublicHash(publicHash);
    }

    @GetMapping("/public/{eventId}/create-public-link")
    public ResponseEntity<ApiReponse<String>> createPublicLink(@PathVariable Long eventId) {
        return eventService.createPublicLink(eventId);
    }
}
