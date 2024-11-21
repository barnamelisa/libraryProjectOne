package model.validation;

import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    // ne vom folosi de regex-uri pt verificarea unui email
    private static final String EMAIL_VALIDATION_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final int MIN_PASSWORD_LENGTH = 8;
    private final List<String> errors; // lista de erori
    private final User user;
    public UserValidator(User user) {
        this.user = user;
        this.errors = new ArrayList<>();
    }

    public boolean validate() {
        validateUsername(user.getUsername());
        validatePassword(user.getPassword());

        return errors.isEmpty();
    }

    private void validateUsername(String username){
        if (!Pattern.compile(EMAIL_VALIDATION_REGEX).matcher(username).matches()){
            errors.add("Email is not valid!");
        }
    }

    private void validatePassword(String password){
        if (password.length() < MIN_PASSWORD_LENGTH){
            errors.add(String.format("Password must be at least %d characters long!", MIN_PASSWORD_LENGTH));
        }

        if (!containsSpecialCharacter(password)){
            errors.add("Password must contain at least one special character.");
        }

        if (!containsDigit(password)){
            errors.add("Password must contain at least one digit!");
        }
    }

    // "    abcd    " -> trim() -> "abcd"
    // "    ab cd    " -> trim() -> "ab cd"
    private boolean containsSpecialCharacter(String password){
        if (password == null || password.trim().isEmpty()){
            return false;
        }
        // black list-ce nu are voie sa treaca de verificare
        // in regex, ^=negatie
        Pattern specialCharactersPattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher specialCharactersMatcher = specialCharactersPattern.matcher(password);

        return specialCharactersMatcher.find();
    }

    private boolean containsDigit(String password){
        return Pattern.compile(".*[0-9].*").matcher(password).find();
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getFormattedErrors() {
        return String.join("\n", errors);
    }
}