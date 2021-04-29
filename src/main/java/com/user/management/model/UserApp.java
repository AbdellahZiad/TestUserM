package com.user.management.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;


@Data
@Entity(name = "user_app")
public class UserApp implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Email(message = "Please provide a valid e-mail")
    @NotEmpty(message = "Please provide an e-mail")
    private String email;

    @Column(name = "firstName", length = 50)
    private String firstName;

    @Column(name = "lastName", length = 50)
    private String lastName;

    private Date createDate;

    private Date validUntil;

    @NotNull
    @Positive
    private Integer age;

    private boolean active = true;

    @ManyToOne(targetEntity = Country.class)
    private Country country;

    @OneToMany(mappedBy = "userApp")
    private List<Account> accounts;


}
