package otus.delivery;

public class DeliveryMapper {

    // Convert Delivery JPA Entity into DeliveryDto
    public static DeliveryDto mapToWareDto(Delivery delivery) {

        return new DeliveryDto(
                delivery.getId(),
                delivery.getStatusDescription()
        );
    }

    // Convert DeliveryDto into Delivery JPA Entity
    public static Delivery mapToWare(DeliveryDto deliveryDto) {
        return new Delivery(
                deliveryDto.getId(),
                deliveryDto.getStatusDescription()
        );
    }
}

