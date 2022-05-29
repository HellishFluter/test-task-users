package ru.weber.test.rest.api.dto;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import ru.weber.test.rest.api.entity.Phone;
import ru.weber.test.rest.api.entity.Profile;
import ru.weber.test.rest.api.entity.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

@Getter
@Setter
public class UserNewDTO extends UserUpdateDTO {
    private BigDecimal cash;
    private String login;
    private String password;

    public User getEntity(ModelMapper modelMapper) {
        User user = modelMapper.map(this, User.class);
        user.setProfile(Profile.builder()
                .cash(this.cash)
                .maxCash(this.cash.multiply(BigDecimal.valueOf(2.07)))
                .user(user)
                .build());
        user.setPhones(new ArrayList<>());
        Arrays.stream(this.getPhones()).forEach(phone -> user.getPhones().add(
                Phone.builder()
                        .value(phone)
                        .user(user)
                        .build()));
        return user;
    }
}
