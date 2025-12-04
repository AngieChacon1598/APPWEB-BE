package pe.edu.vallegrande.restLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.vallegrande.restLosPinos.model.TypeUser;
import pe.edu.vallegrande.restLosPinos.service.TypeUserService;

import java.util.List;

@RequestMapping("/typeuser")
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")

public class TypeUserRest {

    private final TypeUserService typeUserService;

    @GetMapping("/listar-usuario")
    public List<TypeUser> listarTiposUsuario(){
        return typeUserService.findAll();
    }


}
