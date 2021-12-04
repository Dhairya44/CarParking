package com.smart.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.smart.dao.ParkingSlotRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.ParkingSlot;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.sms.SmsSender;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private int flag=0;

    @Autowired
    SmsSender smsSender;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ParkingSlotRepository parkingSlotRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

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

        var client=new RazorpayClient("rzp_test_3fGEPJTbBw4c9f", "ntnofRbVEbYf5xQ7q962WQZE");

        JSONObject ob=new JSONObject();
        ob.put("amount", amt*100);
        ob.put("currency", "INR");
        ob.put("receipt", "txn_235425");

        Order order = client.Orders.create(ob);

        return order.toString();
    }

    @GetMapping("/show-slots/{page}")
    public String showSlots(@PathVariable("page") Integer page, Model m, Principal principal) {
        m.addAttribute("title", "Show Parkings");
        User admin = userRepository.getUserByUserName(principal.getName());
        m.addAttribute("admin", admin);
        Pageable pageable = PageRequest.of(page, 4);
        Page<ParkingSlot> slots = this.parkingSlotRepository.findAll(pageable);
        boolean error = flag == 1;
        flag = 0;
        m.addAttribute("error", error);
        m.addAttribute("slots", slots);
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", slots.getTotalPages());
        return "admin/show_slot";
    }

    @PostMapping("/book-slot/{id}")
    public String bookSlots(@PathVariable("id") Integer id, Model m, Principal principal) {
        m.addAttribute("title", "Book Parking");
        ParkingSlot parkingSlot = this.parkingSlotRepository.findById(id).get();
        User admin = userRepository.getUserByUserName(principal.getName());
        m.addAttribute("admin", admin);
        if(parkingSlot.getAvailable()>0) {
            parkingSlot.setAvailable(parkingSlot.getAvailable() - 1);
        }
        else{
            parkingSlot.setWaiting(parkingSlot.getWaiting()+1);
        }

        if(!(parkingSlot.getNameOfUsers()==null) && parkingSlot.getNameOfUsers().contains(admin.getUsername())){
            flag = 1;
            return "redirect:/admin/show-slots/0";
        }

        parkingSlot.setDate(new Date());

//        String to = admin.getPhone();
//        String message = "Your Parking Slot at Location "+parkingSlot.getLocation()+" with In Time: " + parkingSlot.getInTime()+" is booked!";
//        smsSender.sendSms(message, to);

        if(parkingSlot.getNameOfUsers()==null)
            parkingSlot.setNameOfUsers(admin.getUsername());
        else
            parkingSlot.setNameOfUsers(admin.getUsername()  + " " + parkingSlot.getNameOfUsers());

        parkingSlotRepository.save(parkingSlot);
        return "redirect:/admin/payment";
    }

    @GetMapping("/show-bookings/{page}")
    public String showBookings(@PathVariable("page") Integer page, Model m, Principal principal) {
        m.addAttribute("title", "Show Bookings");
        User admin = userRepository.getUserByUserName(principal.getName());
        m.addAttribute("admin", admin);
        Pageable pageable = PageRequest.of(page, 4);
        int cost = 0;
        Page<ParkingSlot> slots = this.parkingSlotRepository.findAll(pageable);
        List<ParkingSlot> bookings = this.parkingSlotRepository.findParkingSlotByNameOfUsersContaining(admin.getUsername());
        if(!bookings.isEmpty()) {
            cost = 100;
            for (ParkingSlot book : bookings) {
                cost += book.getPrice();
            }
        }
        m.addAttribute("cost", cost);
        m.addAttribute("book", bookings);
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", slots.getTotalPages());
        return "admin/my-booking";
    }

    @PostMapping("/cancel-slot/{id}")
    public String updateParking(@PathVariable("id") Integer Id, Model m, Principal principal) {
        ParkingSlot parkingSlot = this.parkingSlotRepository.getOne(Id);
        User admin = this.userRepository.getUserByUserName(principal.getName());
        String master = parkingSlot.getNameOfUsers();
        String target = admin.getUsername();
        String processed = master.replace(target, "\n");
        System.out.println(target + "$" +  master + "$" + processed);
        parkingSlot.setNameOfUsers(processed);
        parkingSlotRepository.save(parkingSlot);
        return "redirect:/admin/show-bookings/0/";
    }

}
