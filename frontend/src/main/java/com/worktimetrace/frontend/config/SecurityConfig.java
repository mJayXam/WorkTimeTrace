package com.worktimetrace.frontend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authConfig -> {
                    authConfig.requestMatchers(HttpMethod.GET, "/").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET, "/login").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET, "/registration").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET, "/css/**").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET, "/images/**").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET, "/kalender").hasRole("USER");
                    authConfig.requestMatchers(HttpMethod.GET, "/monatsuebersicht").hasRole("USER");
                    authConfig.requestMatchers(HttpMethod.GET, "/logout").hasRole("USER");
                    authConfig.anyRequest().authenticated();
                })
                .formLogin(login -> login
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/kalender?month=0")
                        .failureUrl("/login?error=true"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true));
        
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.builder()
                .username("username")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}