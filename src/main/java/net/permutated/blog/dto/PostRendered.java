package net.permutated.blog.dto;

import net.permutated.blog.entity.Post;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.time.LocalDateTime;

public record PostRendered(Long id, String author, LocalDateTime date, String title, String content) {
    public static PostRendered fromPost(Post post) {

        Parser parser = Parser.builder().build();
        Node document = parser.parse(post.getBody());
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String rendered = renderer.render(document);

        String displayName = post.getAuthor().getDisplayName();

        return new PostRendered(post.getId(), displayName, post.getPublishDate(), post.getTitle(), rendered);
    }
}
