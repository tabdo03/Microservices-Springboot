package com.pi.department_service.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pi.department_service.model.Department;
import com.pi.department_service.repository.DepartmentRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.pi.department_service.client.Employeeclient;
import com.pi.department_service.model.Employee;


@RestController
@RequestMapping("/Department")

public class DepartmentController {
  private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);
  @Autowired
  private DepartmentRepository repository;
  @Autowired
  private Employeeclient employeeClient;
  @PostMapping
  public Department addDepartment(@RequestBody Department department) {
      LOGGER.info("Department add:{}",department);
      return repository.addDepartment(department);
        }
  @GetMapping
  public List<Department> findAll() {
      LOGGER.info("Department find");
      return repository.findAll();
  }
  @GetMapping("/{id}")
  public Department findById(@PathVariable Long id) {
    LOGGER.info("Department find: id={}",id);
      return repository.findById(id);
  }
  @GetMapping("/with-employees")
public List<Department> findAllWithEmployees() {
    LOGGER.info("Department find with employees");

    // Fetch all departments from the repository
    List<Department> departments = repository.findAll();
    
    // Iterate through each department and try to fetch employees
    departments.forEach(department -> {
        try {
            // Attempt to fetch the employees for the current department
            List<Employee> employees = employeeClient.findByDepartment(department.getId());
            
            // Set the employees to the department object
            department.setEmployees(employees);

        } catch (Exception e) {
            // Log the error if fetching employees failed for the department
            LOGGER.error("Error fetching employees for department {}: {}", department.getId(), e.getMessage());
            
            // Optionally, set an empty list of employees in case of error
            department.setEmployees(Collections.emptyList());
        }
    });

    // Return the list of departments, even if some employee data couldn't be fetched
    return departments;
}


  
}
