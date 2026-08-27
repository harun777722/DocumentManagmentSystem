package com.example.DocumentManagementSystem.controller;

import com.example.DocumentManagementSystem.entity.User;
import com.example.DocumentManagementSystem.Service.UserService;
import com.example.DocumentManagementSystem.repository.UserRepository; // Bunu import etmemiz şart
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository; // Repository'yi buraya ekledik
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Kilit Nokta: Şifreyi şifreliyoruz
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userService.creatUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User userDetails) {
        // Not: Asıl iş mantığını (veritabanından bul, departmanı set et, kaydet)
        // UserService içindeki updateUser metodunda yapmalıyız.
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    // Kullanıcı Silme
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}