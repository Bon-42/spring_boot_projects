package com.passwordvalidation.service;

import org.springframework.stereotype.Service;

@Service
public class PasswordValidatorServiceImpl implements PasswordValidatorService {

    @Override
    public boolean validate(String password) {
        return isLengthValid(password) &&
                containsLowerCaseAndDigit(password) &&
                hasRepeatedSequence(password);
    }

    private boolean isLengthValid(String password) {
        int length = password.length();
        return length >= 5 && length <= 12;
    }

    private boolean containsLowerCaseAndDigit(String password) {
        var hasDigit = false;
        var hasChar = false;

        for (char c : password.toCharArray()) {
            if(Character.isDigit(c)){
                hasDigit = true;
            }
            else if(Character.isLowerCase(c)) {
                hasChar = true;
            }
            else{
                return false;
            }
        }
        if (!hasDigit || !hasChar){
            return false;
        }
        return true;
    }

    public static boolean hasRepeatedSequence(String password) {
        int length = password.length();

        // Iterate through each character in the string
        for (int i = 0; i < length; i++) {
            // Check for a repeating sequence starting from this character
            for (int seqLength = 1; seqLength * 2 + i <= length; seqLength++) {
                String sequence = password.substring(i, i + seqLength);
                String nextSequence = password.substring(i + seqLength, i + 2 * seqLength);

                // If a repeating sequence is found, return false
                if (sequence.equals(nextSequence)) {
                    return false;
                }
            }
        }
        return true;
    }
}
