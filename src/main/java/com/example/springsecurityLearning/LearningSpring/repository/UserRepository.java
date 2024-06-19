package com.example.springsecurityLearning.LearningSpring.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.springsecurityLearning.LearningSpring.Entity.User;


public interface UserRepository extends  JpaRepository<User, Integer>{

    Optional<User> findByUsername(String username);
    
   //User findAllByUsername(String username);
}
