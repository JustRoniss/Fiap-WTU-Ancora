package fiap.wtu_ancora.controller;

import fiap.wtu_ancora.domain.Invite;
import fiap.wtu_ancora.model.ApiReponse;
import fiap.wtu_ancora.service.InviteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/invites")

public class InviteController {

    private InviteService inviteService;

    public InviteController(InviteService inviteService){

        this.inviteService = inviteService;
    }

    @GetMapping("/get-invites/{email}")
    public ResponseEntity<ApiReponse<List<Invite>>> getEventsForUser(@PathVariable String email) {
        return inviteService.getUserInvites(email);
    }
}
