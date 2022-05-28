package ru.weber.test.rest.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.weber.test.rest.api.dto.UserNewDTO;
import ru.weber.test.rest.api.dto.UserUpdateDTO;
import ru.weber.test.rest.api.entity.User;
import ru.weber.test.rest.api.repository.specifications.UserSpecification;

public interface UserService {
    User getUserById(Long id);

    Page<User> findAll(UserSpecification specification, Pageable pageable);

    User saveUser(UserNewDTO userDTO);

    void addPhone(Long userId, String phoneNumber);

    void updateEmail(Long userId, String email);

    User updateUser(Long id, UserUpdateDTO userDTO);

    void deleteUserById(Long id);
}

