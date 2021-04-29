package com.user.management.repository;


import com.user.management.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long>, JpaSpecificationExecutor<Country> {

    Optional<Country> findByNameIgnoreCase(String team);
    boolean existsByNameIgnoreCase(String team);


}
