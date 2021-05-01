package com.user.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Account {

    @Id
    private String accountID;

    @Indexed(unique = true)
    private String codeAccount;

    private Date dateCreation;

    private String description;

    @DBRef
    @JsonIgnore
    private UserApp userApp;
}
