package com.vocedelposto.backend.controller;

import com.vocedelposto.backend.model.AppUser;
import com.vocedelposto.backend.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.vocedelposto.backend.model.Tag;
import java.util.Set;
import com.vocedelposto.backend.model.Tag;

@RestController
@RequestMapping("/users")
public class AppUserController {

    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping
    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    @PostMapping
    public AppUser createUser(@RequestBody AppUser user) {
        return appUserRepository.save(user);
    }
    @PutMapping("/{id}/tags")
    public AppUser updateTags(@PathVariable Long id, @RequestBody Set<Tag> tags) {
        AppUser user = appUserRepository.findById(id).orElseThrow();
        user.setPreferredTags(tags);
        return appUserRepository.save(user);
    }

    @GetMapping("/{id}/tags")
    public Set<Tag> getUserTags(@PathVariable Long id) {
        AppUser user = appUserRepository.findById(id).orElseThrow();
        return user.getPreferredTags();
    }
}