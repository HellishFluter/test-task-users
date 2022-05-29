package ru.weber.test.rest.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.weber.test.rest.api.config.SecurityConfig;
import ru.weber.test.rest.api.dto.UserData;
import ru.weber.test.rest.api.entity.User;
import ru.weber.test.rest.api.repository.specifications.UserSpecification;
import ru.weber.test.rest.api.service.JwtService;
import ru.weber.test.rest.api.service.PhoneService;
import ru.weber.test.rest.api.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
@ExtendWith(SpringExtension.class)
class UserControllerTest {

    private UserController userController;
    @MockBean
    private UserService userService;
    @MockBean
    private PhoneService phoneService;
    @MockBean
    private  UserDetailsService userDetailsService;
    @MockBean
    private  JwtService jwtService;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @MockBean
    private UserData userData;
    private Long USER_ID = 1L;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);

        userController = new UserController(userService, phoneService, userData);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getInfo() throws Exception {
        when(userData.getUserId()).thenReturn(USER_ID);
        when(userService.getUserById(USER_ID)).thenReturn(user);
        mockMvc.perform(get("/api/v1/users/info")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAll() throws Exception {
        when(userService.findAll(any(UserSpecification.class), any(Pageable.class))).thenReturn(Page.empty());
        mockMvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateUser() throws Exception {
        when(userData.getUserId()).thenReturn(USER_ID);
        when(userService.updateUser(any(), any())).thenReturn(new User());
        mockMvc.perform(put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"test\",\n" +
                                "    \"age\": 30,\n" +
                                "    \"email\": \"testf@mail.ru\",\n" +
                                "    \"phones\": [\n" +
                                "        \"+79224841234\"\n" +
                                "    ],\n" +
                                "    \"cash\": 20\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void updatePhone() throws Exception {
        when(userData.getUserId()).thenReturn(USER_ID);
        mockMvc.perform(patch("/api/v1/users/phone")
                        .param("phoneId","1")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content("+79221234567"))
                .andExpect(status().isOk());
    }

    @Test
    void addPhone() throws Exception {
        when(userData.getUserId()).thenReturn(USER_ID);
        mockMvc.perform(patch("/api/v1/users/phone/new")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content("+79221234567"))
                .andExpect(status().isOk());
    }

    @Test
    void updateEmail() throws Exception {
        when(userData.getUserId()).thenReturn(USER_ID);
        mockMvc.perform(patch("/api/v1/users/email")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content("test2@dd.rr"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser() throws Exception {
        when(userData.getUserId()).thenReturn(USER_ID);
        mockMvc.perform(delete("/api/v1/users"))
                .andExpect(status().isOk());
    }
}