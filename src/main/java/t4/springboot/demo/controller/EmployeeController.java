package t4.springboot.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import t4.springboot.demo.model.Employee;
import t4.springboot.demo.security.JWTUtil;
import t4.springboot.demo.service.EmployeeService;

@RestController
@RequestMapping("/emp")
public class EmployeeController {

	@Autowired
	EmployeeService empserv;

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	JWTUtil jwtUtil;

	@PostMapping("/add")
	public ResponseEntity<Object> addEmp(@RequestBody Employee employee) {
		return empserv.addEmpl(employee);

	}

	@GetMapping("/getall")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> getAllEmp() {
		return empserv.getEmployee();

	}

	@GetMapping("/get/{id}")
	@PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
	public ResponseEntity<Object> getEmpById(@PathVariable(value = "id") int id) {
		return empserv.getEmployeeById(id);
	}

	@GetMapping("/get")
	@PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
	public ResponseEntity<Object> getEmpByName(@RequestParam(value = "name") String name) {
		return empserv.getEmployeeByName(name);
	}

	@DeleteMapping("/del/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> deleteEmp(@PathVariable("id") int id) {
		return empserv.delEmp(id);
	}

	@PutMapping("/upd/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> UpdEmp(@PathVariable("id") int id, @RequestBody Employee nemp) {
		return empserv.updEmp(id, nemp);
	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody AuthRequest authReq){
		try {
		System.out.println(authReq.getUsername()+" and "+authReq.getPassword());
		Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword()));
		if (auth.isAuthenticated()) {
			System.out.println("Authentication successful");
			String token = jwtUtil.generateToken(authReq.getUsername());
			return ResponseEntity.ok(token);
		}
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	
	
	}
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class AuthRequest {
	String username;
	String password;
}
