package com.chatapplication.chatapp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatapplication.chatapp.model.Login;
import com.chatapplication.chatapp.model.Messages;
import com.chatapplication.chatapp.model.Registration;
import com.chatapplication.chatapp.service.MessageService;

@Controller
public class ChatController {
	  
	    private MessageService messageService;

//<-------------------------welcome--------------------------------------->	    
	    @GetMapping("/welcome")
	    public String getWelcomePage() {
	    	return "welcome";
	    }
	  
	    @Autowired
	    public ChatController(MessageService messageService) {
	        this.messageService = messageService;
	    }

//<----------------------Registration----------------------------------------->

	    @GetMapping("/registration")
	    public String showRegistrationForm(Model model) {
	        model.addAttribute("registration", new Registration());
	        return "registration"; // This corresponds to registration.html in templates
	    }

	    // To handle form submission and store data in the database
	    @PostMapping("/registration")
	    public String registerUser(@Validated Registration registration,BindingResult bindingResult,Model model) {
	    	if(bindingResult.hasErrors()) {
	    		return "registration";
	    	}
	        if(!registration.getPassword().equals(registration.getConfirmPassword())) {
	        	model.addAttribute("error","Password do not match");
	        	return "registration";
	        }
	        if (!messageService.isNotExist(registration)) {
	            model.addAttribute("error", "A user with this email already exists!");
	            return "registrationErrorPage";
	        }
	          boolean isUserCreated=messageService.saveRegistration(registration);
	          if(isUserCreated) {// if(isUserCreated && isNotExist) {
	            	  model.addAttribute("message", "Registration successfull!Please log in.");
	            	  return "redirect:/login";
	              }else {
	            	  model.addAttribute("error", "Error in Registration!Please try again.");
	            	  return "registrationErrorPage";
	              }
	    }
//<---------------------Login--------------------------------------->
	    @GetMapping("/login")
	    public String showLoginForm(Model model) {
	        model.addAttribute("login", new Login()); // Bind the form to the Login object
	        return "login"; // Return the Thymeleaf template name
	    }
	    @PostMapping("/login")
	    public String processLogin(@ModelAttribute("login") Login login , BindingResult result) {
	    	  if (result.hasErrors()) {
		            return "login";  // If validation fails, return to the form view
	    	  }
	        String email = login.getEmail();
	        String password = login.getPassword();
	      
	        boolean isValidUser = messageService.loginDetails(email, password);
	        if (isValidUser) { 
	        	return "redirect:/chat";// Redirect to a dashboard or appropriate page
	        }else {
	        	return "loginErrorPage";
	        }
	    }


 //<---------------------Messages--------------------------------------->	 
	    @GetMapping("/chat")
	    public String home() {
	        return "chat"; 
	    }
	    @MessageMapping("/chat.sendMessage")
	    @SendTo("/topic/public")
	    public Messages sendMessage(Messages message) {
	        Messages savedMessage = messageService.saveMessages(message);
	        message.setTimestamp(LocalDateTime.now().toString());
	        return message;
	    }
	    // Endpoint to fetch all messages
	    @GetMapping
	    public List<Messages> getAllMessages() {
	        return messageService.getAllMessages();
	    }
	   

}
