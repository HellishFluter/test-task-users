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
public class UserUpdateDTO {
    private String name;
    private Integer age;
    private String email;
    private String[] phones;
}
