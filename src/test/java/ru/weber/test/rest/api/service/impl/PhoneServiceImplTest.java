package ru.weber.test.rest.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.weber.test.rest.api.entity.Phone;
import ru.weber.test.rest.api.entity.User;
import ru.weber.test.rest.api.entity.exception.ForbiddenForUserException;
import ru.weber.test.rest.api.entity.exception.PhoneNotFoundException;
import ru.weber.test.rest.api.repository.PhoneRepository;
import ru.weber.test.rest.api.service.PhoneService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class PhoneServiceImplTest {

    @MockBean
    private  PhoneRepository phoneRepository;

    private PhoneService phoneService;

    private Long USER_ID = 1L;
    private Long PHONE_ID = 2L;
    private String NEW_PHONE = "+71234567890";

    private Long USER_ID_BAD = 3L;

    @BeforeEach
    void setUp() {
        phoneService = new PhoneServiceImpl(phoneRepository);
    }

    @Test
    void updatePhone_Ok() {
        Phone phone = new Phone();
        phone.setId(PHONE_ID);
        phone.setUser(new User());
        phone.getUser().setId(USER_ID);

        when(phoneRepository.findById(PHONE_ID)).thenReturn(Optional.of(phone));
        phoneService.updatePhone(PHONE_ID, USER_ID, NEW_PHONE);
        verify(phoneRepository, times(1)).findById(PHONE_ID);
        verify(phoneRepository,  times(1)).updatePhone(PHONE_ID, NEW_PHONE);
        verifyNoMoreInteractions(phoneRepository);
    }

    @Test
    void updatePhone_dontCorrectUser() {
        Phone phone = new Phone();
        phone.setId(PHONE_ID);
        phone.setUser(new User());
        phone.getUser().setId(USER_ID_BAD);

        when(phoneRepository.findById(PHONE_ID)).thenReturn(Optional.of(phone));
        assertThrows(ForbiddenForUserException.class, () -> phoneService.updatePhone(PHONE_ID, USER_ID, NEW_PHONE));
        verify(phoneRepository, times(1)).findById(PHONE_ID);
        verifyNoMoreInteractions(phoneRepository);
    }

    @Test
    void updatePhone_phoneNotFound() {
        when(phoneRepository.findById(PHONE_ID)).thenReturn(Optional.empty());
        assertThrows(PhoneNotFoundException.class, () -> phoneService.updatePhone(PHONE_ID, USER_ID, NEW_PHONE));
        verify(phoneRepository, times(1)).findById(PHONE_ID);
        verifyNoMoreInteractions(phoneRepository);
    }
}