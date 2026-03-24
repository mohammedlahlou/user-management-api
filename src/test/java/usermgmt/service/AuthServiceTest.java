package usermgmt.service;

import usermgmt.dto.AuthResponse;
import usermgmt.dto.LoginRequest;
import usermgmt.dto.RegisterRequest;
import usermgmt.entity.Role;
import usermgmt.entity.User;
import usermgmt.exception.EmailAlreadyExistsException;
import usermgmt.repository.UserRepository;
import usermgmt.security.JwtService;
import usermgmt.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private User savedUser;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("john@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");

        savedUser = User.builder()
                .id(1L)
                .email("john@example.com")
                .password("encoded_password")
                .firstName("John")
                .lastName("Doe")
                .role(Role.USER)
                .build();
    }

    @Test
    void register_shouldReturnAuthResponse_whenEmailNotExists() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded_password");
        when(userRepository.save(any())).thenReturn(savedUser);
        when(jwtService.generateToken(any())).thenReturn("jwt_token");

        AuthResponse response = authService.register(registerRequest);

        assertThat(response.getToken()).isEqualTo("jwt_token");
        assertThat(response.getEmail()).isEqualTo("john@example.com");
        assertThat(response.getRole()).isEqualTo("USER");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {
        when(userRepository.existsByEmail(any())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldReturnAuthResponse_whenCredentialsValid() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("password123");

        when(authenticationManager.authenticate(any())).thenReturn(
                new UsernamePasswordAuthenticationToken("john@example.com", "password123")
        );
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(savedUser));
        when(jwtService.generateToken(any())).thenReturn("jwt_token");

        AuthResponse response = authService.login(loginRequest);

        assertThat(response.getToken()).isEqualTo("jwt_token");
        assertThat(response.getEmail()).isEqualTo("john@example.com");
    }
}