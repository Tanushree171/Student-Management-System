package com.university.sms.config;

import com.university.sms.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(fo -> fo.disable())) // for H2 console
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/", "/login", "/api/auth/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/favicon.ico").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**").permitAll()

                // Student endpoints
                .requestMatchers("/student/**", "/api/student/**").hasAuthority("ROLE_STUDENT")

                // Admin endpoints with role-based access
                .requestMatchers("/admin/users/**", "/admin/settings/**", "/api/admin/users/**", "/api/admin/settings/**")
                    .hasAuthority("ROLE_SUPER_ADMIN")

                .requestMatchers("/admin/fees/**", "/api/admin/fees/**",
                                 "/admin/students/**", "/api/admin/students/**",
                                 "/admin/announcements/**", "/api/admin/announcements/**")
                    .hasAnyAuthority("ROLE_SUPER_ADMIN", "ROLE_REGISTRAR")

                .requestMatchers("/admin/attendance/**", "/api/admin/attendance/**",
                                 "/admin/results/**", "/api/admin/results/**",
                                 "/admin/courses/**", "/api/admin/courses/**")
                    .hasAnyAuthority("ROLE_SUPER_ADMIN", "ROLE_HOD", "ROLE_FACULTY")

                .requestMatchers("/admin/**", "/api/admin/**")
                    .hasAnyAuthority("ROLE_SUPER_ADMIN", "ROLE_HOD", "ROLE_FACULTY", "ROLE_REGISTRAR")

                // Shared endpoints
                .requestMatchers("/api/announcements/**", "/api/notifications/**").authenticated()

                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
