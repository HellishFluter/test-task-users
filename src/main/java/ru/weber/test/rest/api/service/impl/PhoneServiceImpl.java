package ru.weber.test.rest.api.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.weber.test.rest.api.repository.PhoneRepository;
import ru.weber.test.rest.api.service.PhoneService;

@Log4j2
@AllArgsConstructor
@Service
public class PhoneServiceImpl implements PhoneService {
    private final PhoneRepository phoneRepository;

    @Transactional
    @Override
    public void updatePhone(Long id, String phone) {
        phoneRepository.updatePhone(id, phone);
        log.debug("Phone: {} was update, phoneId: ", phone, id);

    }
}
