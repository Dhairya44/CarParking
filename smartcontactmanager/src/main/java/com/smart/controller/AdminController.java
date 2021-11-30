package com.smart.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title", "User Dashboard");
        User admin = userRepository.getUserByUserName(principal.getName());
        model.addAttribute("admin", admin);
        return "admin/admin_dashboard";
    }

    @GetMapping("/profile")
    public String yourProfile(Model model, Principal principal) {
        model.addAttribute("title", "Profile Page");
        User admin = userRepository.getUserByUserName(principal.getName());
        model.addAttribute("admin", admin);
        return "admin/profile";
    }

    @GetMapping("/settings")
    public String openSettings(Model model, Principal principal) {
        User admin = userRepository.getUserByUserName(principal.getName());
        model.addAttribute("admin", admin);
        return "admin/settings";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,Model model,
                                 @RequestParam("newPassword") String newPassword, Principal principal, HttpSession session) {

        String userName = principal.getName();
        User currentUser = this.userRepository.getUserByUserName(userName);
        if (this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {

            currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
            this.userRepository.save(currentUser);
            session.setAttribute("message", new Message("Your password is successfully changed..", "success"));

        } else {

            session.setAttribute("message", new Message("Please Enter correct old password !!", "danger"));
            return "redirect:/admin/settings";
        }
        return "redirect:/admin/index";
    }

    @GetMapping("/payment")
    public String payment(Model model, Principal principal) {
        model.addAttribute("title", "Profile Page");
        User admin = userRepository.getUserByUserName(principal.getName());
        model.addAttribute("admin", admin);
        return "admin/payment";
    }

    @PostMapping("/create_order")
    @ResponseBody
    public String createOrder(@RequestBody Map<String, Object> data) throws Exception
    {
        int amt=Integer.parseInt(data.get("amount").toString());

        var client=new RazorpayClient("rzp_test_haDRsJIQo9vFPJ", "owKJJes2fwE6YD6DToishFuH");

        JSONObject ob=new JSONObject();
        ob.put("amount", amt*100);
        ob.put("currency", "INR");
        ob.put("receipt", "txn_235425");

        Order order = client.Orders.create(ob);
        return order.toString();
    }

}
