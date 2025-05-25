package com.pi.department_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    // LoadBalancedExchangeFilterFunction will enable load balancing using Spring Cloud
    @Autowired
    private LoadBalancedExchangeFilterFunction filterFunction;

    // Define the WebClient bean for employee service communication
    @Bean
    public WebClient employeeWebClient() {
        return WebClient.builder()
            .baseUrl("http://employee-service")  // The service URL (load balanced via Spring Cloud)
            .filter(filterFunction)  // Add load balancer filter
            .build();
    }

    // Bean for the Employeeclient interface, which is implemented using WebClient
    @Bean
    public Employeeclient employeeClient() {
        return new EmployeeclientImpl(employeeWebClient());
    }

    // Manual implementation of Employeeclient using WebClient
    public static class EmployeeclientImpl implements Employeeclient {

        private final WebClient webClient;

        public EmployeeclientImpl(WebClient webClient) {
            this.webClient = webClient;
        }

        // Implementing the method to fetch Employee data by ID
        @Override
        public Mono<Employee> getEmployeeById(String id) {
            return webClient.get()
                    .uri("/employees/{id}", id)  // API endpoint for retrieving employee
                    .retrieve()
                    .bodyToMono(Employee.class);  // Convert response body to Employee object
        }
    }

    // Employee class (you can adjust it based on your actual Employee object)
    public static class Employee {
        private String id;
        private String name;
        private String department;

        // Getters and setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        @Override
        public String toString() {
            return "Employee{id='" + id + "', name='" + name + "', department='" + department + "'}";
        }
    }

    // Interface representing the client to interact with the Employee service
    public interface Employeeclient {
        Mono<Employee> getEmployeeById(String id);  // Method to fetch Employee by ID
    }
}
