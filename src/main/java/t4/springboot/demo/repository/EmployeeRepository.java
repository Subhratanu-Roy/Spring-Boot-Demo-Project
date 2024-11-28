package t4.springboot.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import t4.springboot.demo.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	public Optional<Employee> findByName(String username);
	
}
