package pl.tieto.mat.service;

import pl.tieto.mat.User;

public interface UserService {
	public void save(User user);
	public User findByFirstname(String username);
	public void updateUserById(User user);
	public void approveUserById(int id);
}