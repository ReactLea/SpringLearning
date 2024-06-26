package com.example.springsecurityLearning.LearningSpring.repository;


import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.springsecurityLearning.LearningSpring.Entity.User;


public interface UserRepository extends  MongoRepository<User, String>{

    Optional<User> findByUsername(String username);
    
   //User findAllByUsername(String username);
}
