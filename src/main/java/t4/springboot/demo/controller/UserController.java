package t4.springboot.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import t4.springboot.demo.model.User;
import t4.springboot.demo.security.JwtUtil;
import t4.springboot.demo.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	AuthenticationManager authManager;

	@GetMapping("/admin")
	public String display_admin() {
		return "admin";
	}

	@GetMapping("/normal")
	public String display_normal() {
		return "normal";
	}

	@GetMapping("/public")
	public String display_public() {
		return "public";
	}

	@PostMapping("/add")
	public ResponseEntity<?> addUser(@RequestBody User user) {
		System.out.println("add user");
		try {
			User u = userService.addUser(user);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatusCode.valueOf(404));
		}
	}

	@GetMapping("/get")
	public ResponseEntity<?> getUser() {
		try {
			List<User> uList = userService.getAllUsers();
			return new ResponseEntity<List<User>>(uList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatusCode.valueOf(404));
		}
	}

	@GetMapping("/get/n")
	public ResponseEntity<?> getUserByName(@RequestParam("name") String name) {
		try {
			User user = userService.getUserByName(name);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatusCode.valueOf(404));
		}
	}

	@PutMapping("/upd/{id}")
	public ResponseEntity<?> updUser(@PathVariable("id") int id, @RequestBody User user) {
		try {
			User u = userService.updateUser(id, user);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatusCode.valueOf(404));
		}
	}

	@DeleteMapping("/del/{id}")
	public ResponseEntity<?> delUser(@PathVariable("id") int id) {
		try {
			userService.deleteUserById(id);
			return new ResponseEntity<String>("User deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatusCode.valueOf(404));
		}
	}

	@PostMapping("/signin")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {

		System.out.println("Signin");
		UsernamePasswordAuthenticationToken utoken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
				authRequest.getPassword());
		System.out.println(authRequest.getUsername() + " " + authRequest.getPassword());
		Authentication auth = authManager.authenticate(utoken);
		if (auth.isAuthenticated()) {
			System.out.println("Authentication done");
			return jwtUtil.generateToken(authRequest.getUsername());
		} else
			throw new UsernameNotFoundException("User name not found!!");
	}

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class AuthRequest {
	private String username;
	private String password;
}
