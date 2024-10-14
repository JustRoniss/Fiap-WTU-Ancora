package fiap.wtu_ancora.controller;

import fiap.wtu_ancora.model.Event;
import fiap.wtu_ancora.repository.EventRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/invites")

//TO DO: Refatorar essa parte

// 1- Adicionar service
// 2- Criar lógica para não retornar todos os dados do evento, retornar somente aqui que é necessário;

public class InviteController {

    private EventRepository eventRepository;

    public InviteController(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    @GetMapping("/get-invites/{email}")
    public ResponseEntity<List<Event>> getEventsForUser(@PathVariable String email) {
        List<Event> events = eventRepository.findEventsByUserEmail(email);
        return ResponseEntity.ok(events);
    }
}
