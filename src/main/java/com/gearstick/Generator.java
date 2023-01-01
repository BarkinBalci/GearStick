package com.gearstick;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Generator {
    public static Stream<Character> generateSpecial(int count, boolean bool) {
        Random random = new SecureRandom();
        IntStream specialChars = random.ints(count, 33, 45);
        if(!bool)specialChars = IntStream.empty();
        return specialChars.mapToObj(data -> (char) data);
    }
    public static Stream<Character> generateNumbers(int count, boolean bool) {
        Random random = new SecureRandom();
        IntStream numberChars = random.ints(count, 48, 57);
        if(!bool)numberChars = IntStream.empty();
        return numberChars.mapToObj(data -> (char) data);
    }
    public static Stream<Character> generateUppercase(int count, boolean bool) {
        Random random = new SecureRandom();
        IntStream uppercaseChars = random.ints(count, 65, 90);
        if(!bool)uppercaseChars = IntStream.empty();
        return uppercaseChars.mapToObj(data -> (char) data);
    }
    public static Stream<Character> generateLowercase(int count, boolean bool) {
        Random random = new SecureRandom();
        IntStream lowercaseChars = random.ints(count, 97, 122);
        if(!bool)lowercaseChars = IntStream.empty();
        return lowercaseChars.mapToObj(data -> (char) data);
    }
    public static String generatePassword(int lenght, boolean special, boolean number, boolean lowercase, boolean uppercase) {
        Stream<Character> pwdStream = Stream.concat(generateSpecial(lenght, special), Stream.concat(generateNumbers(lenght, number), Stream.concat(generateLowercase(lenght, lowercase), generateUppercase(lenght, uppercase))));
        List<Character> charList = pwdStream.collect(Collectors.toList());
        Collections.shuffle(charList);
        List<Character> sizedList = charList.subList(0, lenght);
        return sizedList.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }
}
