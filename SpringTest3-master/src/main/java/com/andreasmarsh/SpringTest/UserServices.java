package com.andreasmarsh.SpringTest;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;

@Service
public class UserServices {

    @Autowired
    private UserRepository repo;

    @Autowired
    private AddressRepository repo2;

    @Autowired
    private CreditCardRepository repo3;

    @Autowired
    private PromotionRepository promotionRepo;

    @Autowired
    private MovieRepository movieRepo;

    @Autowired
    private MovieShowingRepository movieShowingRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    public List<User> listAll() {
        return repo.findAll();
    }

    public void register(User user,Address address, String siteURL)
            throws UnsupportedEncodingException, MessagingException {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String randomCode = RandomString.make(64);

        //begin by setting user to non-admin status. Admin status must be given manually behind-the-scenes
        user.setAdminStatus(false); //'false' = non-admin user

        user.setVerificationCode(randomCode);
        user.setEnabled(false);

        user.setAddress(address);

        repo.save(user);
        repo2.save(address);

        sendVerificationEmail(user, siteURL);
    }

    public void reset(String email, String siteURL)
            throws UnsupportedEncodingException, MessagingException {
        PasswordGenerator pwdGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .build();
        String newPwd = pwdGenerator.generate(8);

        User user = repo.findByEmail(email);
        user.setId(repo.findByEmail(user.getEmail()).getId());
        Address address = repo2.findByAddressID(user.getAddress().getAddressID());

        if(user.isEnabled() == true) {
            user.setEnabled(true);
        }

        repo.deleteById(user.getId());

        user.setAddress(address);
        String encodedPassword = passwordEncoder.encode(newPwd);
        user.setPassword(encodedPassword);

        repo.save(user);

        sendResetEmail(user, newPwd, siteURL);
    }

    public void update(User user, Address address, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        System.out.println(user.getEmail());
        user.setId(repo.findByEmail(user.getEmail()).getId());
        repo.deleteById(user.getId());

        user.setAddress(address);
        user.setEnabled(true);

        repo.save(user);

        sendEditEmail(user, siteURL);
    }

    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "CinemaE-Booking@gmail.com";
        String senderName = "Cinema E-Booking";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Team A6 Cinema E-Booking System.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFullName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

        System.out.println("Email has been sent");
    }

    private void sendResetEmail(User user, String password, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "CinemaE-Booking@gmail.com";
        String senderName = "Cinema E-Booking";
        String subject = "Password Reset Successfully";
        String content = "Dear [[name]],<br>"
                + "Your password has been reset to:<br>"
                + "<h3>[[password]]</h3>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Login</a></h3>"
                + "Thank you,<br>"
                + "Team A6 Cinema E-Booking System.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFullName());
        content = content.replace("[[password]]", password);

        String loginURL = siteURL + "/login";

        content = content.replace("[[URL]]", loginURL);

        helper.setText(content, true);

        mailSender.send(message);

        System.out.println("Email has been sent");
    }

    public void sendPromotion(Promotion promotion, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        List<User> signedUpUsers = repo.findByPromotions(true);

        signedUpUsers.forEach((user) -> {
            try {
                sendPromoEmail(user, promotion, siteURL);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendPromoEmail(User user, Promotion promotion, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "CinemaE-Booking@gmail.com";
        String senderName = "Cinema E-Booking";
        String subject = "New Promo";
        String content = "Dear [[name]],<br><br>"
                + "[[description]]<br><br>"
                + "Try out Code:<br>"
                + "<h3>[[code]]</h3>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Login</a></h3>"
                + "Thank you,<br>"
                + "Team A6 Cinema E-Booking System.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFullName());
        content = content.replace("[[description]]", promotion.getDescription());
        content = content.replace("[[code]]", promotion.getCode());

        String loginURL = siteURL + "/login";

        content = content.replace("[[URL]]", loginURL);

        helper.setText(content, true);

        mailSender.send(message);

        System.out.println("Email has been sent");
    }

    private void sendEditEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "CinemaE-Booking@gmail.com";
        String senderName = "Cinema E-Booking";
        String subject = "Profile Updated Successfully";
        String content = "Dear [[name]],<br>"
                + "Your profile has been updated.<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Login</a></h3>"
                + "Thank you,<br>"
                + "Team A6 Cinema E-Booking System.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFullName());

        String loginURL = siteURL + "/login";

        content = content.replace("[[URL]]", loginURL);

        helper.setText(content, true);

        mailSender.send(message);

        System.out.println("Email has been sent");
    }

    public boolean verify(String verificationCode) {
        User user = repo.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            repo.save(user);

            return true;
        }

    }

    public void deletePromotion(Promotion promotion) {
        promotionRepo.deleteById(promotion.getPromotionID());
    }

    public void deleteMovie(Movie movie) {
        // movieShowingRepo.findByMovie(movie.getMovieID())
        movieShowingRepo.deleteAll(movieShowingRepo.findByMovie(movie));
        movieRepo.deleteById(movie.getMovieID());
    }
}