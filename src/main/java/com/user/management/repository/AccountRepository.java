package com.user.management.repository;

import com.user.management.model.Account;
import com.user.management.model.UserApp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends MongoRepository<Account, Long> {

    List<Account> findByUserApp(UserApp userApp);

}
