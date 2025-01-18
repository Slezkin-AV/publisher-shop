package otus.user;

import lombok.Getter;

@Getter
public enum UserErrorType {
    ERR_DUPLICATE("User already exists"),
    ERR_NOT_FOUND("User not found"),
    ERR_NOT_LOGGED("User not logged"),
    ERR_LOGIN_EMPTY("Login cannot be empty"),
    ERR_EMAIL_EMPTY("Email cannot be empty"),
    ERR_LOGIN_DUBLICATE("Login already exists"),
    ERR_EMAIL_DUBLICATE("Email already exists"),
    ERR_INCORRECT_PASSWORD("Password is incorrect");

    UserErrorType(String description) {
        this.description = description;
    }
    private String description;
}
