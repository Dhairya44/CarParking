package com.smart.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.smart.dao.UserRepository;
import com.smart.dao.WorkerRepository;
import com.smart.entities.User;
import com.smart.entities.Worker;
import com.smart.helper.Message;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WorkerRepository workerRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title", "User Dashboard");
        List<Worker> worker1 = workerRepository.findWorkerByName(principal.getName());
        Worker worker = worker1.get(0);
        model.addAttribute("worker", worker);
        return "worker/profile";
    }

    @GetMapping("/profile")
    public String yourProfile(Model model, Principal principal) {
        model.addAttribute("title", "Profile Page");
        List<Worker> worker1 = workerRepository.findWorkerByName(principal.getName());
        Worker worker = worker1.get(0);
        model.addAttribute("worker", worker);
        return "worker/profile";
    }

    @GetMapping("/settings")
    public String openSettings(Model model, Principal principal) {
        User worker = userRepository.getUserByUserName(principal.getName());
        model.addAttribute("worker", worker);
        return "worker/settings";
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
            return "redirect:/worker/settings";
        }
        return "redirect:/worker/index";
    }

}
