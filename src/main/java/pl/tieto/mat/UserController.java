package pl.tieto.mat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import pl.tieto.mat.service.UserServiceImpl;
import pl.tieto.mat.validator.UserValidator;

@Controller
public class UserController {
	public Iterable<User> users;
	private String regexEmailValid = "^(.+)@(.+)$";
	@Autowired
	private UserRepository userRepositories;
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private UserValidator userValidator;

	private List<Message> messages;

	@GetMapping("/adduser")
	public String greetingForm(Model model) {
		addToModelActualUserName(model);
		model.addAttribute("user", new User());
		return "user";
	}

	@GetMapping("/")
	public String homePage(Model model, Message message) {

		model.addAttribute("waitingUsers", findWaitingUsers().size());
		model.addAttribute("role", getRoleForView());
		addToModelActualUserName(model);
		if (messages == null)
			messages = new ArrayList<Message>();
		model.addAttribute("messages", messages);
		model.addAttribute("newMessage", new Message());

		return "homePage";
	}

	@MessageMapping("/")
	@SendTo("/")
	public void handleMessage(String message) throws JSONException {
		JSONObject obj = new JSONObject();

		obj = new JSONObject(message);

		String user = (String) obj.get("sender");
		String content = (String) obj.get("content");
		if (user == null || user.equals(""))
			messages.add(new Message(content, ""));
		else
			messages.add(new Message(content, user));
	}

	@GetMapping("/login")
	public String loginForm(Model model) {
		model.addAttribute("user", new User());
		addToModelActualUserName(model);
		return "login";
	}

	@GetMapping("/logout")
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";// You can redirect wherever you want, but generally it's a good practice to
										// show login screen again.
	}

	@PostMapping("/adduser")
	public String checkPersonInfo(@ModelAttribute @Valid User user, BindingResult bindingResult,
			HttpServletRequest request) {
		userValidator.validate(user, bindingResult);
		if (!isEmail(user.getEmail())) {
			bindingResult.rejectValue("email", "error.user", "Wrong format, try use: xxx@xx.xx");
		}
		if (bindingResult.hasErrors()) {
			return "user";
		} else {
			addToDB(user);
			return "result";
		}
	}

	private boolean isEmail(String email) {
		Pattern pattern = Pattern.compile(regexEmailValid);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	private void addToDB(User user) {
		userService.save(user);
	}

	@GetMapping("/showusers")
	public String showUser(Model model) {
		addToModelActualUserName(model);
		users = userRepositories.findAll();
		model.addAttribute("users", users);
		return "resultall";
	}

	@GetMapping("/showWaitingUsers")
	public String showWaintingUsers(Model model) {
		addToModelActualUserName(model);
		model.addAttribute("users", findWaitingUsers());
		return "showWaitingUsers";
	}

	private List<User> findWaitingUsers() {
		users = userRepositories.findAll();
		List<User> userToApprove = new ArrayList<>();
		for (User user : users) {
			if (!user.isApproved())
				userToApprove.add(user);
		}
		return userToApprove;
	}

	@GetMapping("/showWaitingUsers/approve/{id}")
	public String usersApprove(Model model, @PathVariable(name = "id") int id) {
		addToModelActualUserName(model);
		userService.approveUserById(id);
		return "showWaitingUsers";
	}

	@GetMapping("/showWaitingUsers/edit/{id}")
	public String usersedit(Model model, @PathVariable(name = "id") int id) {
		addToModelActualUserName(model);
		model.addAttribute("user", userRepositories.findById(id));
		return "userEditForm";

	}

	@PostMapping("/showWaitingUsers/edit/{id}")
	public String usersEditAccept(@ModelAttribute @Valid User user, BindingResult bindingResult,
			@PathVariable(name = "id") int id) {
		user.setId(id);
		if (!isEmail(user.getEmail())) {
			bindingResult.rejectValue("email", "error.user", "Wrong format, try use: xxx@xx.xx");
		}
		if (bindingResult.hasErrors() && !bindingResult.getFieldError().getField().equals("id")) { // second condition
			return "userEditForm";
		} else {
			userService.updateUserById(user);
			return "result";
		}
	}

	private String getRoleForView() {
		if (getRoles() == null)
			return "notLogged";
		if (getRoles().contains(("admin")))
			return "admin";
		else
			return "user";
	}

	private ArrayList<String> getRoles() {
		User userActual = getActualUser();
		if (userActual == null)
			return null;
		ArrayList<String> roles = new ArrayList<>();
		for (Role role : userActual.getRoles()) {
			roles.add(role.getName());
		}
		return roles;
	}

	private User getActualUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = "no body";
		if (auth != null)
			name = auth.getName();
		return userRepositories.findByFirstName(name);
	}

	private void addToModelActualUserName(Model model) {
		User user = getActualUser();
		if (user == null) {
			model.addAttribute("userName", "not logged");
		} else {
			model.addAttribute("userName", getActualUser().getFirstName());
		}
	}
}
