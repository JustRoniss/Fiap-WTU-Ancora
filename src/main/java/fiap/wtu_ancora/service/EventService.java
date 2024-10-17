package fiap.wtu_ancora.service;

import fiap.wtu_ancora.dto.EventDTO;
import fiap.wtu_ancora.dto.UnitDTO;
import fiap.wtu_ancora.dto.UserDTO;
import fiap.wtu_ancora.model.Event;
import fiap.wtu_ancora.model.Unit;
import fiap.wtu_ancora.model.User;
import fiap.wtu_ancora.repository.EventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventHashService eventHashService;
    private final UnitService unitService;
    private final UserService userService;


    public EventService(EventRepository eventRepository, EventHashService eventHashService ,UnitService unitService, UserService userService) {
        this.eventRepository = eventRepository;
        this.unitService = unitService;
        this.userService = userService;
        this.eventHashService = eventHashService;
    }

    public ResponseEntity<?> createEvent(EventDTO eventDTO) {
        try{
            Event event = new Event();

            String publickLink = eventDTO.isPublic() ? eventHashService.createSignedHash(eventDTO) : null;
            eventDTO.setPublicLink(publickLink);

            mapEventDTOToEvent(eventDTO, event);

            eventRepository.save(event);
            return ResponseEntity.ok(event.getId());
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getAllEvents() {
        return ResponseEntity.ok(eventRepository.findAll());
    }

    public ResponseEntity<?> updateEvent(Long id, EventDTO eventDTO) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            mapEventDTOToEvent(eventDTO, event);
            eventRepository.save(event);
            return ResponseEntity.ok("Successfully updated the event");
        } else {
            return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> deleteEvent(Long id) {
        try{
            eventRepository.deleteById(id);
            return ResponseEntity.ok("Event deleted successfully");
        }catch (Exception e){
            return new ResponseEntity<>("Error to delete this event", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Event> findEventsByUserEmail(String email) {
        return eventRepository.findEventsByUserEmail(email);
    }

    public ResponseEntity<?> findEventByPublicHash(String publicHash) {
         Optional<Event> event =  eventRepository.findByPublicLink(publicHash);
        if (event.isPresent()) {
            return ResponseEntity.ok(event.get().getIframe());
        } else {
            return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> createPublicLink(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            String link = event.getPublicLink();
            return ResponseEntity.ok("http:localhost:3000/events/public/"+link);
        }else {
            return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
        }
    }

    private Event mapEventDTOToEvent(EventDTO eventDTO, Event event) {
        Set<Long> unitIds = eventDTO.getUnits() != null
                ? eventDTO.getUnits().stream()
                .map(UnitDTO::getId)
                .collect(Collectors.toSet())
                : new HashSet<>();

        Set<String> userEmails = eventDTO.getUsers() != null
                ? eventDTO.getUsers().stream()
                .map(UserDTO::getEmail)
                .collect(Collectors.toSet())
                : new HashSet<>();

        Set<Unit> units = unitService.findUnitsByIds(unitIds);
        Set<User> users = userService.findByEmails(userEmails);

        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setStartDate(eventDTO.getStartDate());
        event.setEndDate(eventDTO.getEndDate());
        event.setUnits(units);
        event.setUsers(users);
        event.setIframe(eventDTO.getIframe());
        event.setPublic(eventDTO.isPublic());
        event.setPublicLink(eventDTO.getPublicLink());
        return event;
    }
}
