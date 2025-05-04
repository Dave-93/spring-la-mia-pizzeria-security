package it.lessons.spring_la_mia_pizzeria_security.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.lessons.spring_la_mia_pizzeria_security.model.User;
import it.lessons.spring_la_mia_pizzeria_security.repository.UserRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optUser = userRepository.findByUsername(username);
        if(optUser.isPresent()){
            return new DatabaseUserDetails(optUser.get());
        }else{
            throw new UsernameNotFoundException("Username not found");
        }
    }
}