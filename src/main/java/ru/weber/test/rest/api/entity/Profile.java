package ru.weber.test.rest.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "PROFILES")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**Баланс*/
    @NotNull
    @Column(name = "CASH")
    private BigDecimal cash;

    /**Максимально возможный баланс*/
    @NotNull
    @Column(name = "MAX_CASH", updatable = false)
    private BigDecimal maxCash;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    @JoinColumn(name = "USER_ID")
    private User user;
}
