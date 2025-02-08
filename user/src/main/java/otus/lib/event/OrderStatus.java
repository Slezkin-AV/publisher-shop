package otus.lib.event;

import lombok.Getter;

@Getter
public enum OrderStatus {
    NONE("None"),
    CREATED("New order created"),
    PAID("Order paid"),
    RESERVED("Order shipped from wharehouse"),
    DELIVERED("Order delivered"),
    PAYBACK("Order paying returned"),
    REJECTED("Order rejected")
    ;

    OrderStatus(String description) {
        this.description = description;
    }
    private String description;
}
