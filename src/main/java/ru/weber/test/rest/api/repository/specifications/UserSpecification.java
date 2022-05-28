package ru.weber.test.rest.api.repository.specifications;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestParam;
import ru.weber.test.rest.api.entity.Phone;
import ru.weber.test.rest.api.entity.Profile;
import ru.weber.test.rest.api.entity.User;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Builder
public class UserSpecification implements Specification<User> {

    private String name;
    private String phone;
    private String email;
    private Integer age;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Join<User, Phone> phoneJoin = root.join("phones", JoinType.LEFT);

        List<Predicate> filters = new ArrayList<>();

        if (null != name) {
            filters.add(criteriaBuilder.like(root.get("name"), name));
        }

        if (null != phone) {
            filters.add(phoneJoin.get("value").in(phone));
        }

        if (null != email) {
            filters.add(criteriaBuilder.equal(root.get("email"), email));
        }

        if (null != age) {
            filters.add(criteriaBuilder.equal(root.get("age"), age));
        }

        return criteriaBuilder.and(filters.toArray(new Predicate[0]));
    }
}
