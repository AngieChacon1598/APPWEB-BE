package pe.edu.vallegrande.restLosPinos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.restLosPinos.model.TypeUser;
import pe.edu.vallegrande.restLosPinos.repository.TypeUserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TypeUserService {

    private final TypeUserRepository typeUserRepository;

    public List<TypeUser> findAll(){
        return typeUserRepository.findAll();
    }
}
