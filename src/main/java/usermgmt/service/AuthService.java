package usermgmt.service;

import usermgmt.dto.AuthResponse;
import usermgmt.dto.LoginRequest;
import usermgmt.dto.RegisterRequest;
import usermgmt.entity.Role;
import usermgmt.entity.User;
import usermgmt.exception.EmailAlreadyExistsException;
import usermgmt.repository.UserRepository;
import usermgmt.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        // Construire et sauvegarder le nouvel utilisateur
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        // Générer le token JWT
        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        // Spring Security vérifie email + password via AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Si on arrive ici, les credentials sont valides
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .build();
    }
}
