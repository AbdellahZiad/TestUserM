package com.user.management.repository;

import com.user.management.model.Account;
import com.user.management.model.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUserApp(UserApp userApp);

}
