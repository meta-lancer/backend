package com.metalancer.backend.common.utils;

import java.util.Random;
import java.util.stream.Collectors;

public class RandomStringGenerator {

    public String generateRandomString(int length) {
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz"
                + "0123456789";

        String randomString = random.ints(length, 0, characters.length())
                .mapToObj(characters::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());

        return randomString;
    }
}