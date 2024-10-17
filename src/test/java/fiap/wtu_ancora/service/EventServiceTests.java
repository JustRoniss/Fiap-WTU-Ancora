package fiap.wtu_ancora.service;

import fiap.wtu_ancora.TestUtils;
import fiap.wtu_ancora.dto.EventDTO;
import fiap.wtu_ancora.domain.Event;
import fiap.wtu_ancora.domain.Unit;
import fiap.wtu_ancora.domain.User;
import fiap.wtu_ancora.model.ApiReponse;
import fiap.wtu_ancora.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.junit.jupiter.api.Assertions.*;


public class EventServiceTests {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UnitService unitService;

    @Mock
    private UserService userService;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateEventWithSuccess(){
        EventDTO eventDTO = TestUtils.createEventDTOFake();
        eventDTO.setId(10L);

        Set<Unit> units = new HashSet<>();
        Set<User> users = new HashSet<>();

        Mockito.when(unitService.findUnitsByIds(anySet())).thenReturn(units);
        Mockito.when(userService.findByEmails(anySet())).thenReturn(users);

        Mockito.when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> {
            Event savedEvent = invocation.getArgument(0);
            savedEvent.setId(10L);
            return savedEvent;
        });

        ResponseEntity<ApiReponse<Long>> response = eventService.createEvent(eventDTO);
        ApiReponse<Long> apiReponse = response.getBody();

        assertNotNull(apiReponse);
        assertEquals(10L, apiReponse.getData());
        assertEquals("Evento criado com sucesso", apiReponse.getMessage());

        Mockito.verify(eventRepository, Mockito.times(1)).save(any(Event.class));
    }

    @Test
    public void testCreateEventWithError(){
        EventDTO eventDTO = TestUtils.createEventDTOFake();
        Mockito.when(unitService.findUnitsByIds(anySet())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<ApiReponse<Long>> response = eventService.createEvent(eventDTO);
        ApiReponse<Long> apiReponse = response.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error", apiReponse.getMessage());

        Mockito.verify(eventRepository, Mockito.never()).save(any(Event.class));
    }

    @Test
    public void testUpdateEventWithSuccess(){
        Long eventId = 1L;
        EventDTO eventDTO = TestUtils.createEventDTOFake();
        Event event = new Event();
        Set<Unit> units = new HashSet<>();
        Set<User> users = new HashSet<>();

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        Mockito.when(unitService.findUnitsByIds(anySet())).thenReturn(units);
        Mockito.when(userService.findByEmails(anySet())).thenReturn(users);
        Mockito.when(eventRepository.save(any(Event.class))).thenReturn(event);

        ResponseEntity<ApiReponse<Event>> response = eventService.updateEvent(eventId, eventDTO);
        ApiReponse<Event> apiReponse = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Evento atualizado com sucesso", apiReponse.getMessage());
        assertEquals(event, apiReponse.getData());

        Mockito.verify(eventRepository, Mockito.times(1)).save(event);
    }

    @Test
    public void testUpdateEventWithError(){
        Long eventId = 1L;
        EventDTO eventDTO = TestUtils.createEventDTOFake();

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        ResponseEntity<ApiReponse<Event>> response = eventService.updateEvent(eventId, eventDTO);
        ApiReponse<Event> apiReponse = response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Evento com o ID: " + eventDTO.getId() + " não encontrado", apiReponse.getMessage());

        Mockito.verify(eventRepository, Mockito.never()).save(any(Event.class));
    }

    @Test
    public void testDeleteEventWithSuccess(){
        Long eventId = 1L;

        Mockito.doNothing().when(eventRepository).deleteById(eventId);

        ResponseEntity<ApiReponse<Long>> response = eventService.deleteEvent(eventId);
        ApiReponse<Long> apiReponse = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Evento de ID: " + eventId + " deletado com sucesso", apiReponse.getMessage());
        assertEquals(eventId, apiReponse.getData());

        Mockito.verify(eventRepository, Mockito.times(1)).deleteById(eventId);
    }

    @Test
    public void testDeleteEventWithError(){
        Long eventId = 1L;

        Mockito.doThrow(new RuntimeException("Delete error")).when(eventRepository).deleteById(eventId);

        ResponseEntity<ApiReponse<Long>> response = eventService.deleteEvent(eventId);
        ApiReponse<Long> apiReponse = response.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Evento de ID: " + eventId + " não foi deletado", apiReponse.getMessage());

        Mockito.verify(eventRepository, Mockito.times(1)).deleteById(eventId);
    }


}
