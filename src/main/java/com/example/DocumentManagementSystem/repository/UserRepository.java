package com.example.DocumentManagementSystem.repository;


import com.example.DocumentManagementSystem.entity.Department;
import com.example.DocumentManagementSystem.entity.Rank;
import com.example.DocumentManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {

    Optional<User> findByEmail(String email);

    List<User> findByDepartmentId(Long departmentId);

    Optional<User> findFirstByRankAndDepartment(Rank rank , Department department);




}
