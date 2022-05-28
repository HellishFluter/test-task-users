package ru.weber.test.rest.api.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.weber.test.rest.api.entity.Profile;
import ru.weber.test.rest.api.repository.ProfileRepository;
import ru.weber.test.rest.api.service.ProfileService;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Log4j2
@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;

    @Transactional
    @Override
    public void increaseCash() {
        List<Profile> profiles = profileRepository.findAll();
        profiles.forEach(profile -> {
            if (profile.getMaxCash().compareTo(profile.getCash()) == 1) {
                profile.setCash(profile.getCash().multiply(BigDecimal.valueOf(1.07)));
                if (profile.getMaxCash().compareTo(profile.getCash()) == -1) {
                    profile.setCash(profile.getMaxCash());
                }
            }
            profileRepository.save(profile);
        });
        log.debug("All cash was increased");
    }

    @Scheduled(fixedRate = 20000)
    protected void autoIncreaseCash() {
        increaseCash();
    }
}
