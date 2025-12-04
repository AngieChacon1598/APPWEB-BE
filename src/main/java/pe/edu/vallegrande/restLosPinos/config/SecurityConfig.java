package pe.edu.vallegrande.restLosPinos.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final FilterSecurityToken filterSecurityToken;
    private final VerifyUserDetailsService verifyUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos (sin autenticación)
                        .requestMatchers("/auth/**").permitAll()

                        // ========== PERMISOS DE ADMIN ==========
                        // ADMIN: CRUD completo en todas las tablas
                        .requestMatchers("/v1/api/**").permitAll()
                        .requestMatchers("/reservaciones/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers("/category/**").hasRole("ADMIN")
                        .requestMatchers("/typeuser/**").hasRole("ADMIN")
                        .requestMatchers("/typereservation/**").hasRole("ADMIN")
                        .requestMatchers("/orderstatustype/**").hasRole("ADMIN")
                        .requestMatchers("/paymenttype/**").hasRole("ADMIN")

                        // ================== Usuarios ==================
                        .requestMatchers(HttpMethod.GET, "/restaurantuser/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/restaurantuser/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/restaurantuser/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/restaurantuser/**").hasRole("ADMIN")

                        // ================== MESAS ==================
                        // EMPLEADO: solo puede ver mesas (GET)
                        .requestMatchers(HttpMethod.GET, "/mesas/**").hasAnyRole("EMPLEADO", "ADMIN", "CLIENTE")
                        // ADMIN: puede crear, editar y eliminar mesas
                        .requestMatchers(HttpMethod.POST, "/mesas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/mesas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/mesas/**").hasRole("ADMIN")

                        // ========== PERMISOS DE EMPLEADO ==========
                        // EMPLEADO: Ver productos, actualizar productos, ver ventas, realizar ventas
                        .requestMatchers(HttpMethod.GET, "/api/menu/**")
                        .hasAnyRole("EMPLEADO", "ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/menu/**")
                        .hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/menu/**")
                        .hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/menu/**")
                        .hasRole("ADMIN")

                        // ========== PERMISOS DE CLIENTE ==========
                        // CLIENTE: Ver productos, realizar compras, ver sus compras
                        .requestMatchers(HttpMethod.GET, "/api/menu/**")
                        .hasAnyRole("CLIENTE", "EMPLEADO", "ADMIN")

                        // ========== REGLAS GENERALES PARA VENTAS ==========
                        // EMPLEADO y ADMIN pueden ver todas las ventas
                        .requestMatchers(HttpMethod.GET, "/api/sales/**")
                        .hasAnyRole("EMPLEADO", "ADMIN", "CLIENTE")
                        // EMPLEADO y ADMIN pueden crear ventas en otros sub-endpoints
                        .requestMatchers(HttpMethod.POST, "/api/sales/**")
                        .hasAnyRole("EMPLEADO", "ADMIN", "CLIENTE")
                        // Solo ADMIN puede actualizar ventas
                        .requestMatchers(HttpMethod.PUT, "/api/sales/**")
                        .hasAnyRole("ADMIN", "CLIENTE")
                        // Solo ADMIN puede eliminar ventas
                        .requestMatchers(HttpMethod.DELETE, "/api/sales/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(filterSecurityToken, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setPasswordEncoder(passwordEncoder());
        dao.setUserDetailsService(verifyUserDetailsService);
        return dao;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
