package com.example.spring.security.controller;

import com.example.spring.security.dao.UserRepository;
import com.example.spring.security.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping({" ","/"})
    public String main(@AuthenticationPrincipal UserDetails userDetails){
        if(userDetails != null){
            System.out.println(userDetails.getUsername());
        }
        return "index";
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/user_info_data")
    public @ResponseBody String user_info_data() {
        return "민감한 정보";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user(){
        return "user";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager(){
        return "manager";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin(){
        return "admin";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "/loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "/joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        System.out.println("user");
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/loginForm";
    }
}
