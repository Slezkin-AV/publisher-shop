package otus.lib.event;

import lombok.Getter;

@Getter
public enum EventType {
    NONE(0,"No event"),
    USER_CREATE(1,"User created"),        //user
    ACCOUNT_CREATE(2,"Account created"),  //billing
    ACCOUNT_UPDATE(3,"Account updated"),  //billing
    ORDER_CREATED(4,"Order created"),     //order
    ACCOUNT_PAID(5,"Account paying"),     //billing
    ORDER_PAID(6,"Order paid"),           //order
    RESERVE_CREATING(7,"Reserve creating"),//ware
    ORDER_RESERVED(8,"Reserved"),         //order
    RESERVE_CANCELING(9,"Reserve canceling"),//ware
    DELIVERING(10,"Delivering"),           //delivery
    ORDER_DELIVERED(11,"Delivered"),       //order
    ACCOUNT_RETURN(12,"Payment returning"),//billing
    ORDER_CANCELED(13,"Order canceled"),   //order
    FINAL(100,"Order closed")
    ;

    EventType(int value, String description) {
        this.description = description;
        this.value = value;
    }
    private final String description;
    private final int value;

}
