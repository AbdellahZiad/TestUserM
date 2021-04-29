package com.user.management.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.user.management.UserMApp;
import com.user.management.service.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserMApp.class})
@AutoConfigureMockMvc
public class UserMTest {

    private static final String DEFAULT_FIRSTNAME = "Ab";
    private static final String DEFAULT_LASTNAME = "TESTER";
    private static final String DEFAULT_EMAIL = "aziad@business.com";
    private static final Integer DEFAULT_AGE = 25;
    private static final String DEFAULT_COUNTRY = "FRANCE";


    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testGetAllUsersAndAllCountry() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/country"))
                .andExpect(status().isOk());
    }

    @Test
    public void testMethodArgumentNotValid() throws Exception {

        /**
         * test add user with invalid email
         */
        UserDTO userApp = createUserDTO();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String qUserJsonFormat = ow.writeValueAsString(userApp);

        mockMvc.perform(post("/api/user").content(qUserJsonFormat).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        /**
         * test add user with invalid age
         */
        userApp.setAge(0);
        String qUserJsonFormatAge = ow.writeValueAsString(userApp);

        mockMvc.perform(post("/api/user").content(qUserJsonFormatAge).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));

        /**
         * test add a valid user
         */

        userApp.setEmail(DEFAULT_EMAIL);
        userApp.setAge(DEFAULT_AGE);
        String newValidUser = ow.writeValueAsString(userApp);
        mockMvc.perform(post("/api/user").content(newValidUser).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }

    @Test
    public void testInternalServerError() throws Exception {
        /**
         * add user with age < 18
         */

        UserDTO userApp = createUserDTO();
        userApp.setAge(null);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String qUserJsonFormat = ow.writeValueAsString(userApp);

        mockMvc.perform(post("/api/user/account").content(qUserJsonFormat))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.message").value("error.http.500"))
                .andExpect(jsonPath("$.title").value("Internal Server Error"));
    }

    private UserDTO createUserDTO() {
        UserDTO newUser = new UserDTO();
        newUser.setFirstName(DEFAULT_FIRSTNAME);
        newUser.setLastName(DEFAULT_LASTNAME);
        newUser.setEmail(null);
        newUser.setActive(true);
        newUser.setAge(DEFAULT_AGE);
        newUser.setCountry(DEFAULT_COUNTRY);
        return newUser;
    }


}
