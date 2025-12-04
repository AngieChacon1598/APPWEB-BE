package pe.edu.vallegrande.restLosPinos.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
public class HolaController {

  @GetMapping("/hola")
  public String hola() {
    return "Hola Mundo";
  }

}
