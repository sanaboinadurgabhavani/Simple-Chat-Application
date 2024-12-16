package com.chatapplication.chatapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chatapplication.chatapp.model.Registration;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long>{
//	Registration findByName(String name);
	Optional<Registration> findByEmail(String email);
	
	//<--------------------------Login----------------------------------->
	  @Query("SELECT r FROM Registration r WHERE r.email = :email AND r.password = :password")
	    Optional<Registration> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

}
