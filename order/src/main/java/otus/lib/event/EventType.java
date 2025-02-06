package otus.lib.event;

import lombok.Getter;

@Getter
public enum EventType {
    USER_CREATE("User created"),        //user
    ACCOUNT_CREATE("Account created"),  //billing
    ACCOUNT_UPDATE("Account updated"),  //billing
    ACCOUNT_PAID("Account paid"),       //billing
    ACCOUNT_RETURN("Payment returning"),//billing
    ORDER_CREATED("Order created"),     //order
    ORDER_PAID("Order paid"),           //order
    ORDER_RESERVED("Reserved"),         //order
    ORDER_DELIVERED("Delivered"),       //order
    ORDER_CANCELED("Order canceled"),   //order
    RESERVE_CREATING("Reserve creating"),//ware
    DELIVERING("Delivering"),           //delivery
    NONE("No event")
    ;

    EventType(String description) {
        this.description = description;
    }
    private String description;

}
