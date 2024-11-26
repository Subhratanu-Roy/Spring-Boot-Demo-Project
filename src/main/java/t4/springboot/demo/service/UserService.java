package t4.springboot.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import t4.springboot.demo.model.User;
import t4.springboot.demo.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	PasswordEncoder encoder;

	public User addUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		userRepo.save(user);
		return user;
	}

	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	public User getUserByName(String name) {
		return userRepo.findByUsername(name).get();
	}

	public User updateUser(int id, User newUser) {
		User user = userRepo.findById(id).get();
		user.setUsername(newUser.getUsername());
		user.setRoles(newUser.getRoles());
		userRepo.save(user);
		return user;

	}
	
	public String deleteUserById(int id) {
		userRepo.deleteById(id);
		return "User deleted suucessfully";
	}

}
