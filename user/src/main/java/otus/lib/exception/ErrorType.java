package otus.lib.exception;

import lombok.Getter;

@Getter
public enum ErrorType {
    USR_DUPLICATE("User already exists"),
    USR_NOT_FOUND("User not found"),
    USR_NOT_LOGGED("User not logged"),
    USR_LOGIN_EMPTY("Login cannot be empty"),
    USR_EMAIL_EMPTY("Email cannot be empty"),
    USR_LOGIN_DUBLICATE("Login already exists"),
    USR_EMAIL_DUBLICATE("Email already exists"),
    USR_INCORRECT_PASSWORD("Password is incorrect"),
    ACC_NOT_FOUND("Account not found"),
    ORD_NOT_FOUND("Order not found"),
    UNKNOWN_ERROR("Unknown Error");

    ErrorType(String description) {
        this.description = description;
    }
    private String description;
}
