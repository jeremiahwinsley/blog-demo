package net.permutated.blog.repository;

import net.permutated.blog.entity.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    Optional<Author> findByUsername(String username);
}
