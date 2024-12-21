package com.security_app.config;


import com.security_app.exceptionhandling.CustomAccessDeniedHandler;
import com.security_app.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class ProjectSecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // Accept HTTPS ONLY
        // This is type of security will have problems in lower environment so we need to use profiles to hide this security inside lower environments like testing staging
        http
//            .requiresChannel(requestChannelConfig -> requestChannelConfig.anyRequest().requiresSecure())
//            The request will be forward to 8443
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/products").authenticated()
                        .requestMatchers("/myAccount").authenticated()
                );

        //Disable form login for rest api
        http.formLogin(AbstractHttpConfigurer::disable);

        http.httpBasic(httpBasicConfig -> httpBasicConfig.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));

        // The only difference between exceptionHandling and httpbasic is that handling in exception will be globally for any 401 or 403 happened
        // while in httpbasic it will be invoked only when authentication failed with 403 or 401 during basicauth proceess
        http.exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer.accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
