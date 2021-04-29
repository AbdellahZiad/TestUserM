package com.user.management.service;


import com.user.management.model.UserApp;
import com.user.management.service.dto.AccountDto;
import com.user.management.service.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Page<UserApp> filterUserApp(UserDTO userAppDto, Pageable pageable);

    UserDTO saveUser(UserDTO userAppDto);

    String deleteUser(Long id);

    List<UserDTO> getAllUsers();

    UserDTO getUserByEmail(String email, Boolean active);

    AccountDto createNewAccountForAUser(AccountDto accountDto);

    List<AccountDto> getListAccountsByUserEmail(String email);
}
