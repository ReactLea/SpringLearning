package com.example.springsecurityLearning.LearningSpring.repository;
import com.example.springsecurityLearning.LearningSpring.Entity.Token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, String> {
//	     @Query("""
//			select t from token t inner join User u on t.user.id = u.id
//			where t.user.id = :userId and t.loggedOut = false
//			""")
			    List<Token> findAllAccessTokensByUser(String string);

			    Optional<Token> findByAccessToken(String token);

			    Optional<Token > findByRefreshToken(String token);
}
