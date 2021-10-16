package com.example.usersalbums.controllers;

import com.example.usersalbums.models.DTO.PostDTO;
import com.example.usersalbums.models.Posts;
import com.example.usersalbums.models.Users;
import com.example.usersalbums.repositories.PostRepository;
import com.example.usersalbums.repositories.UsersRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("/user")
public class MainController {

    private final PostRepository postRepository;
    private final UsersRepository usersRepository;

    public MainController(PostRepository postRepository, UsersRepository usersRepository){
        this.postRepository=postRepository;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/sign_up")
    public String sign_up(){

        return "sign_up";
    }
    @PostMapping("/sign_up")
    public RedirectView sign_up(@ModelAttribute Users users){
        users.setPassword(BCrypt.hashpw(users.getPassword(), BCrypt.gensalt()));
        usersRepository.save(users);
        return new RedirectView("sign_in");
    }

    @GetMapping("/sign_in")
    public String sign_in(){

        return "sign_in";
    }
    @PostMapping("/sign_in")
    public RedirectView signInUser(HttpServletRequest request, @ModelAttribute Users appUser, RedirectAttributes attributes) {
        Users foundUser = usersRepository.findUsersByUsername(appUser.getUsername()).orElseThrow();
        String savedUserPassword = foundUser.getPassword();

        if (BCrypt.checkpw(appUser.getPassword(), savedUserPassword)) { // successfully sign in
            // store session data - in this case the username
            HttpSession session = request.getSession();
            session.setAttribute("username", foundUser.getUsername());

            return new RedirectView("posts");
        } else {
            RedirectView redirectView = new RedirectView("failedSignIn", true);
//            attributes.addAttribute("username", foundUser.getUsername());
            return redirectView;
        }
    }
    @GetMapping("/failedSignIn")
    public String getSignInErrorPage() {
        return "faildSignIn";
    }

    @GetMapping("/posts")
    public String getBlogPosts(Model model, HttpServletRequest request) {
        model.addAttribute("posts", postRepository.findAll());

        String username = (String) request.getSession().getAttribute("username");

        model.addAttribute("username", username);
        return "posts";
    }

    @PostMapping("/posts")
    public RedirectView createNewBlogPost(@ModelAttribute PostDTO postDTO) {
        Users author = usersRepository.findUsersByUsername(postDTO.getUser()).orElseThrow();
        Posts newPost = new Posts(author, postDTO.getContent());
        postRepository.save(newPost);

        return new RedirectView("posts");
    }



    @GetMapping("/posts/{username}")
    public String findPostByUsername(@PathVariable String username, Model model) {
        List<Posts> posts = postRepository.findAllByUser_Username(username);
        model.addAttribute("authorPost", posts);

        return "post";
    }

    @GetMapping("/posts/{postId}")
    public String findPostByPostId(@PathVariable String postId, Model model) {
        Posts post = postRepository.findById(Long.parseLong(postId)).orElseThrow();
        model.addAttribute("authorPost", post);

        return "post";
    }
    @Transactional
    @GetMapping("/delete/{id}")
    public RedirectView deleteUserPost(@PathVariable String id, HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        postRepository.deletePostsByUser_UsernameAndId(username, Long.parseLong(id));
        return new RedirectView("posts");
    }

}
