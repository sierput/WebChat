package pl.tieto.mat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/")
public class HomeController  {

	@GetMapping(path = "/helloworld")
	public @ResponseBody String home() {
		return "hello world";
	}
}
