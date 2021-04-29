package com.user.management.repository;

import com.user.management.model.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserApp, Long>, JpaSpecificationExecutor<UserApp> {

    UserApp findOneByEmailIgnoreCase(String email);

    UserApp findOneByEmailIgnoreCaseAndActiveTrue(String email);

    boolean existsByEmailIgnoreCaseAndActiveTrue(String email);

    @Modifying
    @Query(value = "UPDATE user_app t SET t.active=false where t.id=:id")
    void desactivateUserById(@Param("id") Long id);



}
