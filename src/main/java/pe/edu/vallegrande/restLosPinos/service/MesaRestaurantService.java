package pe.edu.vallegrande.restLosPinos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.restLosPinos.dto.ApiResponse;
import pe.edu.vallegrande.restLosPinos.dto.request.MesaRestaurantRequest;
import pe.edu.vallegrande.restLosPinos.model.MesaRestaurante;
import pe.edu.vallegrande.restLosPinos.repository.MesaRestauranteRepository;
import pe.edu.vallegrande.restLosPinos.utils.ConstansApi;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MesaRestaurantService {

    private final MesaRestauranteRepository mesaRestauranteRepository;

    public ApiResponse<List<MesaRestaurante>> listarMesas(String estado) {
        List<MesaRestaurante> mesas = mesaRestauranteRepository.findByState(estado);
        return ApiResponse.success("Listado de mesas", mesas);
    }

    public ResponseEntity<String> saveMesa(MesaRestaurantRequest request) {
        List<MesaRestaurante> mesasExistentes = mesaRestauranteRepository.findByNumberTable(request.getNumberTable());

        if (!mesasExistentes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe una mesa con el número " + request.getNumberTable());
        }

        MesaRestaurante mesa = MesaRestaurante.builder()
                .numberTable(request.getNumberTable())
                .ability(request.getAbility())
                .descripcion(request.getDescripcion())
                .imagen(request.getImagen())
                .state(ConstansApi.ACTIVO)
                .build();

        mesaRestauranteRepository.save(mesa);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Mesa guardada con éxito");
    }

    public ResponseEntity<String> actualizarMesas(Integer id, MesaRestaurantRequest request) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID de la mesa no puede ser nulo");
        }

        return mesaRestauranteRepository.findById(id)
                .map(mesa -> {


                    mesa.setNumberTable(request.getNumberTable());
                    mesa.setAbility(request.getAbility());
                    mesa.setDescripcion(request.getDescripcion());
                    mesa.setImagen(request.getImagen());

                    mesaRestauranteRepository.save(mesa);

                    return ResponseEntity.status(HttpStatus.CREATED).body("Mesa actualizada con éxito");
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    public ApiResponse<String> deleteMesa(Integer id) {
        Objects.requireNonNull(id);
        Optional<MesaRestaurante> mesaOpt = mesaRestauranteRepository.findById(id);

        if (mesaOpt.isEmpty()) {
            return ApiResponse.error("Mesa no encontrada", null);
        }
        MesaRestaurante mesaElejida = mesaOpt.get();

        mesaElejida.setState(ConstansApi.INACTIVO);
        mesaRestauranteRepository.save(mesaElejida);
        return ApiResponse.success("Se inactivo la mesa " + mesaElejida.getNumberTable(), null);
    }

    public ApiResponse<String> restoreMesa(Integer id) {
        Objects.requireNonNull(id);
        Optional<MesaRestaurante> mesaOpt = mesaRestauranteRepository.findById(id);

        if (mesaOpt.isEmpty()) {
            return ApiResponse.error("Mesa no encontrada", null);
        }
        MesaRestaurante mesaElejida = mesaOpt.get();

        mesaElejida.setState(ConstansApi.ACTIVO);
        mesaRestauranteRepository.save(mesaElejida);
        return ApiResponse.success("Se reactivo la mesa " + mesaElejida.getNumberTable(), null);
    }
}
