package com.pi.department_service.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.pi.department_service.model.Department;

@Repository
public class DepartmentRepository {
  
    private List<Department> department = new ArrayList<>();
    public Department addDepartment(Department newdepartment){
        department.add(newdepartment);
        return newdepartment;
    }
    public Department findById(Long id){
        return department.stream().filter(department -> department.getId().equals(id))
        .findFirst()
        .orElseThrow();
    }
    public List <Department> findAll(){
        return department;
    }
}
