package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import java.security.Principal;
import java.util.Date;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Car Parking");
		return "home";
	}

	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Car Parking");
		return "about";
	}

	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register - Car Parking");
		model.addAttribute("user", new User());
		return "signup";
	}

	// handler for registering user
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,
							   @RequestParam(value = "otp", defaultValue = "") String otp,
			@RequestParam(value = "agreement",  defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {

			String OTP = RandomString.make(8);
			String email = user.getEmail();
			if(userRepository.findByEmail(user.getEmail()).isEmpty()) {
				user.setRole("ROLE_USER");
				user.setEnabled(true);
				user.setImageUrl("default.png");
				user.setPassword(this.passwordEncoder.encode(user.getPassword()));
				user.setOneTimePassword(OTP);
				user.setOtpRequestedTime(new Date());
				this.userRepository.save(user);
				try {
					OtpVerification otpVerification = new OtpVerification();
					otpVerification.generateOneTimePassword(user, OTP);
				} catch (Exception e) {
					System.out.println(e);
				}
				model.addAttribute("user", user);
				model.addAttribute("otpbool", true);
				return "signup";
			}
			User users2 = userRepository.findByEmail(email).get(0);
			System.out.println(otp + " " + users2.getOneTimePassword());
			if(users2.getOneTimePassword().compareTo(otp)==0)
				return "login";
			else {
				model.addAttribute("user", user);
				model.addAttribute("otpbool", true);
				return "signup";
			}

	}

	//handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("title","Login Page");
		return "login";
	}


}
