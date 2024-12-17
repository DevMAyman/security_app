package com.security_app.service;

import com.security_app.repository.UserRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import com.security_app.entity.CustomUser;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser customUser = userRepo.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User details not found for the user"+username));
        List<GrantedAuthority> authorityList = List.of(new SimpleGrantedAuthority(customUser.getRole()));

        return new User(customUser.getEmail(),customUser.getPassword(),authorityList);
    }
}
