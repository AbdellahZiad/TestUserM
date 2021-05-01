package com.user.management.repository;

import com.user.management.model.UserApp;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserApp, Long> {

    UserApp findOneByEmailIgnoreCase(String email);

    UserApp findOneByEmailIgnoreCaseAndActiveTrue(String email);

    boolean existsByEmailIgnoreCaseAndActiveTrue(String email);

}
