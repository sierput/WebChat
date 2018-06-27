package pl.tieto.mat.config;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import pl.tieto.mat.Role;
import pl.tieto.mat.RoleRepository;
import pl.tieto.mat.User;
import pl.tieto.mat.UserRepository;
import pl.tieto.mat.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
            .authorizeRequests()
                .antMatchers("/", "/adduser").permitAll().antMatchers("/css/**").permitAll()
                .antMatchers("/showWaitingUsers/approve/{id}").hasAuthority("admin")
                .antMatchers("/showusers").hasAuthority("admin")
                .antMatchers("/showWaitingUsers").hasAuthority("admin")
                .antMatchers("/showWaitingUsers/edit/{id}").hasAuthority("admin")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void init(WebSecurity web) throws Exception {
		// created admin and roles
		if (roleRepository.findByName("admin") == null)
			roleRepository.save(new Role("admin"));
		if (roleRepository.findByName("user") == null)
			roleRepository.save(new Role("user"));
		for (User user : userRepository.findAll()) {
			for (Role role : user.getRoles()) {
				if (role.getName().equals("admin"))
					super.init(web);
				return;
			}
		}
		User admin = new User("Mateusz", "ed", "sierputx@gmail.com", true, "test", "test");
		admin.setRoles(new HashSet<>(roleRepository.findAll()));
		userService.save(admin);

		super.init(web);
	}
}
