package com.smart.controller;

import com.smart.dao.ParkingSlotRepository;
import com.smart.dao.UserRepository;
import com.smart.dao.WorkerRepository;
import com.smart.entities.ParkingSlot;
import com.smart.entities.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WorkerRepository workerRepository;

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    int flag = 0;

    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title", "User Dashboard");
        List<Worker> worker1 = workerRepository.findWorkerByName(principal.getName());
        Worker worker = worker1.get(0);
        model.addAttribute("worker", worker);
        return "worker/profile";
    }

    @GetMapping("/profile/{name}")
    public String yourProfile(@PathVariable("name")String name ,Model model, Principal principal) {
        model.addAttribute("title", "Profile Page");
        List<Worker> worker1 = workerRepository.findWorkerByName(name);
        Worker worker = worker1.get(0);
        model.addAttribute("worker", worker);
        return "worker/profile";
    }

    @GetMapping("/show-slots/{page}/{name}")
    public String showSlots(@PathVariable("name") String name, @PathVariable("page") Integer page, Model m, Principal principal) {
        m.addAttribute("title", "Show Parkings");
        List<Worker> worker1 = workerRepository.findWorkerByName(name);
        Worker worker = worker1.get(0);
        m.addAttribute("worker", worker);
        if(flag==1)
            m.addAttribute("error" , true);
        flag  = 0;
        Pageable pageable = PageRequest.of(page, 4);
        Page<ParkingSlot> slots = this.parkingSlotRepository.findAll(pageable);
        m.addAttribute("slots", slots);
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", slots.getTotalPages());
        return "worker/show_slot";
    }

    @PostMapping("/update-service/{id}/{name}")
    public String bookSlots(@PathVariable("name") String name, @PathVariable("id") Integer id, Model m, Principal principal) {
        m.addAttribute("title", "Add Services");
        ParkingSlot parkingSlot = this.parkingSlotRepository.getOne(id);
        List<Worker> worker1 = workerRepository.findWorkerByName(name);
        Worker worker = worker1.get(0);
        if(parkingSlot.getService()==null){
            m.addAttribute("worker", worker);
            m.addAttribute("slot", parkingSlot.getId());
            return "worker/add_service";
        }
        else{
            flag = 1;
            return "redirect:/worker/show-slots/0/"+worker.getName();
        }
    }

    @PostMapping("/add-services/{id}/{name}")
    public String addSlots(@Valid @ModelAttribute("service") String service, @PathVariable("name") String name, @PathVariable("id") Integer id, Model m, Principal principal) {
        ParkingSlot parkingSlot = this.parkingSlotRepository.findById(id).get();
        List<Worker> worker1 = workerRepository.findWorkerByName(name);
        Worker worker = worker1.get(0);
        parkingSlot.setService(service+"("+worker.getName()+")");
        parkingSlotRepository.save(parkingSlot);
        return "redirect:/worker/show-slots/0/"+worker.getName();
    }

    @GetMapping("/show-services/{page}/{name}")
    public String showServices(@PathVariable("name") String name, @PathVariable("page") Integer page, Model m, Principal principal) {
        m.addAttribute("title", "Show Services");
        List<Worker> worker1 = workerRepository.findWorkerByName(name);
        Worker worker = worker1.get(0);
        m.addAttribute("worker", worker);
        Pageable pageable = PageRequest.of(page, 4);
        Page<ParkingSlot> slots = this.parkingSlotRepository.findAll(pageable);
        List<ParkingSlot> slot = this.parkingSlotRepository.findParkingSlotByServiceContaining(name);
        m.addAttribute("slots", slot);
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", slots.getTotalPages());
        return "worker/show_services";
    }

    @PostMapping("/cancel-service/{id}/{name}")
    public String updateParking(@PathVariable("name")String name, @PathVariable("id") Integer Id, Model m, Principal principal) {
        ParkingSlot parkingSlot = this.parkingSlotRepository.getOne(Id);
        List<Worker> worker1 = workerRepository.findWorkerByName(name);
        Worker worker = worker1.get(0);
        parkingSlot.setService(null);
        parkingSlotRepository.save(parkingSlot);
        return "redirect:/worker/show-services/0/"+worker.getName();
    }
}
