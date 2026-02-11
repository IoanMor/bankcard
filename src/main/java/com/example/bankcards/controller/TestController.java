package com.example.bankcards.controller;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/test")
public class TestController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public TestController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/s")
    public String secure (){
        return "secure";
    }

    @GetMapping("/create")
    public String crateTestUser(){
        Role role = new Role();
        Role role1 = new Role();
        role1.setName("USER");
        role.setName("ADMIN");
        roleRepository.save(role);
        roleRepository.save(role1);
        User user = User.builder().username("Bob").password(passwordEncoder.encode("1")).isActive(true).roles(Set.of(role)).build();
        User user1 = User.builder().username("Vil").password(passwordEncoder.encode("1")).isActive(true).roles(Set.of(role1)).build();
        userRepository.save(user);
        userRepository.save(user1);
        return "User created";
    }

    @GetMapping("/p")
    public String publicALL(){
        return "public";
    }
}
