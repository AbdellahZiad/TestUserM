package com.user.management.service;


import com.user.management.model.Country;
import com.user.management.service.dto.CountryDto;

import java.util.List;

public interface CountryService {


    Country saveCountry(CountryDto teamDto);

    String deleteCountry(Long i);

    List<Country> getAllCountry();


}
