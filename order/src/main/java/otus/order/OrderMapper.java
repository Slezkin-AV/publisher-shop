package otus.order;

public class OrderMapper {

    // Convert Order JPA Entity into OrderDto
    public static OrderDto mapToOrderDto(Order order) {
        //случайное замедление
//        Random random = new Random();
//        int randomNumber = random.nextInt(1000);  // вернёт случайное число от 0 до 999
//        try{
//            Thread.sleep(100 + randomNumber);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //конец случайного замедления

        return new OrderDto(
                order.getId(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getOrderStatus(),
                order.getUserId(),
                order.getAmount(),
                order.getSum()
        );
    }

    // Convert OrderDto into Order JPA Entity
    public static Order mapToOrder(OrderDto orderDto) {
        return new Order(
                orderDto.getId(),
                orderDto.getCreatedAt(),
                orderDto.getUpdatedAt(),
                orderDto.getOrderStatus(),
                orderDto.getUserId(),
                orderDto.getAmount(),
                orderDto.getSum()
        );
    }
}

