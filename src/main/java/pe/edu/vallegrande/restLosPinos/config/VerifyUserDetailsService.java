package pe.edu.vallegrande.restLosPinos.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.restLosPinos.repository.RestaurantUserRepository;

@Service
@RequiredArgsConstructor
public class VerifyUserDetailsService implements UserDetailsService {

    private final RestaurantUserRepository repository;
    private final EncryptionUtil encryptionUtil;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String encryptedUsername = encryptionUtil.encrypt(username);
        return repository.findByUserNameWithRole(encryptedUsername)
                .or(() -> repository.findByUserNameWithRole(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
