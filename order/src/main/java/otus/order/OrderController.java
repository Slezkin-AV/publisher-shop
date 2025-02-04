package otus.order;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<OrderDto> createOrder(@RequestBody Order order){
        OrderDto savedOrder = orderService.createOrder(order);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderDto> getOrder(@RequestPart Long id){
        OrderDto savedOrder = orderService.getOrder(id);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }


    @GetMapping("/health/")
    public String healthCheck(){
        return "OK";
    }

    @GetMapping("/")
    public String zeroPage(){
        return "It's zero page. Use '/health/' path ";
    }

    @PostMapping("/clean")
    public String cleanAll(){
        orderService.cleanAll();
        return "Orders Cleaned";
    }
}