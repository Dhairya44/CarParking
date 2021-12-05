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
import javax.validation.Valid;
import java.security.Principal;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private int flag=0;
    private int cost = 0;
    private int ParkingID = 0;

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
        model.addAttribute("cost", cost);
        model.addAttribute("admin", admin);
        return "admin/payment";
    }

    @GetMapping("/add-money")
    public String addMoney(Model model, Principal principal) {
        model.addAttribute("title", "Profile Page");
        User admin = userRepository.getUserByUserName(principal.getName());
        model.addAttribute("cost", cost);
        model.addAttribute("admin", admin);
        return "admin/money";
    }

    @PostMapping("/create_order")
    @ResponseBody
    public String createOrder(@RequestBody Map<String, Object> data, Principal principal) throws Exception
    {
        int amt=Integer.parseInt(data.get("amount").toString());
        ParkingSlot parkingSlot = parkingSlotRepository.getOne(ParkingID);
        User user = userRepository.getUserByUserName(principal.getName());

        if(user.getMoney()<amt)
            return "redirect:/admin/payment";

        parkingSlot.setDate(new Date());
        if(parkingSlot.getNameOfUsers()==null)
            parkingSlot.setNameOfUsers(user.getUsername());
        else
            parkingSlot.setNameOfUsers(user.getUsername()  + " " + parkingSlot.getNameOfUsers());
        if(parkingSlot.getRegisNumber()==null)
            parkingSlot.setRegisNumber(user.getCarRegis());
        else
            parkingSlot.setRegisNumber(user.getCarRegis()+" "+parkingSlot.getRegisNumber());

        user.setMoney(user.getMoney()-amt);
        userRepository.save(user);
        parkingSlotRepository.save(parkingSlot);
        var client=new RazorpayClient("rzp_test_3fGEPJTbBw4c9f", "ntnofRbVEbYf5xQ7q962WQZE");

        JSONObject ob=new JSONObject();
        ob.put("amount",  amt*100);
        ob.put("currency", "INR");
        ob.put("receipt", "txn_235425");
        Order order = client.Orders.create(ob);

        String to = user.getPhone();
        String message = "Your Parking Slot at Location "+parkingSlot.getLocation()+" on " + parkingSlot.getDay() +" with In Time: " + parkingSlot.getInTime()+" and Out Time: " + parkingSlot.getOutTime() +" is booked!";
        smsSender.sendSms(message, to);

        return order.toString();
    }

    @PostMapping("/add_money")
    @ResponseBody
    public String addMoney(@RequestBody Map<String, Object> data, Principal principal) throws Exception
    {
        int amt=Integer.parseInt(data.get("amount").toString());
        User user = userRepository.getUserByUserName(principal.getName());
        user.setMoney(user.getMoney() + amt);
        userRepository.save(user);
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
        User admin = userRepository.getUserByUserName(principal.getName());
        m.addAttribute("admin", admin);
        m.addAttribute("id", id);
        return "admin/add_registration";
    }

    @PostMapping("/process-regis/{id}")
    public String processRegis(@PathVariable("id")Integer id, @Valid @ModelAttribute("carModel") String carModel, @Valid @ModelAttribute("carRegis") String carRegis, Model model, Principal principal){
        ParkingSlot parkingSlot = parkingSlotRepository.getOne(id);
        User admin = userRepository.getUserByUserName(principal.getName());

        if(!(parkingSlot.getNameOfUsers()==null) && parkingSlot.getNameOfUsers().contains(admin.getUsername())){
            flag = 1;
            return "redirect:/admin/show-slots/0";
        }

        if(parkingSlot.getAvailable()>0) {
            parkingSlot.setAvailable(parkingSlot.getAvailable() - 1);
        }
        else{
            parkingSlot.setWaiting(parkingSlot.getWaiting()+1);
        }

        admin.setCarModel(carModel);
        admin.setCarRegis(carRegis);
        userRepository.save(admin);
        ParkingID = id;
        cost = parkingSlot.getPrice();
        parkingSlotRepository.save(parkingSlot);
        model.addAttribute("cost", cost);
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
        String master2 = parkingSlot.getRegisNumber();
        String target2 = admin.getCarRegis();
        String processed2 = master2.replace(target2, "\n");

        admin.setCarRegis(null);
        admin.setCarModel(null);
        admin.setMoney(admin.getMoney()+parkingSlot.getPrice());
        userRepository.save(admin);
        parkingSlot.setNameOfUsers(processed);
        parkingSlot.setRegisNumber(processed2);
        parkingSlotRepository.save(parkingSlot);

        String to = admin.getPhone();
        String message = "Your Booking Parking Slot is Cancelled!!";
        smsSender.sendSms(message, to);

        return "redirect:/admin/show-bookings/0/";
    }
}
