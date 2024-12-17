package com.security_app.provider;

import com.security_app.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    // Autowired custom service you make it to load user from your custom schema `You did that as you did not want use default schema of user that spring security make it`
    // It is highly recommended not to load user details inside your provider instead use your user details service that already do so
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            String username =  authentication.getName();
            String password = authentication.getCredentials().toString();
            // If there is no username in db with the same our service will throw this error
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if(passwordEncoder.matches(password,userDetails.getPassword())){
                // If you need add more custom validation make it here
                return new UsernamePasswordAuthenticationToken(username,password,userDetails.getAuthorities());
            }else {
                throw new BadCredentialsException("Invalid credentials");
            }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // Take it from Dao as i use same type of authentication object `UsernamePasswordAuthenticationToken`
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
