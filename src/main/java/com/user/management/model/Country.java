package com.user.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String code;

    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @OneToMany(mappedBy = "country")
    @JsonIgnore
    private List<UserApp> userApps;
}
