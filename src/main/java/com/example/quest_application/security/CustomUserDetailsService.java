package com.example.quest_application.security;

import com.example.quest_application.entity.User;
import com.example.quest_application.repos.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Kullanıcıyı veri tabanından al
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username: " + username));

        // Kullanıcı rollerini GrantedAuthority'e dönüştür
        Collection<GrantedAuthority> authorities = getAuthorities(user.getRoles());

        // Spring Security'nin UserDetails nesnesini döndür
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    // Kullanıcı rollerini SimpleGrantedAuthority'lere dönüştür
    private Collection<GrantedAuthority> getAuthorities(Set<String> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // ROLE_ prefix ekleniyor
                .collect(Collectors.toSet());
    }
}
