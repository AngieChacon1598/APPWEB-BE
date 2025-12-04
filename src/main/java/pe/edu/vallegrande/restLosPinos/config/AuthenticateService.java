package pe.edu.vallegrande.restLosPinos.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.restLosPinos.dto.request.AuthenticationRequest;
import pe.edu.vallegrande.restLosPinos.dto.request.UsuarioRequest;
import pe.edu.vallegrande.restLosPinos.dto.response.AuthenticationResponse;
import pe.edu.vallegrande.restLosPinos.model.RestaurantUser;
import pe.edu.vallegrande.restLosPinos.model.TypeUser;
import pe.edu.vallegrande.restLosPinos.repository.RestaurantUserRepository;
import pe.edu.vallegrande.restLosPinos.repository.TypeUserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticateService {

    private final JwtService jwtService;
    private final RestaurantUserRepository usuariosRepository;
    private final TypeUserRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EncryptionUtil encryptionUtil;


    @Transactional
    public AuthenticationResponse registerUser(UsuarioRequest register) {
        TypeUser roles = rolesRepository.findByName(register.getRole())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        RestaurantUser usuarios = RestaurantUser.builder()
                .userName(encryptionUtil.encrypt(register.getUsername()))
                .password(passwordEncoder.encode(register.getPassword()))
                .names(register.getNames())
                .lastName(register.getLastName())
                .birthDate(convertStringToDate(register.getBirthDate()))
                .address(register.getAddress())
                .email(register.getEmail())
                .typeDocument(register.getTypeDocument())
                .numberDocument(register.getNumberDocument())
                .typeUsersIdTypeUsers(roles)
                .state("A")
                .build();

        usuariosRepository.save(usuarios);

        String jwt = jwtService.generateToken(usuarios);

        // Safely decrypt username - if it fails, use the username as is
        String decryptedUsername;
        try {
            decryptedUsername = encryptionUtil.decrypt(usuarios.getUsername());
        } catch (Exception e) {
            decryptedUsername = usuarios.getUsername();
        }

        return AuthenticationResponse.builder()
                .userName(decryptedUsername)
                .role(usuarios.getTypeUsersIdTypeUsers().getName())
                .token(jwt)
                .build();
    }

    @Transactional
    public LocalDate convertStringToDate(String date) {
        List<DateTimeFormatter> formatos = List.of(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
        );

        for (DateTimeFormatter f : formatos) {
            try {
                return LocalDate.parse(date, f);
            } catch (DateTimeParseException ignored) {}
        }

        throw new IllegalArgumentException("Formato de fecha no válido: " + date);
    }

    @Transactional
    public AuthenticationResponse loginUser(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        String encryptedUsername = encryptionUtil.encrypt(request.getUsername());

        RestaurantUser usuarios = usuariosRepository.findByUserNameWithRole(encryptedUsername)
                .or(() -> usuariosRepository.findByUserNameWithRole(request.getUsername()))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String jwtToken = jwtService.generateToken(usuarios);
        
        String decryptedUsername;
        try {
            decryptedUsername = encryptionUtil.decrypt(usuarios.getUsername());
        } catch (Exception e) {
            decryptedUsername = usuarios.getUsername();
        }
        
        return AuthenticationResponse.builder()
                .userName(decryptedUsername)
                .role(usuarios.getTypeUsersIdTypeUsers().getName())
                .token(jwtToken)
                .idUser(usuarios.getIdUser())
                .build();
    }

}
