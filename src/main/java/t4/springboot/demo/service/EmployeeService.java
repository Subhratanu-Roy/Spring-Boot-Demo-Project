package t4.springboot.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import t4.springboot.demo.model.Employee;
import t4.springboot.demo.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	EmployeeRepository empRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	public ResponseEntity<Object> addEmpl(Employee emp) {
		try {
			emp.setPassword(passwordEncoder.encode(emp.getPassword()));
			empRepo.save(emp);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
		}
	}

	public ResponseEntity<Object> getEmployee() {
		try {
			return ResponseEntity.ok(empRepo.findAll());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	public ResponseEntity<Object> getEmployeeByName(String name) {
		try {
			Employee emp = empRepo.findByName(name).get();
			if (emp == null)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			return ResponseEntity.ok(empRepo.findByName(name));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	public ResponseEntity<Object> getEmployeeById(int id) {
		try {
			return ResponseEntity.ok(empRepo.findById(id));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	public ResponseEntity<Object> updEmp(int id, Employee nemp) {
		try {
			Employee oemp = empRepo.findById(id).get();
			if(oemp == null)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			oemp.setName(nemp.getName());
			oemp.setPassword(nemp.getPassword());
			oemp.setRoles(nemp.getRoles());
			empRepo.save(oemp);
			return ResponseEntity.ok(oemp);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	public ResponseEntity<Object> delEmp(int id) {
		try {
			empRepo.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}
