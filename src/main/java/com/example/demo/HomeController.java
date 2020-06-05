package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PostRepository postRepository;

    @RequestMapping("/secure")
    public String secure(Principal principal, Model model){
        String username = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
        model.addAttribute("post", postRepository.findAll());

        return "secure";
    }

    @RequestMapping("/")
    public String index(Principal principal, Model model){
        String username = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
        model.addAttribute("post", postRepository.findAll());
        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(){
        return "redirect:/login?logout=true";
    }

    @RequestMapping("/admin")
    public String admin(Principal principal, Model model){
        String username = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
        return "admin";
    }

    @GetMapping("/register")
    public String register(Model model, Principal principal){
        String username = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("user")User user, BindingResult result, Model model, Principal principal){
        model.addAttribute("user", user);
        String username = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
        if(result.hasErrors()){
            return "register";
        } else {
            model.addAttribute("message", "User Account Created");
            user.setEnabled(true);
            Role role = new Role(user.getUsername(), "ROLE_USER");
            Set<Role> roles = new HashSet<Role>();
            roles.add(role);

            roleRepository.save(role);
            userRepository.save(user);
        }
        return "index";
    }



    Post posts = new Post();


    @GetMapping("/post")
    public String post(Model model, Principal principal){
        String username = principal.getName();
        model.addAttribute("post", userRepository.findByUsername(username));
        model.addAttribute("post", new Post());
        return "message";
    }

    @PostMapping("/post")
    public String post(@Valid @ModelAttribute("post")Post post, User user, BindingResult result, Model model, Principal principal){
        model.addAttribute("post", post);
        String username = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
        if(result.hasErrors()){
            return "message";
        } else {
            model.addAttribute("message", "Status Updated");
            Role role = new Role(user.getUsername(), "ROLE_USER");
            ArrayList<Post> posts = new ArrayList<Post>();
            Set<Post> message = new HashSet<Post>();
            posts.add(post);

            roleRepository.save(role);
            userRepository.save(user);
            postRepository.save(post);
        }
        return "index";
    }












    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") long id, Model model){
        model.addAttribute("task", postRepository.findById(id).get());
        return "message";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model){
        model.addAttribute("task", postRepository.findById(id).get());
        postRepository.deleteById(id);
        return "redirect:/";
    }
}
