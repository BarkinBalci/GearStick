package com.gearstick;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Generator {
    public static Stream<Character> getRandomSpecialChars(int count) {
        Random random = new SecureRandom();
        IntStream specialChars = random.ints(count, 33, 45);
        return specialChars.mapToObj(data -> (char) data);
    }
    public static Stream<Character> getRandomNumbers(int count) {
        Random random = new SecureRandom();
        IntStream numberChars = random.ints(count, 48, 57);
        return numberChars.mapToObj(data -> (char) data);
    }
    public static Stream<Character> getRandomLowercaseAlphabets(int count) {
        Random random = new SecureRandom();
        IntStream alphabetChars = random.ints(count, 65, 90);
        return alphabetChars.mapToObj(data -> (char) data);
    }
    public static Stream<Character> getRandomUppercaseAlphabets(int count) {
        Random random = new SecureRandom();
        IntStream alphabetChars = random.ints(count, 97, 122);
        return alphabetChars.mapToObj(data -> (char) data);
    }
    public static String generateSecureRandomPassword() {
        Stream<Character> pwdStream = Stream.concat(getRandomNumbers(8), Stream.concat(getRandomSpecialChars(8), Stream.concat(getRandomLowercaseAlphabets(8), getRandomUppercaseAlphabets(8))));
        List<Character> charList = pwdStream.collect(Collectors.toList());
        Collections.shuffle(charList);
        String password = charList.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
        return password;
    }
}
