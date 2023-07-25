package com.metalancer.backend.user.domain;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static java.util.regex.Pattern.matches;
import static lombok.AccessLevel.PROTECTED;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class Email {

    private String address;

    public Email(String address) {
//        if (isEmpty(address)) {
//            throw new EmptyParamException("email must be provided");
//        }
//        if (!checkAddress(address)) {
//            throw new InvalidParamException("Invalid email address");
//        }

        this.address = address;
    }

    private boolean checkAddress(String address) {
        return matches("[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+", address);
    }

    public String value() {
        return address;
    }
}

