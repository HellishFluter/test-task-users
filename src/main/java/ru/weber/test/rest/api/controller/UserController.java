package ru.weber.test.rest.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.weber.test.rest.api.dto.UserData;
import ru.weber.test.rest.api.dto.UserUpdateDTO;
import ru.weber.test.rest.api.entity.User;
import ru.weber.test.rest.api.repository.specifications.UserSpecification;
import ru.weber.test.rest.api.service.PhoneService;
import ru.weber.test.rest.api.service.UserService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final PhoneService phoneService;

    private final UserData userData;

    @GetMapping("/info")
    public ResponseEntity<User> getInfo() {
        return new ResponseEntity<>(userService.getUserById(userData.getUserId()), HttpStatus.OK);
    }

    //TODO: доступ к getAll можно предоставлять не всем юзерам, однако в рамках данной тестовой задачи
    // роли не выделялись и этот вопрос не решался
    @GetMapping("")
    public ResponseEntity<List<User>> getAll(
            @RequestParam Integer page,
            @RequestParam(value = "page-size", defaultValue = "100", required = false) Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer age
    ) {
        UserSpecification specification = UserSpecification
                .builder()
                .name(name)
                .phone(phone)
                .email(email)
                .age(age)
                .build();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> pageUsers = userService.findAll(specification, pageable);
        return new ResponseEntity<>(pageUsers.getContent(), HttpStatus.OK);
    }


    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateDTO userDTO) {
        return new ResponseEntity<>(userService.updateUser(userData.getUserId(), userDTO), HttpStatus.OK);
    }

    @PatchMapping("/phone")
    public ResponseEntity<?> updatePhone(@RequestParam Long phoneId, @RequestBody String phoneNumber) {
        phoneService.updatePhone(phoneId, userData.getUserId(), phoneNumber);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/phone/new")
    public ResponseEntity<?> addPhone(@RequestBody String phoneNumber) {
        userService.addPhone(userData.getUserId(), phoneNumber);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/email")
    public ResponseEntity<?> updateEmail(@RequestBody String email) {
        userService.updateEmail(userData.getUserId(), email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("")
    public ResponseEntity<User> deleteUser() {
        userService.deleteUserById(userData.getUserId());
        return ResponseEntity.ok().build();
    }
}
