package fiap.wtu_ancora.controller;

import fiap.wtu_ancora.domain.User;
import fiap.wtu_ancora.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get-all")
    public List<User> getAllUsers(){
        return userService.findAllUsers();
    }
}
