package pe.edu.vallegrande.restLosPinos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.restLosPinos.model.TypeReservation;
import pe.edu.vallegrande.restLosPinos.repository.TypeReservationRepository;
import pe.edu.vallegrande.restLosPinos.utils.ConstansApi;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeReservationService {
    private final TypeReservationRepository typeReservationRepository;

    public List<TypeReservation> listarTiposReservacion() {
        return typeReservationRepository.findByState(ConstansApi.ACTIVO)
                .stream()
                .map(tipo -> TypeReservation
                        .builder()
                        .idTypeReservation(tipo.getIdTypeReservation())
                        .name(tipo.getName())
                        .build())
                .toList();
    }


}
