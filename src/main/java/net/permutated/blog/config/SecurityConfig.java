package net.permutated.blog.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.headers().contentSecurityPolicy("default-src 'self'");

        http.authorizeHttpRequests(auth -> auth
                .antMatchers("/css/**", "/js/**", "/vendor/**", "/favicon.ico").permitAll()
                .antMatchers("/actuator/**").hasRole(Role.ADMIN.name())
                .antMatchers("/admin/user/**").hasRole(Role.ADMIN.name())
                .antMatchers("/admin/**").hasAnyRole(Role.ADMIN.name(), Role.AUTHOR.name())
                .antMatchers("/", "/search", "/p/{id}", "/error").permitAll()
                .anyRequest().denyAll()
            ).formLogin()
            .loginPage("/login").permitAll();

        return http.build();
    }

    @Bean
    public AuditorAware<UUID> auditorAware() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails user) {
                // ideally this would be a UUID primary key,
                // but substituting with a hash-based UUID due to in memory user storage.
                var bytes = user.getUsername().getBytes(StandardCharsets.UTF_8);
                var uuid = UUID.nameUUIDFromBytes(bytes);
                return Optional.of(uuid);
            } else {
                return Optional.empty();
            }
        };
    }

    @Bean
    public UserDetailsManager userDetailsManager() {
        UserDetails adminUser = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("password")
            .authorities(Role.ADMIN)
            .build();

        UserDetails authorUser = User.withDefaultPasswordEncoder()
            .username("author")
            .password("password")
            .authorities(Role.AUTHOR)
            .build();

        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(adminUser);
        manager.createUser(authorUser);
        return manager;
    }
}
