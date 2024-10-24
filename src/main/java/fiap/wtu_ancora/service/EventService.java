package fiap.wtu_ancora.service;

import fiap.wtu_ancora.dto.EventDTO;
import fiap.wtu_ancora.dto.UnitDTO;
import fiap.wtu_ancora.dto.UserDTO;
import fiap.wtu_ancora.domain.Event;
import fiap.wtu_ancora.domain.Unit;
import fiap.wtu_ancora.domain.User;
import fiap.wtu_ancora.model.ApiReponse;
import fiap.wtu_ancora.repository.EventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class EventService {

    private final String CLIENT_URI = "http://localhost:3000";
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

    public ResponseEntity<ApiReponse<Long>> createEvent(EventDTO eventDTO) {
        try{
            Event event = new Event();

            String publickLink = eventDTO.isPublic() ? eventHashService.createSignedHash(eventDTO) : null;
            eventDTO.setPublicLink(publickLink);

            mapEventDTOToEvent(eventDTO, event);

            Map<String, String> links = new HashMap<>();
            links.put("self", "/events/create");
            links.put("edit",  "/events/edit/" + eventDTO.getId());
            links.put("delete",  "/events/delete/" + eventDTO.getId());

            ApiReponse<Long> response = new ApiReponse<>(
                    "Evento criado com sucesso",
                    HttpStatus.OK.value(),
                    links,
                    LocalDateTime.now(),
                    event.getId()
            );

            boolean hasUnit = eventDTO.getUnits() != null && !eventDTO.getUnits().isEmpty();
            boolean hasUser = eventDTO.getUsers() != null && !eventDTO.getUsers().isEmpty();

            Set<String> usersEmails = new HashSet<>();

            if (hasUnit) {
                eventDTO.getUnits().forEach(unit -> {
                    usersEmails.addAll(userService.findUsersByUnitId(unit.getId()));
                });
            }

            if(hasUser) {
                eventDTO.getUsers().forEach(user -> {
                    usersEmails.add(user.getEmail());
                });
            }

            eventRepository.save(event);

            EmailSender.sendEmailsToMultipleRecipients(usersEmails);

            return ResponseEntity.ok(response);

        } catch (Exception e){
            ApiReponse<Long> errorResponse = new ApiReponse<>(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                null,
                LocalDateTime.now(),
                null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiReponse<List<Event>>> getAllEvents() {
        try{
            List<Event> events = eventRepository.findAll();

            ApiReponse<List<Event>> response = new ApiReponse<>(
                "Eventos retornados com sucesso",
                HttpStatus.OK.value(),
                null,
                LocalDateTime.now(),
                events
            );

            return ResponseEntity.ok(response);
        } catch (Exception e){
            ApiReponse<List<Event>> errorResponse = new ApiReponse<>(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                null,
                LocalDateTime.now(),
                null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiReponse<Event>> updateEvent(Long id, EventDTO eventDTO) {

        Map<String, String> links = new HashMap<>();
        links.put("self", "/events/edit/" + eventDTO.getId());
        links.put("delete",  "/events/delete/" + eventDTO.getId());

        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            mapEventDTOToEvent(eventDTO, event);
            eventRepository.save(event);

            ApiReponse<Event> response = new ApiReponse<>(
                "Evento atualizado com sucesso",
                    HttpStatus.OK.value(),
                    links,
                    LocalDateTime.now(),
                    event
            );
            return ResponseEntity.ok(response);
        } else {
            ApiReponse<Event> errorResponse = new ApiReponse<>(
                    "Evento com o ID: " + eventDTO.getId() + " não encontrado",
                    HttpStatus.NOT_FOUND.value(),
                    null,
                    LocalDateTime.now(),
                    null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<ApiReponse<Long>> deleteEvent(Long id) {

        Map<String, String> links = new HashMap<>();
        links.put("self", "/events/delete/" + id);
        links.put("edit",  "/events/edit/" + id);
        links.put("create",  "/events/create/");

        try{
            eventRepository.deleteById(id);

            ApiReponse<Long> response = new ApiReponse<>(
                    "Evento de ID: " + id + " deletado com sucesso",
                    HttpStatus.OK.value(),
                    links,
                    LocalDateTime.now(),
                    id
            );

            return ResponseEntity.ok(response);
        }catch (Exception e){
            ApiReponse<Long> errorResponse = new ApiReponse<>(
                    "Evento de ID: " + id + " não foi deletado",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    links,
                    LocalDateTime.now(),
                    id
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Event> findEventsByUserEmail(String email) {
        return eventRepository.findEventsByUserEmail(email);
    }

    public ResponseEntity<ApiReponse<String>> findEventByPublicHash(String publicHash) {
         Optional<Event> event =  eventRepository.findByPublicLink(publicHash);
        if (event.isPresent()) {
            ApiReponse<String> response = new ApiReponse<>(
                    "Evento encontrado com sucesso",
                    HttpStatus.OK.value(),
                    null,
                    LocalDateTime.now(),
                    event.get().getIframe()
            );
            return ResponseEntity.ok(response);
        } else {
            ApiReponse<String> errorResponse = new ApiReponse<>(
                    "Nenhum evento encontrado para essa public hash",
                    HttpStatus.NOT_FOUND.value(),
                    null,
                    LocalDateTime.now(),
                    null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<ApiReponse<String>> createPublicLink(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            String link = event.getPublicLink();
            ApiReponse<String> response = new ApiReponse<>(
                    "Link publico criado",
                    HttpStatus.OK.value(),
                    null,
                    LocalDateTime.now(),
                    CLIENT_URI + "/events/public/" + link
            );

            return ResponseEntity.ok(response);
        }else {
            ApiReponse<String> errorResponse = new ApiReponse<>(
                    "Erro ao criar link publico",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null,
                    LocalDateTime.now(),
                    null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }


//    private ApiReponse<List<Event>>

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
