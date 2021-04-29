package com.user.management.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 100, unique = true)
    private String codeAccount;

    private Date dateCreation;


    @Column
    @Lob
    private String description;

    @ManyToOne()
    private UserApp userApp;
}
