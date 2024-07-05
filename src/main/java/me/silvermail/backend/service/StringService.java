package me.silvermail.backend.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class StringService {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";

    public String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
