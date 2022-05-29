package ru.weber.test.rest.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.weber.test.rest.api.dto.UserNewDTO;
import ru.weber.test.rest.api.dto.UserUpdateDTO;
import ru.weber.test.rest.api.entity.Phone;
import ru.weber.test.rest.api.entity.User;
import ru.weber.test.rest.api.entity.UserCredential;
import ru.weber.test.rest.api.entity.exception.UserNotFoundException;
import ru.weber.test.rest.api.repository.PhoneRepository;
import ru.weber.test.rest.api.repository.UserCredentialRepository;
import ru.weber.test.rest.api.repository.UserRepository;
import ru.weber.test.rest.api.repository.specifications.UserSpecification;
import ru.weber.test.rest.api.service.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserCredentialRepository userCredentialRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private PhoneRepository phoneRepository;
    @MockBean
    private ModelMapper modelMapper;

    private Long USER_ID = 1L;


    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userCredentialRepository, passwordEncoder, phoneRepository, modelMapper);
    }

    @Test
    void getUserById_Ok() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(new User()));
        userService.getUserById(USER_ID);
        verify(userRepository, times(1)).findById(USER_ID);
        verifyNoMoreInteractions(userRepository);
    }
    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(USER_ID));
        verify(userRepository, times(1)).findById(USER_ID);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findAll() {
        when(userRepository.findAll(any(UserSpecification.class), any(Pageable.class))).thenReturn(Page.empty());
        userService.findAll(UserSpecification.builder().name("name").build(), PageRequest.of(1, 10));
        verify(userRepository, times(1)).findAll(any(UserSpecification.class), any(Pageable.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void saveUser() {
        when(userRepository.save(any())).thenReturn(new User());
        UserNewDTO userNewDTO = new UserNewDTO();
        userNewDTO.setCash(BigDecimal.valueOf(10));
        userNewDTO.setPhones(new String[]{"+79221234567"});
        when(modelMapper.map(userNewDTO, User.class)).thenReturn(new User());
        when(modelMapper.map(userNewDTO, UserCredential.class)).thenReturn(new UserCredential());

        userService.saveUser(userNewDTO);
        verify(modelMapper, times(2)).map(any(), any());
        verify(userRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(any());
        verify(userCredentialRepository, times(1)).save(any());
    }

    @Test
    void updateUser_userNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        UserUpdateDTO userDto = new UserUpdateDTO();

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(USER_ID, userDto));
        verify(userRepository, times(1)).findById(USER_ID);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(phoneRepository);
    }

    @Test
    void updateUser_eqQuantityPhones() {
        User user = new User();
        user.setPhones(new ArrayList<>());
        user.getPhones().add(Phone.builder().value("+78945656565").build());
        user.getPhones().add(Phone.builder().value("+78945656566").build());
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        UserUpdateDTO userDto = new UserUpdateDTO();
        userDto.setPhones(new String[]{"+78945656567", "+78945656568"});

        userService.updateUser(USER_ID, userDto);
        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, times(1)).save(any());
        verifyNoInteractions(phoneRepository);
    }

    @Test
    void updateUser_morePhones() {
        User user = new User();
        user.setPhones(new ArrayList<>());
        user.getPhones().add(Phone.builder().value("+78945656565").build());
        user.getPhones().add(Phone.builder().value("+78945656566").build());
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        UserUpdateDTO userDto = new UserUpdateDTO();
        userDto.setPhones(new String[]{"+78945656567", "+78945656568", "+78945656569"});

        userService.updateUser(USER_ID, userDto);
        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, times(1)).save(any());
        verifyNoInteractions(phoneRepository);
    }

    @Test
    void updateUser_lessPhones() {
        User user = new User();
        user.setPhones(new ArrayList<>());
        user.getPhones().add(Phone.builder().value("+78945656565").build());
        user.getPhones().add(Phone.builder().value("+78945656566").build());
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        UserUpdateDTO userDto = new UserUpdateDTO();
        userDto.setPhones(new String[]{"+78945656567"});

        userService.updateUser(USER_ID, userDto);
        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, times(1)).save(any());
        verify(phoneRepository, times(1)).delete(any());
    }

    @Test
    void addPhone_Ok() {
        User user = new User();
        user.setPhones(new ArrayList<>());
        user.getPhones().add(Phone.builder().value("+78945656565").build());
        user.getPhones().add(Phone.builder().value("+78945656566").build());
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        userService.addPhone(USER_ID, "+71234567890");
        verify(userRepository, times(1)).findById(USER_ID);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void addPhone_userNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.addPhone(USER_ID, "+71234567890"));
        verify(userRepository, times(1)).findById(USER_ID);
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    void updateEmail() {
        userService.updateEmail(USER_ID, "dd@dd.rr");
        verify(userRepository, times(1)).updateEmail(USER_ID, "dd@dd.rr");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUserById_ok() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        userService.deleteUserById(USER_ID);
        verify(userCredentialRepository, times(1)).deleteByUserId(USER_ID);
        verify(userRepository, times(1)).existsById(USER_ID);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUserById_userNotFound() {
        when(userRepository.existsById(USER_ID)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(USER_ID));
        verify(userRepository, times(1)).existsById(USER_ID);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userCredentialRepository);
    }
}