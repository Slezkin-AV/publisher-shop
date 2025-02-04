package otus.lib.event;

import lombok.Getter;

@Getter
public enum EventType {
    USER_CREATE("User creating"),
    ACCOUNT_CREATE("Account creating"),
    ACCOUNT_PAID("Account paid"),
    ORDER_CREATED("Order created"),
    ORDER_PAID("Order paid"),
    ORDER_RESERVED("Reserved"),
    ORDER_DELIVERED("Delivered"),
    ORDER_CANCELED("Order canceled"),
    RESERVE_CREATE("Reserve sucsess"),
    RESERVE_FAILED("Reserve failed"),
    DELIVER_FAILED("Not delivered"),
    NONE("No event")
    ;

    EventType(String description) {
        this.description = description;
    }
    private String description;

}
