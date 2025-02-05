package otus.lib.event;

import lombok.Getter;

@Getter
public enum EventType {
    USER_CREATE("User created"),
    ACCOUNT_CREATE("Account created"),
    ACCOUNT_UPDATE("Account updated"),
    ACCOUNT_PAID("Account paid"),
    ORDER_CREATED("Order created"),
    ORDER_PAID("Order paid"),
    ORDER_RESERVED("Reserved"),
    ORDER_DELIVERED("Delivered"),
    ORDER_CANCELED("Order canceled"),
    RESERVE_CREATING("Reserve creating"),
    DELIVERING("Delivering"),
    NONE("No event")
    ;

    EventType(String description) {
        this.description = description;
    }
    private String description;

}
