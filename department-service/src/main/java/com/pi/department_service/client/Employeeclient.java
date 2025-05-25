package com.pi.department_service.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.pi.department_service.model.Employee;

@FeignClient(name = "employee-service")  // Replace with the actual name of the service you're calling
public interface Employeeclient {

    @GetMapping("/employee/department/{departmentID}")
    List<Employee> findByDepartment(@PathVariable("departmentID") Long departmentId);
}
