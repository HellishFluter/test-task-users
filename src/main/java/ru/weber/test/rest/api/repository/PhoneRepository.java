package ru.weber.test.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.weber.test.rest.api.entity.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    @Modifying
    @Query("update Phone p set p.value = :phone where p.id = :id")
    void updatePhone(Long id, String phone);
}
