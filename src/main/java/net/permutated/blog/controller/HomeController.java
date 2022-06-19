package net.permutated.blog.controller;

import net.permutated.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    PostService postService;

    @Autowired
    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public ModelAndView home(@RequestParam(value = "p", defaultValue = "0") int page) {
        ModelAndView mav = new ModelAndView("public/home");
        mav.addObject("posts", postService.getNewestPosts(page));

        // parameter is 0-index, frontend is 1-index
        mav.addObject("previousPage", Math.max(0, page - 1));
        mav.addObject("currentPage", Math.max(0, page));
        mav.addObject("nextPage", Math.max(0, page + 1));

        return mav;
    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam(value = "q", defaultValue = "") String query,
                         @RequestParam(value = "p", defaultValue = "0") int page) {
        ModelAndView mav = new ModelAndView("public/search");
        mav.addObject("posts", postService.searchPosts(query, page));

        // parameter is 0-index, frontend is 1-index
        mav.addObject("previousPage", Math.max(0, page - 1));
        mav.addObject("currentPage", Math.max(0, page));
        mav.addObject("nextPage", Math.max(0, page + 1));
        return mav;
    }

    @GetMapping("/p/{id}")
    public ModelAndView viewPost(@PathVariable Long id) {
        var post = postService.getRendered(id);
        if (post.isPresent()) {
            var mav = new ModelAndView("public/view");
            mav.addObject("post", post.get());
            return mav;
        } else {
            return new ModelAndView("public/notFound");
        }
    }
}
