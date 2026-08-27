package com.example.DocumentManagementSystem.controller;

import com.example.DocumentManagementSystem.entity.Department;
import com.example.DocumentManagementSystem.Service.DepartmentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<Department> creatDepartment(@RequestBody Department department){
        return ResponseEntity.ok(departmentService.creatDepartment(department));
    }

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments(){
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable("id") Long id){
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }
}
