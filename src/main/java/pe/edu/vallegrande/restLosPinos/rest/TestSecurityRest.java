package pe.edu.vallegrande.restLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.restLosPinos.dto.ApiResponse;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TestSecurityRest {

    @GetMapping("/protected")
    public ResponseEntity<ApiResponse<String>> testProtectedEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String authorities = authentication.getAuthorities().toString();
        
        return ResponseEntity.ok(ApiResponse.success(
            "Acceso exitoso al endpoint protegido", 
            "Usuario: " + username + " | Roles: " + authorities
        ));
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<String>> testPublicEndpoint() {
        return ResponseEntity.ok(ApiResponse.success(
            "Este endpoint debería fallar porque no está permitido", 
            "Error: Este endpoint no debería ser accesible"
        ));
    }
}
