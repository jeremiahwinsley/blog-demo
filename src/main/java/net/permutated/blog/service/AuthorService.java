package net.permutated.blog.service;

import net.permutated.blog.config.Role;
import net.permutated.blog.entity.Author;
import net.permutated.blog.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {
    public final AuthorRepository repository;

    @Autowired
    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public Optional<Author> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            return repository.findByUsername(username);
        }
        return Optional.empty();
    }

    @EventListener
    public void updateOnLogin(InteractiveAuthenticationSuccessEvent event) {
        if (event.getAuthentication().getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            // update Author if one exists, else create new Author
            var author = repository.findByUsername(username).orElseGet(Author::new);
            Role role = userDetails.getAuthorities().stream()
                .filter(Role.class::isInstance)
                .map(Role.class::cast)
                .findFirst()
                .orElse(Role.USER);

            author.setUsername(userDetails.getUsername());
            author.setRole(role);

            if (author.getDisplayName() == null) {
                author.setDisplayName(userDetails.getUsername());
            }

            repository.save(author);
        }
    }
}
