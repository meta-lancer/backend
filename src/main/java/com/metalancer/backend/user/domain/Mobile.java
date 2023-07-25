package com.metalancer.backend.user.domain;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static java.util.regex.Pattern.matches;
import static lombok.AccessLevel.PROTECTED;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class Mobile {

    private String phone;

    public Mobile(String phone) {
//        if (isEmpty(phone)) {
//            throw new EmptyParamException("phone must be provided");
//        }
//
//        if (!checkPhone(phone)) {
//            throw new InvalidParamException("Invalid phone address");
//        }

        this.phone = phone;
    }

    private boolean checkPhone(String address) {
        return matches("[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+", address);
    }

    public String number() {
        return phone;
    }
}
