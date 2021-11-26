package com.carparking.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.unbescape.css.CssIdentifierEscapeLevel;

import com.carparking.entities.AuthenticationProvider;
import com.carparking.entities.UserRepository;
import com.carparking.entities.Users;

import net.bytebuddy.utility.RandomString;

@Controller
public class MainController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/")
	public String base() {
		return "base";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("user", new Users());
		return "signup2";
	}
	
	@PostMapping("/registered")
	public String dashboard(@ModelAttribute("user") Users users, Model model, @RequestParam(value = "otp", defaultValue = "") String otp) {
			String OTP = RandomString.make(8);
			String email = users.getEmail();
			if(userRepository.findByEmail(users.getEmail()).isEmpty()) {
				users.setRole("ROLE_USER");
				users.setPassword(passwordEncoder.encode(users.getPassword()));
				users.setAuthProvider(AuthenticationProvider.LOCAL);
				users.setOneTimePassword(OTP);
				users.setOtpRequestedTime(new Date());
				this.userRepository.save(users);
				model.addAttribute("user", users);
				model.addAttribute("otpbool", true);
				
				try
				{
					OtpVerification otpvefVerification = new OtpVerification();
					otpvefVerification.generateOneTimePassword(users, OTP);
				}
				catch (Exception e) {
					System.out.println(e);
				}
				return "signup2";
			}
			Users users2 = userRepository.findByEmail(email).get(0);
			System.out.println(otp + " " + users2.getOneTimePassword());
			if(users2.getOneTimePassword().compareTo(otp)==0)
				return "User/dashboard";
			else {
				model.addAttribute("user", users);
				model.addAttribute("otpbool", true);
				return "signup2";
			}
	}

	@GetMapping("/login")
	public String login() {
		return "login2";
	}
	
	@GetMapping("/do-login")
	public String logicProcess() {
		return "User/dashboard";
	}
	
}
