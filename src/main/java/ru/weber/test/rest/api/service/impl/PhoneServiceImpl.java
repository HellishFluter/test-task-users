package ru.weber.test.rest.api.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.weber.test.rest.api.entity.Phone;
import ru.weber.test.rest.api.entity.exception.ForbiddenForUserException;
import ru.weber.test.rest.api.entity.exception.PhoneNotFoundException;
import ru.weber.test.rest.api.repository.PhoneRepository;
import ru.weber.test.rest.api.service.PhoneService;

@Log4j2
@AllArgsConstructor
@Service
public class PhoneServiceImpl implements PhoneService {
    private final PhoneRepository phoneRepository;

    @Transactional
    @Override
    public void updatePhone(Long phoneId, Long userId, String newPhone) {
        Phone phone = phoneRepository.findById(phoneId).orElseThrow(() -> new PhoneNotFoundException(phoneId));
        if(!phone.getUser().getId().equals(userId)) {
            throw new ForbiddenForUserException(userId);
        }
        phoneRepository.updatePhone(phoneId, newPhone);
        log.debug("Phone: {} was update, phoneId: ", newPhone, phoneId);

    }
}
