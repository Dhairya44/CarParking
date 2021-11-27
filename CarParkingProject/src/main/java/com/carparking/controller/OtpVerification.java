package com.carparking.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.carparking.entities.UserRepository;
import com.carparking.entities.Users;

import net.bytebuddy.utility.RandomString;

public class OtpVerification {
	
	@Autowired
	private UserRepository usersRepository;
	
	public void generateOneTimePassword(Users users, String oTP) throws UnsupportedEncodingException, MessagingException {
		
		sendOTPEmail(users, oTP);
		return;
		
	}
	
	private void sendOTPEmail(Users users, String oTP) throws UnsupportedEncodingException, MessagingException {
		
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.enable", true);
		props.put("mail.debug", "true");  
		props.put("mail.smtp.socketFactory.port", "587");	  
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
		props.put("mail.smtp.socketFactory.fallback", "false");
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		       protected PasswordAuthentication getPasswordAuthentication() {  
		           return new PasswordAuthentication("dhairya.agrawal44@gmail.com", "dhairya@44");  
		       }  
		       });
		
		Transport transport = session.getTransport("smtps");  
		InternetAddress addressFrom = new InternetAddress("dhairya.agrawal44@gmail.com");
		
		MimeMessage message = new MimeMessage(session);
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom("contact@CarParking.com", "CarParking Support");
		helper.setTo(users.getEmail());
		
		String subjectString = "Here's your OTP - Expire in 5 minutes";
		
		String content = "<p> Hello " + users.getFirstName() + ",</p>"
				+ "<p>Use the following OTP to login:</p>"
				+ "<p><b>" + oTP + "</b></p>"
				+ "<br>"
				+ "<p>Note this OTP will expire in 5 minutes.</p>";
		
		helper.setSubject(subjectString);
		helper.setText(content, true);
		
		transport.connect("smtp.gmail.com", 465, "Dhairya", "dhairya@44");  
		transport.sendMessage(message, message.getAllRecipients());  
		transport.close();
			
		System.out.println("Email was sent");
		return;
	}
	
	@Autowired JavaMailSender javaMailSender;
}
