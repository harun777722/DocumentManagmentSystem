package com.example.DocumentManagementSystem.repository;

import com.example.DocumentManagementSystem.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Departmanların CRUD (creat,read,upload,delete) işlemlerini yapacaz
@Repository
public interface DepartmentRepository extends  JpaRepository<Department , Long>{
}
