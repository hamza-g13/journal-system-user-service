package se.kth.journalsystem.auth.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Importera denna
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.kth.journalsystem.auth.model.User;
import se.kth.journalsystem.auth.repository.UserRepository;

import java.util.Collections; // Importera denna

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        try {
            Long id = Long.parseLong(userId);
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

            // HÄR SKER MAGIN: Konvertera Role enum till Authority
            // Spring Security vill ofta ha prefixet "ROLE_" för hasRole()-metoden
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().toString());

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.singletonList(authority) // Skicka med listan av authorities
            );
        } catch (NumberFormatException e) {
            // Fallback för username om det behövs
            User user = userRepository.findByUsername(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userId));

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().toString());

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.singletonList(authority));
        }
    }
}
