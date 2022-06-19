package net.permutated.blog.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import net.permutated.blog.dto.PostCreate;
import net.permutated.blog.entity.Author;
import net.permutated.blog.entity.Post;
import net.permutated.blog.service.AuthorService;
import net.permutated.blog.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class AdminController {
    private final PostService postService;
    private final AuthorService authorService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(PostService postService, AuthorService authorService) {
        this.postService = postService;
        this.authorService = authorService;
    }

    @GetMapping("/admin/post/list")
    public ModelAndView home(@RequestParam(value = "p", defaultValue = "0") int page) {
        ModelAndView mav = new ModelAndView("admin/post/list");
        // should be paginated eventually
        mav.addObject("posts", postService.getAllPosts());
        return mav;
    }


    @GetMapping("/admin/post/create")
    public ModelAndView createPost() {
        return new ModelAndView("admin/post/create", "post", new PostCreate());
    }

    @GetMapping("/admin/post/edit/{id}")
    public ModelAndView editPost(@PathVariable Long id) {
        Optional<Post> optional = postService.getPost(id);
        if (optional.isPresent()) {
            PostCreate dto = PostCreate.fromPost(optional.get());
            return new ModelAndView("admin/post/edit", "post", dto);
        } else {
            return new ModelAndView("admin/notFound");
        }
    }

    @PostMapping("/admin/post/create")
    public ModelAndView savePost(@Valid PostCreate post, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("admin/post/create", "post", post);
        }

        Optional<Author> optional = authorService.getCurrentUser();
        if (optional.isPresent()) {
            try {
                postService.createOrUpdate(post, optional.get());
            } catch (ValidationException e) {
                result.addError(new ObjectError("body", e.getMessage()));
                return new ModelAndView("admin/post/create", "post", post);
            }

            return new ModelAndView("redirect:/admin/post/list");
        } else {
            LOGGER.error("accessed postCreate with no matching Author");
            return new ModelAndView("admin/error");
        }
    }

    @GetMapping("/admin/post/delete/{id}")
    public ModelAndView confirmDelete(@PathVariable Long id) {
        Optional<Post> optional = postService.getPost(id);
        if (optional.isPresent()) {
            PostCreate dto = PostCreate.fromPost(optional.get());
            return new ModelAndView("admin/post/confirm", "post", dto);
        } else {
            return new ModelAndView("admin/notFound");
        }
    }

    @PostMapping("/admin/post/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/admin/post/list";
    }

}
