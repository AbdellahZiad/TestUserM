package com.user.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Country {
    @Id
    private String countryId;

    @Indexed(unique = true)
    private String name;

    private String code;

    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }


    @DBRef
    @JsonIgnore
    private List<UserApp> userApps = new ArrayList<>();
}
