package com.artworkmanagement.GraphQL;

import java.util.List;

import org.springframework.stereotype.Component;

import com.artworkmanagement.entities.Employee;
import com.artworkmanagement.repository.EmployeeRepository;

import graphql.kickstart.tools.GraphQLQueryResolver;

@Component
public class EmployeeGraphQLResolver implements GraphQLQueryResolver {
	private final EmployeeRepository employeeRepository;

	public EmployeeGraphQLResolver(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public List<Employee> listAllEmployees() {
		List<Employee> list = employeeRepository.findAll();
		return list;
	}

	public Employee findEmployee(Long id) {
		return employeeRepository.findById(id).orElse(null);
	}
}
