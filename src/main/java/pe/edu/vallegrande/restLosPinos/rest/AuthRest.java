package pe.edu.vallegrande.restLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.restLosPinos.config.AuthenticateService;
import pe.edu.vallegrande.restLosPinos.dto.request.AuthenticationRequest;
import pe.edu.vallegrande.restLosPinos.dto.request.UsuarioRequest;
import pe.edu.vallegrande.restLosPinos.dto.response.AuthenticationResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthRest {
    private final AuthenticateService authenticateService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(authenticateService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticateService.loginUser(request));
    }

}
