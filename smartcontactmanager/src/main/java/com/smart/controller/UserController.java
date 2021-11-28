package com.smart.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import com.razorpay.*;

import com.smart.dao.WorkerRepository;
import com.smart.entities.Worker;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WorkerRepository workerRepository;

	// method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println("USERNAME " + userName);

		// get the user using usernamne(Email)

		User user = userRepository.getUserByUserName(userName);
		System.out.println("USER " + user);
		model.addAttribute("user", user);

	}

	// dashboard home
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}

	// open add form handler
	@GetMapping("/add-worker")
	public String openAddWorkerForm(Model model) {
		model.addAttribute("title", "Add Worker");
		model.addAttribute("worker", new Worker());
		return "normal/add_worker_form";
	}

	@PostMapping("/process-worker")
	public String processWorker(@ModelAttribute Worker worker,
								 Principal principal, HttpSession session) {

		try {

			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);
			user.getWorker().add(worker);
			worker.setUser(user);
			this.userRepository.save(user);
			System.out.println("DATA " + worker);
			System.out.println("Added to data base");

			// message success.......
			session.setAttribute("message", new Message("Worker is added !! Add more..", "success"));

		} catch (Exception e) {
			System.out.println("ERROR " + e.getMessage());
			e.printStackTrace();
			// message error
			session.setAttribute("message", new Message("Some went wrong !! Try again..", "danger"));

		}

		return "normal/add_worker_form";
	}

	// per page = 5[n]
	// current page = 0 [page]
	@GetMapping("/show-worker/{page}")
	public String showWorkers(@PathVariable("page") Integer page, Model m, Principal principal) {
		m.addAttribute("title", "Show User Workers");

		String userName = principal.getName();

		User user = this.userRepository.getUserByUserName(userName);

		// currentPage-page
		// Worker Per page - 5
		Pageable pageable = PageRequest.of(page, 4);

		Page<Worker> worker = this.workerRepository.findWorkersByUser(user.getId(), pageable);
		m.addAttribute("workers", worker);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", worker.getTotalPages());
		return "normal/show_worker";
	}

	@GetMapping("/show-user/{page}")
	public String showUsers(@PathVariable("page") Integer page, Model m, Principal principal) {
		m.addAttribute("title", "Show Users");
		Pageable pageable = PageRequest.of(page, 4);

		//Page<Worker> worker = this.workerRepository.findWorkersByUser(user.getId(), pageable);
		Page<User> users = this.userRepository.findAll(pageable);
		m.addAttribute("users", users);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", users.getTotalPages());
		return "normal/show_user";
	}

	@RequestMapping("/{cId}/worker")
	public String showWorkerDetail(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		System.out.println("CID " + cId);

		Optional<Worker> workerOptional = this.workerRepository.findById(cId);
		Worker worker = workerOptional.get();

		//
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);

		if (user.getId() == worker.getUser().getId()) {
			model.addAttribute("worker", worker);
			model.addAttribute("title", worker.getName());
		}

		return "normal/worker_detail";
	}

	@GetMapping("/deleteuser/{id}")
	public String deleteUser(@PathVariable("id") Integer Id, HttpSession session,
							   Principal principal) {

		User user = this.userRepository.getOne(Id);

		this.userRepository.delete(user);

		session.setAttribute("message", new Message("User deleted succesfully...", "success"));

		return "redirect:/user/show-user/0";
	}

	@GetMapping("/delete/{cid}")
	@Transactional
	public String deleteWorker(@PathVariable("cid") Integer cId, HttpSession session,
								Principal principal) {
		System.out.println("CID " + cId);

		Worker worker = this.workerRepository.findById(cId).get();

		User user = this.userRepository.getUserByUserName(principal.getName());

		user.getWorker().remove(worker);

		this.userRepository.save(user);

		System.out.println("DELETED");
		session.setAttribute("message", new Message("Worker deleted succesfully...", "success"));

		return "redirect:/user/show-worker/0";
	}

	// open update form handler
	@PostMapping("/update-worker/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, Model m) {

		m.addAttribute("title", "Update Worker");

		Worker worker = this.workerRepository.findById(cid).get();

		m.addAttribute("worker", worker);

		return "normal/update_form";
	}

	@RequestMapping(value = "/process-update", method = RequestMethod.POST)
	public String updateHandler(@ModelAttribute Worker worker,
								Model m, HttpSession session, Principal principal) {

		try {

			Worker oldworkerDetail = this.workerRepository.findById(worker.getcId()).get();

			User user = this.userRepository.getUserByUserName(principal.getName());

			worker.setUser(user);

			this.workerRepository.save(worker);

			session.setAttribute("message", new Message("Your worker is updated...", "success"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("WORKER NAME " + worker.getName());
		System.out.println("WORKER ID " + worker.getcId());
		return "redirect:/user/" + worker.getcId() + "/worker";
	}

	// your profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		model.addAttribute("title", "Profile Page");
		return "normal/profile";
	}

	// open settings handler
	@GetMapping("/settings")
	public String openSettings() {
		return "normal/settings";
	}

	// change password..handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
								 @RequestParam("newPassword") String newPassword, Principal principal, HttpSession session) {
		System.out.println("OLD PASSWORD " + oldPassword);
		System.out.println("NEW PASSWORD " + newPassword);

		String userName = principal.getName();
		User currentUser = this.userRepository.getUserByUserName(userName);
		System.out.println(currentUser.getPassword());

		if (this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
			// change the password

			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(currentUser);
			session.setAttribute("message", new Message("Your password is successfully changed..", "success"));

		} else {
			// error...
			session.setAttribute("message", new Message("Please Enter correct old password !!", "danger"));
			return "redirect:/user/settings";
		}

		return "redirect:/user/index";
	}


	//creating order for payment

	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> data) throws Exception
	{
		//System.out.println("Hey order function ex.");
		System.out.println(data);

		int amt=Integer.parseInt(data.get("amount").toString());

		var client=new RazorpayClient("rzp_test_haDRsJIQo9vFPJ", "owKJJes2fwE6YD6DToishFuH");

		JSONObject ob=new JSONObject();
		ob.put("amount", amt*100);
		ob.put("currency", "INR");
		ob.put("receipt", "txn_235425");

		//creating new order

		Order order = client.Orders.create(ob);
		System.out.println(order);

		//if you want you can save this to your data..
		return order.toString();
	}

}
