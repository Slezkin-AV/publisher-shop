package otus.order;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED("New order created"),
    PAID("Order paid"),
    RESERVED("Order reserved in wharehouse"),
    DELIVERED("Order delivered"),
    REJECTED("ERROR"),
    NONE("No info")
    ;

    OrderStatus(String description) {
        this.description = description;
    }
    private String description;
}
