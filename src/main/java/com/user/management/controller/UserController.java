package com.user.management.controller;


import com.user.management.service.UserService;
import com.user.management.service.dto.AccountDto;
import com.user.management.service.dto.UserDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/api/user")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register or update a user
     */

    @ApiOperation(value = "add or update user")
    @PostMapping("")
    ResponseEntity<?> addUser(@Valid @RequestBody UserDTO userAppDto) {
        return ResponseEntity.ok(userService.saveUser(userAppDto));
    }


    /**
     * The 'active' parameter is a non-mandatory request parameter,
     * it is true by default if it is empty
     */

    @ApiOperation("Get user by email")
    @GetMapping("/mail")
    ResponseEntity<?> getUserByEmail(
            @Valid @Email @RequestParam String email,
            @RequestParam(value = "active", required = false, defaultValue = "true") Boolean active) {
        return ResponseEntity.ok(userService.getUserByEmail(email, active));


    }


    @ApiOperation(value = "Get All User")
    @GetMapping("")
    ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * delete user by id
     */

    @ApiOperation(value = "Delete User")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<String>(userService.deleteUser(id), HttpStatus.OK);

    }


    /**
     * Create account for a user
     */

    @ApiOperation("add Account")
    @PostMapping("/account")
    ResponseEntity<?> createAccountForAUser(
         @RequestBody AccountDto accountDto, @Valid @Email @RequestParam String email) {
        return ResponseEntity.ok(userService.createNewAccountForAUser(accountDto,email));


    }

    /**
     * Get List Of Accounts By Email User
     */

    @ApiOperation("get List Accounts")
    @GetMapping("/all/{email}")
    ResponseEntity<?> getListAccount(
            @PathVariable String email) {
        return ResponseEntity.ok(userService.getListAccountsByUserEmail(email));


    }


}
