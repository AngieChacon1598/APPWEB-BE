package pe.edu.vallegrande.restLosPinos.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.restLosPinos.config.EncryptionUtil;
import pe.edu.vallegrande.restLosPinos.config.JwtService;
import pe.edu.vallegrande.restLosPinos.dto.ApiResponse;
import pe.edu.vallegrande.restLosPinos.dto.PageResponse;
import pe.edu.vallegrande.restLosPinos.dto.request.UsuarioRequest;
import pe.edu.vallegrande.restLosPinos.dto.response.AuthenticationResponse;
import pe.edu.vallegrande.restLosPinos.exceptions.GlobalException;
import pe.edu.vallegrande.restLosPinos.model.RestaurantUser;
import pe.edu.vallegrande.restLosPinos.model.TypeUser;
import pe.edu.vallegrande.restLosPinos.repository.RestaurantUserRepository;
import pe.edu.vallegrande.restLosPinos.repository.TypeUserRepository;
import pe.edu.vallegrande.restLosPinos.utils.ConstansApi;
import pe.edu.vallegrande.restLosPinos.utils.EncryptionService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RestaurantUserService {
    private final RestaurantUserRepository restaurantUserRepository;
    private final TypeUserRepository typeUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionService encryptionService;
    private final RestaurantUserRepository usuariosRepository;
    private final TypeUserRepository rolesRepository;
    private final EncryptionUtil encryptionUtil;
    private final JwtService jwtService;


    @Transactional
    public LocalDate convertStringToDate(String date) {
        List<DateTimeFormatter> formatos = List.of(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
        );

        for (DateTimeFormatter f : formatos) {
            try {
                return LocalDate.parse(date, f);
            } catch (DateTimeParseException ignored) {}
        }

        throw new IllegalArgumentException("Formato de fecha no válido: " + date);
    }

    public PageResponse<RestaurantUser> findAll(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantUser> restaurantUserPage;

        if (search != null && !search.trim().isEmpty()) {
            restaurantUserPage = restaurantUserRepository.searchByMultipleFields(search, pageable);
        } else {
            restaurantUserPage = restaurantUserRepository.findAll(pageable);
        }

        List<RestaurantUser> decryptedUsers = restaurantUserPage.getContent().stream()
                .map(user -> {
                    try {
                        String decryptedUsername = encryptionService.decrypt(user.getUsername());
                        user.setUserName(decryptedUsername);
                    } catch (Exception ignored) {}

                    try {
                        String decryptedRole = encryptionService.decrypt(user.getTypeUsersIdTypeUsers().getName());
                        user.getTypeUsersIdTypeUsers().setName(decryptedRole);
                    } catch (Exception ignored) {}

                    return user;
                })
                .collect(Collectors.toList());

        PageResponse<RestaurantUser> response = new PageResponse<>();
        response.setPage(page);
        response.setSize(size);
        response.setContent(decryptedUsers);
        response.setTotalElements(restaurantUserPage.getTotalElements());
        response.setTotalPages(restaurantUserPage.getTotalPages());

        return response;
    }


    public Optional<RestaurantUser> findById(Integer id) {
        return restaurantUserRepository.findById(id);
    }

    public ApiResponse<String> crearUsuario(UsuarioRequest register) {
        TypeUser roles = rolesRepository.findByName(register.getRole())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        RestaurantUser usuarios = RestaurantUser.builder()
                .userName(encryptionUtil.encrypt(register.getUsername()))
                .password(passwordEncoder.encode(register.getPassword()))
                .names(register.getNames())
                .lastName(register.getLastName())
                .birthDate(convertStringToDate(register.getBirthDate()))
                .address(register.getAddress())
                .email(register.getEmail())
                .typeDocument(register.getTypeDocument())
                .numberDocument(register.getNumberDocument())
                .typeUsersIdTypeUsers(roles)
                .state("A")
                .build();

        usuariosRepository.save(usuarios);

        return ApiResponse.success("Usuario creado con exito",null);
    }

    public ApiResponse<String> updateUser(Integer id, UsuarioRequest usuarioRequest) {
        Objects.requireNonNull(id);
        Optional<RestaurantUser> userOpt = restaurantUserRepository.findById(id);

        if (userOpt.isEmpty()) {
            return ApiResponse.error("Usuario no encontrado", null);
        }

        RestaurantUser existingUser = userOpt.get();
        
        // Actualizar campos básicos
        existingUser.setNames(usuarioRequest.getNames());
        existingUser.setLastName(usuarioRequest.getLastName());
        existingUser.setBirthDate(LocalDate.parse(usuarioRequest.getBirthDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        existingUser.setAddress(usuarioRequest.getAddress());
        existingUser.setEmail(usuarioRequest.getEmail());
        existingUser.setTypeDocument(usuarioRequest.getTypeDocument());
        existingUser.setNumberDocument(usuarioRequest.getNumberDocument());

        // Si se proporciona nueva contraseña, hashearla
        if (usuarioRequest.getPassword() != null && !usuarioRequest.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(usuarioRequest.getPassword()));
        }

        // Si se proporciona nuevo username, encriptarlo
        if (usuarioRequest.getUsername() != null && !usuarioRequest.getUsername().isEmpty()) {
            existingUser.setUserName(encryptionService.encrypt(usuarioRequest.getUsername()));
        }

        // Si se proporciona nuevo rol, actualizarlo
        if (usuarioRequest.getRole() != null && !usuarioRequest.getRole().isEmpty()) {
            TypeUser newRole = typeUserRepository.findByName(usuarioRequest.getRole())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + usuarioRequest.getRole()));
            
            // Encriptar el nombre del rol
            String encryptedRole = encryptionService.encrypt(newRole.getName());
            newRole.setName(encryptedRole);
            
            existingUser.setTypeUsersIdTypeUsers(newRole);
        }

        restaurantUserRepository.save(existingUser);
        return ApiResponse.success("Usuario actualizado exitosamente", null);
    }

    public ApiResponse<String> deactivateUser(Integer id) {
        Objects.requireNonNull(id);
        Optional<RestaurantUser> userOpt = restaurantUserRepository.findById(id);

        if (userOpt.isEmpty()) {
            return ApiResponse.error("Usuario no encontrado", null);
        }

        RestaurantUser user = userOpt.get();
        user.setState(ConstansApi.INACTIVO);
        restaurantUserRepository.save(user);
        
        return ApiResponse.success("Usuario inactivado exitosamente", null);
    }

    public ApiResponse<String> restoreUser(Integer id) {
        Objects.requireNonNull(id);
        Optional<RestaurantUser> userOpt = restaurantUserRepository.findById(id);

        if (userOpt.isEmpty()) {
            return ApiResponse.error("Usuario no encontrado", null);
        }

        RestaurantUser user = userOpt.get();
        user.setState(ConstansApi.ACTIVO);
        restaurantUserRepository.save(user);
        
        return ApiResponse.success("Usuario restaurado exitosamente", null);
    }

    public ApiResponse<String> deleteUser(Integer id) {
        Objects.requireNonNull(id);
        Optional<RestaurantUser> userOpt = restaurantUserRepository.findById(id);

        if (userOpt.isEmpty()) {
            return ApiResponse.error("Usuario no encontrado", null);
        }

        restaurantUserRepository.deleteById(id);
        return ApiResponse.success("Usuario eliminado exitosamente", null);
    }

    public PageResponse<RestaurantUser> findByState(String state, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("idUser"));
        Page<RestaurantUser> userPage;
        
        userPage = restaurantUserRepository.findByState(state, pageable);

        List<RestaurantUser> decryptedUsers = userPage.getContent().stream()
                .map(user -> {
                    try {
                        String decryptedUsername = encryptionService.decrypt(user.getUsername());
                        user.setUserName(decryptedUsername);
                    } catch (Exception e) {
                    }
                    
                    try {
                        String decryptedRole = encryptionService.decrypt(user.getTypeUsersIdTypeUsers().getName());
                        user.getTypeUsersIdTypeUsers().setName(decryptedRole);
                    } catch (Exception e) {
                    }
                    
                    return user;
                })
                .collect(java.util.stream.Collectors.toList());

        PageResponse<RestaurantUser> response = new PageResponse<>();
        response.setPage(page);
        response.setSize(size);
        response.setContent(decryptedUsers);
        response.setTotalElements(userPage.getTotalElements());
        response.setTotalPages(userPage.getTotalPages());
        return response;
    }


}
