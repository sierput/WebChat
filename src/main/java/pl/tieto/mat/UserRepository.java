package pl.tieto.mat;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	public User findByFirstName(String firstName);

	public User findById(Integer id);

	@Transactional
	@Modifying
	@Query("UPDATE User SET firstName = ?1, lastName = ?2, email = ?3, password = ?4 where id = ?5")
	public void updateUserById(String firstName, String LastName, String email, String password, int id);

	@Transactional
	@Modifying
	@Query("UPDATE User SET approved = 1 where id = ?1")
	public void approveUserById(int id);
}
