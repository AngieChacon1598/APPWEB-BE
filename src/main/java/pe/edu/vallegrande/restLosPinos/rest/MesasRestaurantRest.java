package pe.edu.vallegrande.restLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.restLosPinos.dto.ApiResponse;
import pe.edu.vallegrande.restLosPinos.dto.request.MesaRestaurantRequest;
import pe.edu.vallegrande.restLosPinos.model.MesaRestaurante;
import pe.edu.vallegrande.restLosPinos.service.MesaRestaurantService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mesas")
@CrossOrigin(origins = "*")
public class MesasRestaurantRest {

    private final MesaRestaurantService mesaRestaurantService;

    @GetMapping
    public ApiResponse<List<MesaRestaurante>> listarMesas(
            @RequestParam(value = "estado", required = false) String estado) {
        return mesaRestaurantService.listarMesas(estado);
    }

    @PostMapping("/guardar-mesas")
    public ResponseEntity<String> guardarMesas(@RequestBody MesaRestaurantRequest mesaRestaurante) {
        return mesaRestaurantService.saveMesa(mesaRestaurante);
    }

    @PatchMapping("/actualizar-mesas")
    public ResponseEntity<String> actualizarMesas(
            @RequestParam("idMesa") Integer id,
            @RequestBody MesaRestaurantRequest mesaRestaurante) {
        return mesaRestaurantService.actualizarMesas(id, mesaRestaurante);
    }

    @PatchMapping("/inactivar-mesas/{id}")
    public ApiResponse<String> desactivarMesas(
            @PathVariable("id") Integer id) {
        return mesaRestaurantService.deleteMesa(id);
    }

    @PatchMapping("/reactivar-mesas/{id}")
    public ApiResponse<String> reactivarMesas(
            @PathVariable("id") Integer id) {
        return mesaRestaurantService.restoreMesa(id);
    }

}
