package com.user.management.repository;


import com.user.management.model.Country;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends MongoRepository<Country, Long> {

    Optional<Country> findByNameIgnoreCase(String team);

    boolean existsByNameIgnoreCase(String team);


}
