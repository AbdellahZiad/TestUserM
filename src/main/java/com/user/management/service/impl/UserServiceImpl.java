package com.user.management.service.impl;

import com.user.management.exception.APIException;
import com.user.management.model.Account;
import com.user.management.model.Country;
import com.user.management.model.UserApp;
import com.user.management.repository.AccountRepository;
import com.user.management.repository.CountryRepository;
import com.user.management.repository.UserRepository;
import com.user.management.service.UserService;
import com.user.management.service.dto.AccountDto;
import com.user.management.service.dto.UserDTO;
import com.user.management.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final CountryRepository countryRepository;

    @Autowired
    public UserServiceImpl(CountryRepository countryRepository,
                           AccountRepository accountRepository,
                           UserRepository userRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.countryRepository = countryRepository;
    }


    /**
     * Create Or update new user
     */

    @Override
    public UserApp saveUser(UserDTO userAppDto) {
        return createOrUpdateNewUser(userAppDto);

    }

    private UserApp createOrUpdateNewUser(UserDTO userAppDto) {
        UserApp newUser = new UserApp();
        newUser.setUserID(userAppDto.getId());
        newUser.setFirstName(userAppDto.getFirstName());
        newUser.setLastName(userAppDto.getLastName());
        newUser.setEmail(userAppDto.getEmail());
        newUser.setActive(userAppDto.isActive());
        newUser.setCreateDate(new Date());
        newUser.setValidUntil(userAppDto.getValidUntil());
        newUser.setAge(userAppDto.getAge());

        Country country = null;

        if (userAppDto.getCountry() != null && !userAppDto.getCountry().isEmpty())
            country = countryRepository
                    .existsByNameIgnoreCase(userAppDto.getCountry()) ?
                    countryRepository.findByNameIgnoreCase(userAppDto.getCountry()).orElse(null) :
                    countryRepository
                            .save(new Country(userAppDto.getCountry(), userAppDto.getCountry().toUpperCase()));


        newUser.setCountry(country);
        return userRepository.save(newUser);
    }


    @Override
    @Transactional
    public String deleteUser(Long id) {
        log.info("delete user id =  " + id);
        UserApp userApp = userRepository.findById(id).orElse(null);

        if (userApp == null) return "Error whit delete this user";

        userRepository.delete(userApp);
        return "User Deleted Successfully!!";


    }


    @Override
    public List<UserApp> getAllUsers() {
        return userRepository.findAll().stream().filter(UserApp::isActive).collect(toList());
    }


    @Override
    public UserApp getUserByEmail(String email, Boolean active) {
        return active ? userRepository.findOneByEmailIgnoreCaseAndActiveTrue(email) :
                userRepository.findOneByEmailIgnoreCase(email);
    }

    @Override
    public AccountDto createNewAccountForAUser(AccountDto accountDto, String email) {
        if (email != null && !userRepository.existsByEmailIgnoreCaseAndActiveTrue(email))
            throw new APIException("User with email @ " + email + " is not exist !!!");

        UserApp userApp = userRepository.findOneByEmailIgnoreCaseAndActiveTrue(email);
        if (isFranceCountry(userApp.getCountry()) && isAdults(userApp.getAge()))
            return createNewAccount(accountDto, userApp);

        log.error("Error please check that user's country " + email + " is not France or he is under 18 years old");

        throw new APIException("Error please check that user's country " + email + " is not France or he is under 18 years old");
    }

    @Override
    @Transactional
    public List<AccountDto> getListAccountsByUserEmail(String email) {
        if (!userRepository.existsByEmailIgnoreCaseAndActiveTrue(email))
            throw new APIException("User with email @ " + email + " is not exist !!!");

        UserApp userApp = userRepository.findOneByEmailIgnoreCaseAndActiveTrue(email);

        List<Account> accounts = accountRepository.findByUserApp(userApp);

        List<AccountDto> accountDtoList = new ArrayList<>();
        accounts.forEach(account -> accountDtoList.add(getAccountDto(account)));

        return accountDtoList;
    }

    private AccountDto createNewAccount(AccountDto accountDto, UserApp userApp) {

        log.info("create new account for user " + userApp.getEmail());

        Account account = new Account();
        account.setCodeAccount(accountDto.getCodeAccount());
        account.setDateCreation(new Date());
        account.setDescription(accountDto.getDescription());
        account.setUserApp(userApp);

        account = accountRepository.save(account);
        userApp.getAccounts().add(account);
        userRepository.save(userApp);

        return getAccountDto(account);
    }

    private UserDTO getUserDto(UserApp userApp) {
        UserDTO newUser = new UserDTO();
        newUser.setId(userApp.getUserID());
        newUser.setFirstName(userApp.getFirstName());
        newUser.setLastName(userApp.getLastName());
        newUser.setEmail(userApp.getEmail());
        newUser.setActive(userApp.isActive());
        newUser.setAge(userApp.getAge());
        newUser.setValidUntil(userApp.getValidUntil());
        newUser.setCreateDate(userApp.getCreateDate());
        newUser.setCountry(userApp.getCountry().getName());
        return newUser;
    }


    private AccountDto getAccountDto(Account account) {

        return AccountDto.builder()
                .codeAccount(account.getCodeAccount())
                .description(account.getDescription())
                .user(getUserDto(account.getUserApp()))
                .build();
    }

    private boolean isAdults(Integer age) {
        return age != null && age >= 18;
    }

    private boolean isFranceCountry(Country country) {
        return country != null && country.getCode().contains(Constants.FRANCE);
    }


}
