package com.user.management.repository.specification;


import com.user.management.model.UserApp;
import com.user.management.model.UserApp_;
import com.user.management.service.dto.UserDTO;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Objects;

import static java.util.Optional.ofNullable;

public class SpecificationConfig {


    public static Specification<UserApp> filterUser(UserDTO userDto) {

        return Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Specification.where(ofNullable(userDto.getId()).map(item -> assertIsLikeUser(UserApp_.id, String.valueOf(item))).orElse(null)))
                .and(ofNullable(userDto.getEmail()).map(email -> assertIsLikeUser(UserApp_.email, email)).orElse(null)))
                .and(ofNullable(userDto.getFirstName()).map(fName -> assertIsLikeUser(UserApp_.firstName, fName)).orElse(null)))
                .and(ofNullable(userDto.getLastName()).map(lName -> assertIsLikeUser(UserApp_.lastName, lName)).orElse(null));


    }


    private static Specification<UserApp> assertIsLikeUser(SingularAttribute<UserApp, ?> attr, String user) {
        return (Root<UserApp> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
             cb.like(cb.lower(root.get(attr).as(String.class)), "%" + user.toLowerCase() + "%");

    }


}
