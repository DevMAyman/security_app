package com.security_app.config;


import com.security_app.exceptionhandling.CustomAccessDeniedHandler;
import com.security_app.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.security_app.filter.JWTTokenGeneratorFilter;
import com.security_app.filter.JWTTokenValidatorFilter;
import com.security_app.provideer.CustomUsernamePasswordProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
public class ProjectSecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // Accept HTTPS ONLY
        // This is type of security will have problems in lower environment so we need to use profiles to hide this security inside lower environments like testing staging
        http
//            .requiresChannel(requestChannelConfig -> requestChannelConfig.anyRequest().requiresSecure())
//            The request will be forward to 8443
                // By default spring security using session
//                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
//                        .requestMatchers(HttpMethod.GET, "/products").hasAuthority("VIEWPRODUCTS")
//                        .requestMatchers(HttpMethod.POST, "/products").hasAuthority("ADDPRODUCT")
//                        .requestMatchers(HttpMethod.DELETE, "/products").hasAuthority("DELETEPRODUCT")
                                .requestMatchers(HttpMethod.GET, "/products").hasAnyRole("USER","ADMIN")
                                .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/products").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/apiLogin").permitAll()
                                .requestMatchers(HttpMethod.GET, "/ifUserHasAtLeastOneAuthority").hasAnyAuthority("DELETEPRODUCT", "ADDPRODUCT")
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


    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        CustomUsernamePasswordProvider authenticationProvider =
                new CustomUsernamePasswordProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return  providerManager;
    }

}
