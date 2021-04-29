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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.user.management.repository.specification.SpecificationConfig.filterUser;
import static java.util.Optional.ofNullable;
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

    @Override
    public Page<UserApp> filterUserApp(UserDTO filter, Pageable pageable) {
        return doFilter(filter, pageable);
    }


    /**
     * Create Or update new user
     */

    @Override
    public UserDTO saveUser(UserDTO userAppDto) {

        log.info("save new user " + userAppDto.getEmail());

        if (userAppDto.getId() != null && userAppDto.getId() > 0)
            return createOrUpdateNewUser(userAppDto);
        else if (userRepository.findById(userAppDto.getId()).isPresent() && userRepository.existsByEmailIgnoreCaseAndActiveTrue(userAppDto.getEmail()))
            throw new APIException("User with email @ " + userAppDto.getEmail() + " is already exist !!!");

        return createOrUpdateNewUser(userAppDto);

    }

    private UserDTO createOrUpdateNewUser(UserDTO userAppDto) {
        UserApp newUser = new UserApp();
        newUser.setId(userAppDto.getId());
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
        return getUserDto(userRepository.save(newUser));
    }


    @Override
    @Transactional
    public String deleteUser(Long id) {
        log.info("delete user id =  " + id);

        UserApp userApp = userRepository.findById(id).orElse(null);

        if (userApp == null) return "Error whit delete this user";

        userRepository.desactivateUserById(id);
        return "User Deleted Successfully!!";


    }


    private Page<UserApp> doFilter(UserDTO filter, Pageable pageable) {
        return ofNullable(filter)
                .map(f -> userRepository.findAll(filterUser(f), pageable))
                .orElse(userRepository.findAll(pageable));
    }


    @Override
    public List<UserDTO> getAllUsers() {
        return getAllUsersDto(userRepository.findAll().stream().filter(UserApp::isActive).collect(toList()));
    }

    private List<UserDTO> getAllUsersDto(List<UserApp> userApps) {
        List<UserDTO> userDTOList = new ArrayList<>();
        userApps.forEach(userApp -> userDTOList.add(getUserDto(userApp)));

        return userDTOList;
    }


    @Override
    public UserDTO getUserByEmail(String email, Boolean active) {
        return active ? getUserDto(userRepository.findOneByEmailIgnoreCaseAndActiveTrue(email)) : getUserDto(userRepository.findOneByEmailIgnoreCase(email));
    }

    @Override
    public AccountDto createNewAccountForAUser(AccountDto accountDto ) {
        if (accountDto.getUserAppDto().getEmail()!=null && !userRepository.existsByEmailIgnoreCaseAndActiveTrue(accountDto.getUserAppDto().getEmail()))
            throw new APIException("User with email @ " + accountDto.getUserAppDto().getEmail() + " is not exist !!!");

        UserApp userApp = userRepository.findOneByEmailIgnoreCaseAndActiveTrue(accountDto.getUserAppDto().getEmail());
        if (isFranceCountry(userApp.getCountry()) && isAdults(userApp.getAge()))
            return createNewAccount(accountDto, userApp);

        log.error("Error please check that user's country " + accountDto.getUserAppDto().getEmail() + " is not France or he is under 18 years old");

        throw new APIException("Error please check that user's country " + accountDto.getUserAppDto().getEmail() + " is not France or he is under 18 years old");
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
        return getAccountDto(account);
    }

    public AccountDto getAccountDto(Account account) {

        return AccountDto.builder()
                .codeAccount(account.getCodeAccount())
                .description(account.getDescription())
                .userAppDto(getUserDto(account.getUserApp()))
                .build();
    }

    private UserDTO getUserDto(UserApp userApp) {
        UserDTO newUser = new UserDTO();
        newUser.setId(userApp.getId());
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

    private boolean isAdults(Integer age) {
        return age != null && age >= 18;
    }

    private boolean isFranceCountry(Country country) {
        return country != null && country.getCode().contains(Constants.FRANCE);
    }


}
