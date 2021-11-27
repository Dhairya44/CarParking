package com.carparking.oauth;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.carparking.entities.AuthenticationProvider;
import com.carparking.entities.UserRepository;
import com.carparking.entities.Users;


@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
		String clientName = oAuth2User.getClientName();
		String clienName = clientName.toUpperCase();
		String email = oAuth2User.getEmail();
		String name = oAuth2User.getName();
		List<Users> user = userRepository.findByEmail(email);
		if(user.isEmpty()) {
			Users users = new Users();
			users.setEmail(email);
			users.setFirstName(name);
			users.setAuthProvider(AuthenticationProvider.valueOf(clienName));
			userRepository.save(users);
		}
		System.out.println(email);
		super.setDefaultTargetUrl("/user/index");
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
}
