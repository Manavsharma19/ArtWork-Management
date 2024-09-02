package com.artworkmanagement.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.artworkmanagement.entities.Employee;
import com.artworkmanagement.model.Password;
import com.artworkmanagement.repository.EmployeeRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";

	private final EmployeeRepository employeeRepository;

	public EmployeeController(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@GetMapping
	public ResponseEntity<?> listAllEmployees() {
		List<Employee> employeeList = employeeRepository.findAll();
		if (employeeList.isEmpty()) {
			return ResponseEntity.status(404).body("Response Message : {No Data Found}");
		}
		return ResponseEntity.status(200).body(employeeList);
	}

	@PostMapping
	public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee employee) {
		Employee savedEmployee = employeeRepository.save(employee);
		return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
		if (!employeeRepository.existsById(id)) {
			return ResponseEntity.status(404).body("Response Message : {No Data Found}");
		}
		employeeRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/password")
	public ResponseEntity<?> modifyPassword(@PathVariable Long id, @RequestBody Password pass) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
		if (!employeeRepository.existsById(id)) {
			return ResponseEntity.status(404).body("Response Message : {No Data Found}");
		}
		System.out.println(pass.getNewPassword());
		if (!isPasswordValid(pass.getNewPassword())) {
			return ResponseEntity.status(400).body("Response Message : {No Data Found}");
		}
		employee.setPassword(pass.getNewPassword());
		employeeRepository.save(employee);
		return ResponseEntity.ok(employee);
	}

	public static boolean isPasswordValid(String password) {
		Pattern pattern = Pattern.compile(PASSWORD_REGEX);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}
}
