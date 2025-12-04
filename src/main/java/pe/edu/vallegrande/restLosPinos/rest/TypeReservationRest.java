package pe.edu.vallegrande.restLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.vallegrande.restLosPinos.model.TypeReservation;
import pe.edu.vallegrande.restLosPinos.service.TypeReservationService;

import java.util.List;

@RestController
@RequestMapping("/typereservation")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class TypeReservationRest {
    private final TypeReservationService typeReservationService;

    @GetMapping("/tipos-reservacion")
    public List<TypeReservation> listarTiposReservacion() {
        return typeReservationService.listarTiposReservacion();
    }
}
