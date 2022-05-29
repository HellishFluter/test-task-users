package ru.weber.test.rest.api.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.weber.test.rest.api.dto.UserNewDTO;
import ru.weber.test.rest.api.dto.UserUpdateDTO;
import ru.weber.test.rest.api.entity.Phone;
import ru.weber.test.rest.api.entity.User;
import ru.weber.test.rest.api.entity.UserCredential;
import ru.weber.test.rest.api.entity.exception.UserNotFoundException;
import ru.weber.test.rest.api.repository.UserCredentialRepository;
import ru.weber.test.rest.api.repository.UserRepository;
import ru.weber.test.rest.api.repository.PhoneRepository;
import ru.weber.test.rest.api.repository.specifications.UserSpecification;
import ru.weber.test.rest.api.service.UserService;

@Log4j2
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhoneRepository phoneRepository;
    private final ModelMapper modelMapper;

    @Cacheable(cacheNames = "user")
    @Transactional
    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        log.debug("User {} was got", user.toString());
        return user;
    }

    @Cacheable(cacheNames = "users")
    @Transactional
    @Override
    public Page<User> findAll(UserSpecification specification, Pageable pageable) {
        Page<User> users = userRepository.findAll(specification, pageable);
        log.debug("All users was got. Specification: {}. Pageable: {}", specification, pageable);
        return users;
    }

    @Transactional
    @Override
    public User saveUser(UserNewDTO userDTO) {
        User user = userDTO.getEntity(modelMapper);
        user = userRepository.save(user);

        UserCredential userCredential = modelMapper.map(userDTO, UserCredential.class);
        userCredential.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userCredential.setUser(user);
        userCredentialRepository.save(userCredential);

        log.debug("User {} was created", user.toString());
        return user;
    }

    @Transactional
    @Override
    public User updateUser(Long id, UserUpdateDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        log.debug("User {} was got for updating", user.toString());
        if(userDTO.getPhones() != null && userDTO.getPhones().length < user.getPhones().size()) {
            for (int i = user.getPhones().size(); i > userDTO.getPhones().length; i--) {
                phoneRepository.delete(user.getPhones().get(i-1));
            }
        }
        user.update(userDTO);
        user = userRepository.save(user);
        log.debug("User {} was updated", user.toString());
        return user;
    }

    @Transactional
    @Override
    public void addPhone(Long userId, String phoneNumber) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        user.getPhones().add(Phone
                .builder()
                .user(user)
                .value(phoneNumber)
                .build());
        log.debug("Phone: {} was add for user with id {}", phoneNumber, userId);
    }

    @Transactional
    @Override
    public void updateEmail(Long userId, String email) {
        userRepository.updateEmail(userId, email);
        log.debug("Email: {} was update for user with id {}", email, userId);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        if(userRepository.existsById(id)) {
            userCredentialRepository.deleteByUserId(id);
            log.debug("User with id: {} was deleted", id);
        } else {
            throw new UserNotFoundException(id);
        }
    }
}
