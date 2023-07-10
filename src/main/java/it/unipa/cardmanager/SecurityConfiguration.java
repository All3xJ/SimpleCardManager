package it.unipa.cardmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserDetailsService userDetailsService;  // attributo che verrà inizializzato automaticamente con la nostra classe CustomUserDetailsService che implementa UserDetailsService appunto. sta classe permette di associare l'utente a un ruolo.

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/").permitAll()   // permitAll permette anche a chi non è loggato
                                .requestMatchers("/home").permitAll()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/search").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/cardowner/**").hasAnyRole("CARDOWNER","MERCHANT","ADMIN")
                                .requestMatchers("/merchant/**").hasAnyRole("MERCHANT","ADMIN")
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()   // authenticated fa si che basta che l'utente sia loggato per accedervi
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/success") // dove deve portarmi a login fatto
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)     // diamo il riferimento della classe che alla fine corrisponderà alla nostra CustomUserDetailsService. permette di associare l'utente a un ruolo.
            .passwordEncoder(passwordEncoder());
    }

}