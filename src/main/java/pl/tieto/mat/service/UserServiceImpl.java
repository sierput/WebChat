package pl.tieto.mat.service;

import pl.tieto.mat.Role;
import pl.tieto.mat.RoleRepository;
import pl.tieto.mat.User;
import pl.tieto.mat.UserRepository;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	public void save(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		if (user.getRoles() == null) {
			HashSet<Role> roles = new HashSet<>();
			roles.add(roleRepository.findByName("user"));
			user.setRoles(roles);
		}
		userRepository.save(user);
	}

	public User findByFirstname(String username) {
		return userRepository.findByFirstName(username);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void updateUserById(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.updateUserById(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getId());
		
	}

	@Override
	public void approveUserById(int id) {
		userRepository.approveUserById(id);
	}
}