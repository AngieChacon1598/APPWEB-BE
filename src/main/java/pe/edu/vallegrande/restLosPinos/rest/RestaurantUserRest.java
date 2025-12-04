package pe.edu.vallegrande.restLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.restLosPinos.dto.ApiResponse;
import pe.edu.vallegrande.restLosPinos.dto.PageResponse;
import pe.edu.vallegrande.restLosPinos.dto.request.UsuarioRequest;
import pe.edu.vallegrande.restLosPinos.dto.response.AuthenticationResponse;
import pe.edu.vallegrande.restLosPinos.model.RestaurantUser;
import pe.edu.vallegrande.restLosPinos.service.RestaurantUserService;

import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
@RequestMapping("/restaurantuser")
@RestController
@CrossOrigin(origins = "*")

public class RestaurantUserRest {

    private final RestaurantUserService restaurantUserService;

    @GetMapping("/listar-restaurant-user")
    public PageResponse<RestaurantUser> listarRestaurantUsers(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(name = "search", required = false) String search
    ) {
        return restaurantUserService.findAll(search, page, size);
    }

    // ========== ENDPOINTS DE GESTIÓN DE USUARIOS ==========


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantUser>> getUserById(@PathVariable Integer id) {
        Optional<RestaurantUser> user = restaurantUserService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Usuario encontrado", user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ApiResponse<String>> updateUser(
            @PathVariable Integer id,
            @RequestBody UsuarioRequest usuarioRequest) {
        ApiResponse<String> response = restaurantUserService.updateUser(id, usuarioRequest);
        if (response.status()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }


    @PatchMapping("/inactivar/{id}")
    public ResponseEntity<ApiResponse<String>> deactivateUser(@PathVariable Integer id) {
        ApiResponse<String> response = restaurantUserService.deactivateUser(id);
        if (response.status()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PatchMapping("/restaurar/{id}")
    public ResponseEntity<ApiResponse<String>> restoreUser(@PathVariable Integer id) {
        ApiResponse<String> response = restaurantUserService.restoreUser(id);
        if (response.status()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }


    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Integer id) {
        ApiResponse<String> response = restaurantUserService.deleteUser(id);
        if (response.status()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }


    @GetMapping("/listar-por-estado")
    public PageResponse<RestaurantUser> listarUsuariosPorEstado(
            @Param("estado") String estado,
            @Param("page") int page,
            @Param("size") int size) {
        return restaurantUserService.findByState(estado, page, size);
    }

    @PostMapping("/crear")
    public ApiResponse<String> crearUsuario(@RequestBody UsuarioRequest register) {
        return restaurantUserService.crearUsuario(register);
    }


    @GetMapping("/mi-rol")
    public ResponseEntity<Map<String, String>> getMiRol() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities() != null) {
            String rol = auth.getAuthorities().iterator().next().getAuthority();
            Map<String, String> response = new HashMap<>();
            response.put("rol", rol);
            response.put("username", auth.getName());
            response.put("authenticated", "true");
            response.put("authorities", auth.getAuthorities().toString());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @GetMapping("/debug-auth")
    public ResponseEntity<Map<String, Object>> debugAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();

        if (auth != null) {
            response.put("authenticated", true);
            response.put("name", auth.getName());
            response.put("authorities", auth.getAuthorities());
            response.put("principal", auth.getPrincipal().getClass().getSimpleName());
            response.put("details", auth.getDetails());
        } else {
            response.put("authenticated", false);
        }

        return ResponseEntity.ok(response);
    }

}
