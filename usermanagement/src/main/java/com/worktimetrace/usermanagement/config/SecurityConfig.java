package com.worktimetrace.usermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.worktimetrace.usermanagement.service.UserDetailsServiceImpl;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private UserDetailsServiceImpl userService;

    public SecurityConfig(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /*
     * @Bean
     * AuthenticationManager authenticationManager(UserDetailsService
     * userDetailsService,
     * PasswordEncoder passwordEncoder) {
     * DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
     * provider.setUserDetailsService(userDetailsService);
     * provider.setPasswordEncoder(passwordEncoder);
     * return provider::authenticate;
     * }
     */

    /*
     * @Bean
     * public AuthenticationManager
     * authenticationManager(AuthenticationConfiguration authConfig) throws
     * Exception {
     * return authConfig.getAuthenticationManager();
     * }
     */

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * @Bean
     * SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     * http
     * .csrf(AbstractHttpConfigurer::disable)
     * .authorizeHttpRequests((authorize) -> authorize
     * .requestMatchers("/user/login", "/swagger-ui/*", "/user/register",
     * "/user/{username}").permitAll()
     * .anyRequest().authenticated())
     * .httpBasic(withDefaults())
     * .formLogin(withDefaults());
     * return http.build();
     * }
     */

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().permitAll())
                .httpBasic(withDefaults())
                .formLogin(withDefaults());
        return http.build();
    }

    /* @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    return http
        .cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
//            public endpoints
            .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
//            private endpoints
            .anyRequest().authenticated())
        .authenticationManager(authenticationManager)
        .build();
  } */
}