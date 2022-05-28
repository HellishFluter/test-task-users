package ru.weber.test.rest.api.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.weber.test.rest.api.dto.UserUpdateDTO;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**Имя*/
    @NotNull
    @Column(name = "NAME")
    private String name;

    /**Возраст, лет*/
    @Column(name = "AGE")
    private Integer age;

    /**E-mail*/
    @NotNull
    @Email
    @Column(name = "EMAIL")
    private String email;

    /**Телефоны*/
    @NotNull
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Phone> phones;

    /**Профиль*/
    @NotNull
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Profile profile;

    public void update(UserUpdateDTO userDTO) {
        if (userDTO.getName() != null) {
            this.name = userDTO.getName();
        }
        if (userDTO.getAge() != null) {
            this.age = userDTO.getAge();
        }
        if (userDTO.getEmail() != null) {
            this.email = userDTO.getEmail();
        }
        if(userDTO.getPhones() != null && userDTO.getPhones().length > 0) {
            List<Long> oldPhonesIds = this.getPhones().stream().map(Phone :: getId).collect(Collectors.toList());
            this.setPhones(new ArrayList<>());
            Arrays.stream(userDTO.getPhones()).forEach(phone -> {
                this.getPhones().add(Phone.builder()
                        .value(phone)
                        .user(this)
                        .build());
            });
            for (int i = 0; i < Math.min(oldPhonesIds.size(), userDTO.getPhones().length); i++) {
                this.getPhones().get(i).setId(oldPhonesIds.get(i));
            }
        }

    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", phones=" + phones +
                ", profile=" + profile +
                '}';
    }
}
