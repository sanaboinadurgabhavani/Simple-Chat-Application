package com.chatapplication.chatapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.chatapplication.chatapp.model.Login;
import com.chatapplication.chatapp.model.Messages;
import com.chatapplication.chatapp.model.Registration;
import com.chatapplication.chatapp.repository.MessagesRepository;
import com.chatapplication.chatapp.repository.RegistrationRepository;

@Service
public class MessageService {

	   
	 private final RegistrationRepository registrationRepository;
	 private final MessagesRepository messagesRepository;

	    @Autowired
	    public MessageService(RegistrationRepository registrationRepository,MessagesRepository messagesRepository) {
	        this.registrationRepository = registrationRepository;
	        this.messagesRepository=messagesRepository;
	    }
//<--------------------------Registration----------------------------------->

	    public boolean isNotExist(Registration newRegistration) {
	        return registrationRepository.findByEmail(newRegistration.getEmail().toLowerCase()).isEmpty();
	    }

	    public boolean saveRegistration(Registration registration) {
	        try {
	            registrationRepository.save(registration);
	            return true;
	        } catch (DataIntegrityViolationException e) {
	            return false; // Duplicate entry or other constraint violations
	        }
	    }

//<--------------------------Login----------------------------------->
		
		public boolean loginDetails(String email, String password) {
		    Optional<Registration> result = registrationRepository.findByEmailAndPassword(email, password);
		    return result.isPresent();
		}
		
// ---------------------------Save a new message--------------------->
	   
	        // Example of additional logic: Add a timestamp before saving
		 public Messages saveMessages(Messages messages) {
	        messages.setTimestamp(System.currentTimeMillis());
	        return messagesRepository.save(messages);
	    }

	    // Get all messages
	    public List<Messages> getAllMessages() {
	        return messagesRepository.findAll(Sort.by(Sort.Direction.ASC, "timestamp"));
	    }


	    // Find a message by ID
	    public Optional<Messages> getMessagesById(Long id) {
	        return messagesRepository.findById(id);
	    }

	    // Delete a message by ID
	    public void deleteMessagesById(Long id) {
	        if (messagesRepository.existsById(id)) {
	            messagesRepository.deleteById(id);
	        } else {
	            throw new RuntimeException("Message with ID " + id + " not found.");
	        }
	    }


		
}
