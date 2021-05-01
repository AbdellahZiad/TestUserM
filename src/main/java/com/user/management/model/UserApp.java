package com.user.management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class UserApp implements java.io.Serializable {
    @Id
    private String userID;

    @Indexed(unique = true)
    @Email(message = "Please provide a valid e-mail")
    @NotEmpty(message = "Please provide an e-mail")
    private String email;


    private String firstName;

    private String lastName;

    private Date createDate;

    private Date validUntil;

    @NotNull
    @Positive
    private Integer age;

    private boolean active = true;

    @DBRef
    private Country country;

    @DBRef
    private List<Account> accounts = new ArrayList<>();

}
