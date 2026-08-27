package com.example.DocumentManagementSystem.Service;

import com.example.DocumentManagementSystem.entity.Department;
import com.example.DocumentManagementSystem.entity.Rank;
import com.example.DocumentManagementSystem.entity.User;
import com.example.DocumentManagementSystem.repository.DepartmentRepository;
import com.example.DocumentManagementSystem.repository.RankRepository;
import com.example.DocumentManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RankRepository rankRepository;

    public User creatUser ( User user) {

        if (user.getDepartment() != null && user.getDepartment().getId() != null) {
            Department gercekDepartment = departmentRepository.findById(user.getDepartment().getId())
                    .orElseThrow(() -> new RuntimeException("Departman bulunamadı!"));
            user.setDepartment(gercekDepartment);
        }

        // 2. Rank ID'si geldiyse, veritabanından dolu objeyi çek ve set et
        if (user.getRank() != null && user.getRank().getId() != null) {
            Rank gercekRank = rankRepository.findById(user.getRank().getId())
                    .orElseThrow(() -> new RuntimeException("Rütbe bulunamadı!"));
            user.setRank(gercekRank);
        }
        return userRepository.save(user);
    }

    public List<User > getAllUsers(){
        return userRepository.findAll();
    }
    public User getUserById (Long id){
        return userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Kullanıcı bulunamadı."));
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(Long id, User userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());

        // --- FAZ 2: DEPARTMAN VE RÜTBE ATAMASI ---
        if (userDetails.getDepartment() != null) {
            existingUser.setDepartment(userDetails.getDepartment());
        }
        if (userDetails.getRank() != null) {
            existingUser.setRank(userDetails.getRank());
        }

        // Not: Şifre güncelleme işlemi güvenlik gereği genelde ayrı bir metotta yapılır,
        // bu yüzden buraya şifre değişimini dahil etmiyoruz.

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
