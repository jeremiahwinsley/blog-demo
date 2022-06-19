package net.permutated.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import net.permutated.blog.entity.Post;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostCreate {
    private Long id;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime publishDate;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    @NotBlank
    private String excerpt;

    public static PostCreate fromPost(Post post) {
        var dto = new PostCreate();
        dto.setId(post.getId());
        dto.setPublishDate(post.getPublishDate());
        dto.setTitle(post.getTitle());
        dto.setBody(post.getBody());
        dto.setExcerpt(post.getExcerpt());
        return dto;
    }
}
