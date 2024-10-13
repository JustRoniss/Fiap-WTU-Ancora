package fiap.wtu_ancora.service;

import fiap.wtu_ancora.dto.AuthenticationDTO;
import fiap.wtu_ancora.dto.LoginResponseDTO;
import fiap.wtu_ancora.dto.RegisterDTO;
import fiap.wtu_ancora.model.Unit;
import fiap.wtu_ancora.model.User;
import fiap.wtu_ancora.model.UserRole;
import fiap.wtu_ancora.security.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


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
        Authentication usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.email(), authenticationDTO.password());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    public ResponseEntity<?> register (RegisterDTO registerDTO) {

        if(this.userService.findByEmail(registerDTO.email()) != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        String encryptedPassword = encryptPassword(registerDTO.password());
        Unit unit = unitService.findUnitById(registerDTO.unitId()).orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada"));
        User user = new User(registerDTO.name(), registerDTO.email(), encryptedPassword, UserRole.USER);
        user.setUnit(unit);

        userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    public String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}