package pe.edu.vallegrande.restLosPinos.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {

    private String username;

    private String password;

    private String names;

    private String lastName;

    private String birthDate;

    private String address;

    private String email;

    private String typeDocument;

    private String numberDocument;

    private String role;

}
