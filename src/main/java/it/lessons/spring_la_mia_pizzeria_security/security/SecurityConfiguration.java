package it.lessons.spring_la_mia_pizzeria_security.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
            .requestMatchers("/pizzeria/addPizza", "/pizzeria/modifica/**").hasAuthority("ADMIN")//solo gli Admin possono creare e modificare
            //.requestMatchers("/pizzeria/cancella").hasAnyAuthority("ADMIN")
            .requestMatchers(HttpMethod.POST, "/pizzeria/**").hasAnyAuthority("ADMIN")//tutte le post(che in questo caso compredono anche i delete) possono farli solo gli Admin
            .requestMatchers("/ingredienti", "/ingredienti/**").hasAnyAuthority("ADMIN")//gli ingredienti può aggiungerli solo l'Admin
            .requestMatchers("/pizzeria", "/pizzeria/**").hasAnyAuthority("USER", "ADMIN")//tutto quello che non è stato escluso in /pizzeria possono farloUser e Admin
            .requestMatchers("/**").permitAll()//tutto il resto  possono tutti (senza non è possibile effettuare nemmeno il login)
            .requestMatchers("/static/**").permitAll()
            .and().formLogin()
            .and().logout();

        return http.build();
    }

    @Bean
    DatabaseUserDetailsService userDetailsService(){
        return new DatabaseUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}