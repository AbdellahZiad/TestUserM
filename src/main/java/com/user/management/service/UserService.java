package com.user.management.service;


import com.user.management.model.UserApp;
import com.user.management.service.dto.AccountDto;
import com.user.management.service.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserApp saveUser(UserDTO userAppDto);

    String deleteUser(Long id);

    List<UserApp> getAllUsers();

    UserApp getUserByEmail(String email, Boolean active);

    AccountDto createNewAccountForAUser(AccountDto accountDto, String email);

    List<AccountDto> getListAccountsByUserEmail(String email);
}
