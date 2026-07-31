package com.iva.task_manager.controller;

import com.iva.task_manager.config.JwtService;
import com.iva.task_manager.model.LoginRequest;
import com.iva.task_manager.model.User;
import com.iva.task_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User doesn't exists"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        return jwtService.generateToken(user.getUsername());
    }

    @PostMapping("/register")
    public String register(@RequestBody User newUser) {
        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);

        return jwtService.generateToken(newUser.getUsername());
    }
}
/*nadje korisnika po username-u u bazi,
* passwordEncoder.matches(plainPassword, encodedPassword) - uzme lozinku koju je
* korisnik upravo ukucao (cist tekst), enkriptuje je na isti nacin i uporedi sa onom sacuvanom
* enkriptovaom verzijom u bazi,
* ako je sve tacno generise i vrati token preko JwtService-a koji sam napravila
* postMapping register - provera da li username postoji*/