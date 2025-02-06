package otus.delivery;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@AllArgsConstructor
public class DeliveryController {

    private DeliveryService deliveryService;

    @GetMapping("/delivery/{id}")
    public ResponseEntity<DeliveryDto> getWare(@RequestPart Long id){
        DeliveryDto deliveryDto = deliveryService.getDelivery(id);
        return new ResponseEntity<>(deliveryDto, HttpStatus.CREATED);
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
        deliveryService.cleanAll();
        return "Delivery Cleaned";
    }
}