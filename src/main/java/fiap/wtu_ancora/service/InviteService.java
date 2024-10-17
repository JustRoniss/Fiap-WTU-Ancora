package fiap.wtu_ancora.service;

import fiap.wtu_ancora.domain.Event;
import fiap.wtu_ancora.domain.Invite;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InviteService {

    private final EventService eventService;

    public InviteService(EventService eventService) {
        this.eventService = eventService;
    }

    public ResponseEntity<?> getUserInvites(String userEmail){
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
           return ResponseEntity.ok(invites);
       } catch (Exception e) {
           return new ResponseEntity<>("Error on invite service", HttpStatus.INTERNAL_SERVER_ERROR);
       }

    }
}
