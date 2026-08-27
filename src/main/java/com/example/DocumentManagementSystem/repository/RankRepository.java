package com.example.DocumentManagementSystem.repository;

import com.example.DocumentManagementSystem.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RankRepository extends JpaRepository<Rank , Long> {

    Optional<Rank> findByRutbe(int rutbe);

}

