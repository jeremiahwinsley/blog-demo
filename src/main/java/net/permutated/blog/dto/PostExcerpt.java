package net.permutated.blog.dto;

import java.time.LocalDateTime;

public record PostExcerpt(Long id, String author, LocalDateTime date, String title, String excerpt) {
}
