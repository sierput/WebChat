package pl.tieto.mat.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import pl.tieto.mat.User;

@Component
public class UserValidator implements Validator {

	@Override
	public boolean supports(Class<?> classObj) {
		return User.class.equals(classObj);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		User user = (User) obj;
		if (!user.getPassword().equals(user.getPasswordConfirm())) {
			errors.rejectValue("passwordConfirm", "PasswordsAreDifferent");
		}
	}
}
