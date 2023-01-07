package com.gearstick;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Generator {
    /**
     * Generates char in range of U+0021 and U+002D (!"#$%&'()*+,)
     */
    public static Stream<Character> generateSpecial(int count, boolean bool) {
        SecureRandom random = new SecureRandom();
        IntStream specialChars = random.ints(count, 33, 45);
        if (!bool)
            specialChars = IntStream.empty();
        return specialChars.mapToObj(data -> (char) data);
    }
    /**
     * Generates char in range of U+0030 and U+0039 (0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
     */
    public static Stream<Character> generateNumbers(int count, boolean bool) {
        SecureRandom random = new SecureRandom();
        IntStream numberChars = random.ints(count, 48, 57);
        if (!bool)
            numberChars = IntStream.empty();
        return numberChars.mapToObj(data -> (char) data);
    }

    /**
     * Generates char in range of U+0041 and U+005A (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z)
     */
    public static Stream<Character> generateUppercase(int count, boolean bool) {
        SecureRandom random = new SecureRandom();
        IntStream uppercaseChars = random.ints(count, 65, 90);
        if (!bool)
            uppercaseChars = IntStream.empty();
        return uppercaseChars.mapToObj(data -> (char) data);
    }
    /**
     * Generates char in range of U+0061 and U+007A (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z)
     */
    public static Stream<Character> generateLowercase(int count, boolean bool) {
        SecureRandom random = new SecureRandom();
        IntStream lowercaseChars = random.ints(count, 97, 122);
        if (!bool)
            lowercaseChars = IntStream.empty();
        return lowercaseChars.mapToObj(data -> (char) data);
    }

    public static String generatePassword(int length, boolean special, boolean number, boolean lowercase,
            boolean uppercase) {
        Stream<Character> pwdStream = Stream.concat(generateSpecial(length, special),
                Stream.concat(generateNumbers(length, number),
                        Stream.concat(generateLowercase(length, lowercase), generateUppercase(length, uppercase))));
        List<Character> charList = pwdStream.collect(Collectors.toList());
        Collections.shuffle(charList);
        List<Character> sizedList = charList.subList(0, length);
        return sizedList.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }
}
