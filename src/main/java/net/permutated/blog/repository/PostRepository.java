package net.permutated.blog.repository;

import net.permutated.blog.dto.PostExcerpt;
import net.permutated.blog.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {
    @Query("select new net.permutated.blog.dto.PostExcerpt(p.id, p.author.displayName, p.publishDate, p.title, p.excerpt) from Post p order by p.createdDate desc")
    List<PostExcerpt> listByNewest(Pageable pageable);
    @Query("select new net.permutated.blog.dto.PostExcerpt(p.id, p.author.displayName, p.publishDate, p.title, p.excerpt) from Post p order by p.createdDate desc")
    List<PostExcerpt> listByNewest();

    // this could be implemented better, using something like Postgres full text search or Elasticsearch,
    // but this works fine for an example
    @Query("select new net.permutated.blog.dto.PostExcerpt(p.id, p.author.displayName, p.publishDate, p.title, p.excerpt) from Post p where lower(p.title) like %:title% order by p.createdDate desc")
    List<PostExcerpt> searchByTitle(String title, Pageable pageable);
}
