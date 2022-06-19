package net.permutated.blog.service;

import jakarta.validation.ValidationException;
import net.permutated.blog.dto.PostCreate;
import net.permutated.blog.dto.PostExcerpt;
import net.permutated.blog.dto.PostRendered;
import net.permutated.blog.entity.Author;
import net.permutated.blog.entity.Post;
import net.permutated.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository repository;

    @Autowired
    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<PostExcerpt> getNewestPosts(int page) {
        var pageable = PageRequest.of(page, 10);
        return repository.listByNewest(pageable);
    }

    public Iterable<PostExcerpt> getAllPosts() {
        return repository.listByNewest();
    }

    public List<PostExcerpt> searchPosts(String query, int page) {
        var pageable = PageRequest.of(page, 10);
        return repository.searchByTitle(query.toLowerCase(Locale.ROOT), pageable);
    }

    public Optional<Post> getPost(Long id) {
        return repository.findById(id);
    }

    public Optional<PostRendered> getRendered(Long id) {
        return repository.findById(id).map(PostRendered::fromPost);
    }

    @Transactional
    public Post createOrUpdate(PostCreate dto, Author author) {
        Post post;
        if (dto.getId() == null) {
            post = new Post();
            post.setAuthor(author);
            post.setPublishDate(dto.getPublishDate());
        } else {
            post = repository.findById(dto.getId())
                .orElseThrow(() -> new ValidationException("Update ID provided but does not exist"));
        }

        post.setBody(dto.getBody());
        post.setTitle(dto.getTitle());
        post.setExcerpt(dto.getExcerpt());
        return repository.save(post);
    }

    @Transactional
    public void deletePost(Long id) {
        repository.deleteById(id);
    }
}
