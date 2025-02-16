package com.gymmanager.config;

import com.gymmanager.security.exception.AuthEntryPointJwt;
import com.gymmanager.security.jwt.JwtRequestFilter;
import com.gymmanager.security.services.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
public class SecurityConfig {

    private final AuthEntryPointJwt unauthorizedHandler;
    private final JwtRequestFilter jwtRequestFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(unauthorizedHandler)
            )
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/admin/**","/api/statistics/**").hasRole("ADMIN")
            .requestMatchers("/api/auth/signup").hasRole("ADMIN")
            .requestMatchers("/api/staff/**").hasAnyRole("ADMIN", "STAFF")
            .requestMatchers(HttpMethod.DELETE, "/api/admin/users/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/admin/users").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/api/users/**").hasAnyRole("ADMIN", "STAFF")
            .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
            .requestMatchers(HttpMethod.GET, "/api/customers/**").hasAnyRole("ADMIN", "STAFF")
            .requestMatchers(HttpMethod.POST, "/api/customers").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/customers/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/packs").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/packs").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/packs/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/packs/**").hasRole("ADMIN")
            
                .requestMatchers(
                    "/api/auth/signin",
                    "/api/customer/auth/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**"
                ).permitAll()
                .requestMatchers("/api/staff/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return new ProviderManager(
            userAuthenticationProvider()
        );
    }

    @Bean
    public AuthenticationProvider userAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    
}
