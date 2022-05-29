package ru.weber.test.rest.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.weber.test.rest.api.entity.Profile;
import ru.weber.test.rest.api.repository.ProfileRepository;
import ru.weber.test.rest.api.service.ProfileService;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ProfileServiceImplTest {
    @MockBean
    private ProfileRepository profileRepository;

    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        profileService = new ProfileServiceImpl(profileRepository);
    }

    @Test
    void increaseCash() {
        Profile profile = Profile.builder()
                .cash(BigDecimal.valueOf(100.00))
                .maxCash(BigDecimal.valueOf(207.00))
                .build();
        when(profileRepository.findAll()).thenReturn(Collections.singletonList(profile));
        profileService.increaseCash();
        verify(profileRepository).save(any());
    }
}