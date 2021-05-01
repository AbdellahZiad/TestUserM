package com.user.management.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CountryDto {

    private String id;

    @NotNull
    private String name;

}
