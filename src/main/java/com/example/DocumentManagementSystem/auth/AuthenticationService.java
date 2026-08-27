package com.example.DocumentManagementSystem.auth;

import com.example.DocumentManagementSystem.entity.Department;
import com.example.DocumentManagementSystem.entity.Rank;
import com.example.DocumentManagementSystem.entity.User;
import com.example.DocumentManagementSystem.repository.UserRepository;
import com.example.DocumentManagementSystem.repository.DepartmentRepository;
import com.example.DocumentManagementSystem.repository.RankRepository;
import com.example.DocumentManagementSystem.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final DepartmentRepository departmentRepository;
    private final RankRepository rankRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository,DepartmentRepository departmentRepository,RankRepository rankRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.departmentRepository = departmentRepository;
        this.rankRepository = rankRepository;
    }

   public AuthenticationResponse register(RegisterRequest request) {

       User user = new User();
       user.setName(request.getName());
       user.setEmail(request.getEmail());
       user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (request.getDepartmentId() != null) {
           Department department = departmentRepository.findById(request.getDepartmentId())
                   .orElseThrow(() -> new RuntimeException("Departman bulunamadı"));
           user.setDepartment(department);
       }

       if (request.getRankId() != null) {
           Rank rank = rankRepository.findById(request.getRankId())
                   .orElseThrow(() -> new RuntimeException("Rütbe bulunamadı"));
           user.setRank(rank);
       }

       repository.save(user);
       var jwtToken = jwtService.generateToken(user);

       return new AuthenticationResponse(jwtToken);
    }

     public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow();

         String jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }
}