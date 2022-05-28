package ru.weber.test.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.weber.test.rest.api.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
