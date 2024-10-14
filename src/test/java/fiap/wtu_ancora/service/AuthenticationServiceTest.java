package fiap.wtu_ancora.service;

import fiap.wtu_ancora.dto.AuthenticationDTO;
import fiap.wtu_ancora.dto.LoginResponseDTO;
import fiap.wtu_ancora.dto.RegisterDTO;
import fiap.wtu_ancora.model.Unit;
import fiap.wtu_ancora.model.User;
import fiap.wtu_ancora.model.UserRole;
import fiap.wtu_ancora.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private UnitService unitService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAutheticateWithSuccess(){
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("fake@teste.com", "fakePassword");
        User mockUser = new User("user", "fake@teste.com", "fakePassword", UserRole.USER);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(tokenService.generateToken(mockUser)).thenReturn("fakeToken");

        ResponseEntity<?> response = authenticationService.authenticate(authenticationDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("fakeToken", ((LoginResponseDTO) Objects.requireNonNull(response.getBody())).token());
        verify(tokenService).generateToken(mockUser);
        verify(authenticationManager, Mockito.times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testAutheticateWithFailure(){
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("fake@teste.com", "fakePassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException("Authentication Failed"));

        ResponseEntity<?> response = authenticationService.authenticate(authenticationDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Error to authenticate", response.getBody());
        verify(authenticationManager, Mockito.times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testRegisterWithSuccess(){
        RegisterDTO registerDTO = new RegisterDTO("Fake User", "fake@teste.com", "fakePassword", UserRole.USER, 1L);
        Unit mockUnit = new Unit();

        when(userService.findByEmail(registerDTO.email())).thenReturn(null);
        when(unitService.findUnitById(registerDTO.unitId())).thenReturn(Optional.of(mockUnit));
        when(userService.saveUser(any(User.class))).thenReturn(new User());

        ResponseEntity<?> response = authenticationService.register(registerDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).findByEmail(registerDTO.email());
        verify(unitService, times(1)).findUnitById(registerDTO.unitId());
        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    public void testRegisterWithEmailExists(){
        RegisterDTO registerDTO = new RegisterDTO("Fake User", "fake@teste.com", "fakePassword", UserRole.USER, 1L);
        User mockUser = new User("user", "fake@teste.com", "fakePassword", UserRole.USER);

        when(userService.findByEmail(registerDTO.email())).thenReturn(mockUser);

        ResponseEntity<?> response = authenticationService.register(registerDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already exists", response.getBody());
        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    public void testRegisterWithInvalidUnit(){
        RegisterDTO registerDTO = new RegisterDTO("Fake User", "fake@teste.com", "fakePassword", UserRole.USER, 1L);

        when(userService.findByEmail(registerDTO.email())).thenReturn(null);
        when(unitService.findUnitById(registerDTO.unitId())).thenReturn(Optional.empty());

        ResponseEntity<?> response = authenticationService.register(registerDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Unit does not exist", response.getBody());
        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    public void testEncryptPassword() {
        String rawPassword = "fakePassword";
        String encryptedPassword = authenticationService.encryptPassword(rawPassword);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches(rawPassword, encryptedPassword));
    }
}
