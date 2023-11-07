package com.vention.stock_market_share.config;

import com.vention.stock_market_share.filter.JwtAuthenticationFilter;
import com.vention.stock_market_share.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.vention.stock_market_share.enums.Permission.*;
import static com.vention.stock_market_share.enums.Role.ADMIN;
import static com.vention.stock_market_share.enums.Role.USER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/register", "/authenticate", "/refresh", "/create/**").permitAll()
                        .requestMatchers("/security/**").hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(GET, "/security/**").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
                        .requestMatchers(DELETE, "/security/**").hasAnyAuthority(ADMIN_DELETE.name(), USER_UPDATE_BY_ID.name())
                        .requestMatchers(PUT, "/security/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE_BY_ID.name())
                        .requestMatchers(POST, "/security/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers("/stocks/**").hasAnyRole(ADMIN_READ.name(), USER_READ.name())
                        .requestMatchers(GET, "/stocks/**").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
                        .requestMatchers("/stocks/**").hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(GET, "/stocks/**").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
                        .requestMatchers(POST, "/stocks/**").hasAnyAuthority(ADMIN_CREATE.name(), USER_CREATE.name())
                        .anyRequest()
                        .authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            try {
                return userRepository.findByUsername(username);
            } catch (Exception e) {
                throw new UsernameNotFoundException("User is not found");
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
