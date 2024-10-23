package fiap.wtu_ancora.service;

import fiap.wtu_ancora.dto.AuthenticationDTO;
import fiap.wtu_ancora.dto.LoginResponseDTO;
import fiap.wtu_ancora.dto.RegisterDTO;
import fiap.wtu_ancora.domain.Unit;
import fiap.wtu_ancora.domain.User;
import fiap.wtu_ancora.domain.UserRole;
import fiap.wtu_ancora.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final TokenService tokenService;

    private final UnitService unitService;

    public AuthenticationService(AuthenticationManager authenticationManager, UserService userService, TokenService tokenService, UnitService unitService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
        this.unitService = unitService;
    }

    public ResponseEntity<?> authenticate(AuthenticationDTO authenticationDTO) {
        try{
            Authentication usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.email(), authenticationDTO.password());
            Authentication auth = this.authenticationManager.authenticate(usernamePassword);

            String token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        }catch (Exception e){
            return new ResponseEntity<>("Error to authenticate", HttpStatus.UNAUTHORIZED);
        }

    }

    public ResponseEntity<?> register (RegisterDTO registerDTO) {

        Optional<Unit> unit = unitService.findUnitById(registerDTO.unitId());

        if(this.userService.findByEmail(registerDTO.email()) != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        if(unit.isEmpty()) {
            return ResponseEntity.badRequest().body("Unit does not exist");
        }
        
        String encryptedPassword = encryptPassword(registerDTO.password());
        User user = new User(registerDTO.name(), registerDTO.email(), encryptedPassword, UserRole.USER);

        unit.ifPresent(user::setUnit);

        userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    public String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
