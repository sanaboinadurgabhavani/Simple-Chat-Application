package com.chatapplication.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatapplication.chatapp.model.Messages;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, Long>{

}
