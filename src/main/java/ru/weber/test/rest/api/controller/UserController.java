package ru.weber.test.rest.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.weber.test.rest.api.dto.UserNewDTO;
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
    private UserService userService;
    private PhoneService phoneService;

    @GetMapping("/id={id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

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

    @PostMapping("")
    public ResponseEntity<User> postUser(@RequestBody UserNewDTO userDTO) {
        return new ResponseEntity<>(userService.saveUser(userDTO), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestParam Long id, @RequestBody UserUpdateDTO userDTO) {//TODO: userId брать из авторизации когда будет
        return new ResponseEntity<>(userService.updateUser(id, userDTO), HttpStatus.OK);
    }

    @PatchMapping("/phone")
    public ResponseEntity<?> updatePhone(@RequestParam Long id, @RequestBody String phoneNumber) {//TODO: добавить проверку что ИД юзера
        phoneService.updatePhone(id, phoneNumber);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/phone/new")
    public ResponseEntity<?> addPhone(@RequestParam Long userId, @RequestBody String phoneNumber) {//TODO: userId брать из авторизации когда будет
        userService.addPhone(userId, phoneNumber);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/email")
    public ResponseEntity<?> updateEmail(@RequestParam Long userId, @RequestBody String email) {//TODO: userId брать из авторизации когда будет
        userService.updateEmail(userId, email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/id={id}")
    public ResponseEntity<User> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
}
