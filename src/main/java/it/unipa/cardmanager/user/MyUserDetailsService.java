package it.unipa.cardmanager.user;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {   // classe che serve a Spring per associare il ruolo all'user che si logga.

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);    // entity presa dal db

        if (user == null){  // se utente non esiste
            throw new BadCredentialsException("Invalid username or password.");
        } else if (!user.isEnabled()) {     // controllo se utente è disabilitato
            throw new DisabledException("User not enabled");
        } else {
            return new org.springframework.security.core.userdetails.User(user.getUsername(),   // salvo username nel context spring
                                                                            user.getPassword(),
                                                                            mapRolesToAuthorities(user.getRoles()));    // associo anche i ruoli (nella nostra app un utente può avere solo un ruolo) alle spring authorities
        }
    }

    private Collection < ? extends GrantedAuthority> mapRolesToAuthorities(Collection <Role> roles) {
        Collection < ? extends GrantedAuthority> mapRoles = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))    // associo Role a una authority spring tramite la stringa del nome del ruolo
                .collect(Collectors.toList());
        return mapRoles;
    }
}