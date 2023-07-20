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
    private UserDetailsService userDetailsService;  // attributo che verrà inizializzato automaticamente con la nostra classe CustomUserDetailsService che implementa UserDetailsService appunto. questa classe permette di associare l'utente a un ruolo.

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
                ).exceptionHandling(h -> h
                                .accessDeniedPage("/access_denied")
                ).formLogin(    // già se facciamo cosi stiamo preimpostando un meccanismo di spring che di default preleva alla url di login nella req post i parametri che di default sono "username" e "password" (e infatti in login thymeleaf vengono usati id username e id password!)
                        form -> form
                                .loginPage("/login")    // inoltre loginPage() insieme ad aver messo @EnableWebSecurity fa si che venga fatto redirect se l'url che si vuole visitare RICHIEDE di essere autenticato
                                .failureUrl("/login_error") // fa redirect a questo url se c'è problema nel login, e verrà gestito dall'apposito metodo del controller
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/success") // dove deve portarmi a login avvenuto con successo
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // associa l'azione di logout alla url /logout
                                .permitAll()
                );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)     // diamo il riferimento della classe che alla fine corrisponderà alla nostra CustomUserDetailsService. permette di associare l'utente a un ruolo e fornisce il metodo loadUserByUsername(di cui abbiamo fatto override) che cerca effettivamente user dal db per capire se login può avere successo, e se ha trovato utente fa return di oggetto UserDetails che contiene il username e ruolo dell'utente. se non va a buon fine invece fa il throw di un eccezione apposita
            .passwordEncoder(passwordEncoder());
    }

}