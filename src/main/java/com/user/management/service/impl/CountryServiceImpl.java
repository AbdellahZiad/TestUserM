package com.user.management.service.impl;


import com.user.management.exception.APIException;
import com.user.management.model.Country;
import com.user.management.repository.CountryRepository;
import com.user.management.service.CountryService;
import com.user.management.service.dto.CountryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;


    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }


    @Override
    public Country saveCountry(CountryDto countryDto) {
        log.info("save new country " + countryDto.getName());
        if (countryDto.getId() == null || countryDto.getId() == 0) {
            countryRepository.findByNameIgnoreCase(countryDto.getName()).ifPresent(existingCountry -> {
                        throw new APIException("Country " + existingCountry.getName() + " is Already Exist !!!");
                    }
            );
        }
        Country country = new Country();
        country.setId(countryDto.getId());
        country.setCode(countryDto.getName());
        country.setName(countryDto.getName().toUpperCase());


        return countryRepository.save(country);
    }

    @Transactional
    @Override
    public String deleteCountry(Long id) {
        log.info("delete country id =  " + id);
        Country country = countryRepository.findById(id).orElse(null);
        if (country == null) return "Error with to delete this Country";

        countryRepository.deleteById(id);
        return "Country deleted successfully";
    }


    @Override
    public List<Country> getAllCountry() {
        return countryRepository.findAll();
    }

}
