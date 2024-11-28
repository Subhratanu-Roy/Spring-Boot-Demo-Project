package t4.springboot.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import t4.springboot.demo.model.Employee;
import t4.springboot.demo.repository.EmployeeRepository;

@Service
public class UserAuthService implements UserDetailsService{

	@Autowired
	EmployeeRepository empRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Employee emp = empRepo.findByName(username).get();
		if (emp == null)
			throw new UsernameNotFoundException("User not found!!");
		return emp;
	}

}
