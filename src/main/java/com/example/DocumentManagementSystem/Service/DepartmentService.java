package com.example.DocumentManagementSystem.Service;

import com.example.DocumentManagementSystem.entity.Department;
import com.example.DocumentManagementSystem.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Department creatDepartment(Department department){
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments(){
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id){
        return departmentRepository.findById(id)
                .orElseThrow(() ->new RuntimeException("Departman bulunamadı."));
    }
}
