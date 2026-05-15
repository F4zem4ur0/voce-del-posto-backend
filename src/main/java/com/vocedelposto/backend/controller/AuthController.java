package com.vocedelposto.backend.controller;

import com.vocedelposto.backend.dto.AuthDTO;
import com.vocedelposto.backend.model.AppUser;
import com.vocedelposto.backend.repository.AppUserRepository;
import com.vocedelposto.backend.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthDTO> register(@RequestBody AuthDTO request) {
        if (appUserRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        AppUser user = new AppUser();
        user.setEmail(request.getEmail());
        user.setUsername(request.getEmail().split("@")[0]);
        user.setPasswordHash(request.getPassword()); // in chiaro per ora, cifreremo dopo
        AppUser saved = appUserRepository.save(user);
        String token = jwtService.generateToken(saved.getEmail());
        return ResponseEntity.ok(new AuthDTO(null, null, token, saved.getId(), saved.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDTO> login(@RequestBody AuthDTO request) {
        Optional<AppUser> userOpt = appUserRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        AppUser user = userOpt.get();
        if (!user.getPasswordHash().equals(request.getPassword())) {
            return ResponseEntity.status(401).build();
        }
        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthDTO(null, null, token, user.getId(), user.getUsername()));
    }
}