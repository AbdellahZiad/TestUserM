package com.user.management.service.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * A DTO representing a user.
 */
@Data
public class UserDTO {

    private Long id;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 4, max = 254)
    private String email;

    private boolean active;

    @NotNull
    private String country;

    private Date validUntil;

    private Date createDate;

    private Integer age;
}
