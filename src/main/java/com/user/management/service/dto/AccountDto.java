package com.user.management.service.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Builder
public class AccountDto {

    @Size(max = 100)
    private String codeAccount;

    private String description;

    private UserDTO userAppDto;

}
