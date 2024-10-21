package fiap.wtu_ancora.service;

import fiap.wtu_ancora.domain.Event;
import fiap.wtu_ancora.domain.Invite;
import fiap.wtu_ancora.model.ApiReponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InviteService {

    private final EventService eventService;

    public InviteService(EventService eventService) {
        this.eventService = eventService;
    }

    public ResponseEntity<ApiReponse<List<Invite>>> getUserInvites(String userEmail){
       try{
           List<Event> events = eventService.findEventsByUserEmail(userEmail);
           List<Invite> invites = new ArrayList<>();
           for (Event event : events) {
               Invite invite = new Invite();

               invite.setTitle(event.getTitle());
               invite.setDescription(event.getDescription());
               invite.setIframe(event.getIframe());
               invite.setStartDate(event.getStartDate());
               invite.setEndDate(event.getEndDate());
               invites.add(invite);

           }

           ApiReponse<List<Invite>> reponse = new ApiReponse<>(
                   "Convites encontrados para o e-mail: " + userEmail,
                   HttpStatus.OK.value(),
                   null,
                   LocalDateTime.now(),
                   invites
           );

           return ResponseEntity.ok(reponse);
       } catch (Exception e) {
           ApiReponse<List<Invite>> errorReponse = new ApiReponse<>(
                   "Ocorreu um erro ao buscar convites para o e-mail: " + userEmail,
                   HttpStatus.INTERNAL_SERVER_ERROR.value(),
                   null,
                   LocalDateTime.now(),
                   null
           );
           return new ResponseEntity<>(errorReponse, HttpStatus.INTERNAL_SERVER_ERROR);
       }

    }
}
