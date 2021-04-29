package com.user.management.controller;


import com.user.management.service.CountryService;
import com.user.management.service.dto.CountryDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/country")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }


    @ApiOperation(value = "Add or update country")
    @PostMapping("")
    ResponseEntity<?> saveCountry(@Valid @RequestBody CountryDto countryDto) {
        return ResponseEntity.ok(countryService.saveCountry(countryDto));
    }


    @ApiOperation(value = "Delete country")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteCountry(@PathVariable Long id) {

        return new ResponseEntity<String>(countryService.deleteCountry(id), HttpStatus.OK);
    }


    @ApiOperation(value = "GetAll Country")
    @GetMapping("")
    ResponseEntity<?> getAllCountry() {
        return ResponseEntity.ok(countryService.getAllCountry());
    }


}
