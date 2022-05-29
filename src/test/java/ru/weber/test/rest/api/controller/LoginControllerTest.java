package ru.weber.test.rest.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.weber.test.rest.api.config.SecurityConfig;
import ru.weber.test.rest.api.entity.User;
import ru.weber.test.rest.api.entity.UserCredential;
import ru.weber.test.rest.api.service.JwtService;
import ru.weber.test.rest.api.service.UserCredentialsService;
import ru.weber.test.rest.api.service.UserService;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
@Import(SecurityConfig.class)
@ExtendWith(SpringExtension.class)
class LoginControllerTest {
    private LoginController loginController;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserCredentialsService userCredentialsService;
    @MockBean
    private UserService userService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private  JwtService jwtService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        loginController = new LoginController(authenticationManager, userCredentialsService, userService, jwtService);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void authenticateUser() throws Exception {
        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(USER_ID, "token", new ArrayList<>()));
        UserCredential userCredential = new UserCredential();
        userCredential.setUser(new User());
        when(userCredentialsService.getByLogin(any())).thenReturn(userCredential);

        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{    \n" +
                                "    \"login\": \"admin\",\n" +
                                "    \"password\": \"admin\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void registerUser_200() throws Exception {
        when(userCredentialsService.existsByLogin(any())).thenReturn(false);
        when(userService.saveUser(any())).thenReturn(new User());

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"John Do2\",\n" +
                                "    \"age\": 31,\n" +
                                "    \"email\": \"john@sadfsd.r\",\n" +
                                "    \"phones\":[\"+79222885912\"],\n" +
                                "    \"cash\": 20,\n" +
                                "    \"login\": \"admin1\",\n" +
                                "    \"password\": \"admin\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void registerUser_400() throws Exception {
        when(userCredentialsService.existsByLogin(any())).thenReturn(true);
        when(userService.saveUser(any())).thenReturn(new User());

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"John Do2\",\n" +
                                "    \"age\": 31,\n" +
                                "    \"email\": \"john@sadfsd.r\",\n" +
                                "    \"phones\":[\"+79222885912\"],\n" +
                                "    \"cash\": 20,\n" +
                                "    \"login\": \"admin1\",\n" +
                                "    \"password\": \"admin\"\n" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}