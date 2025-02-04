package otus.ware;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@AllArgsConstructor
public class WareController {

    private WareService wareService;

    @GetMapping("/ware/{id}")
    public ResponseEntity<WareDto> getWare(@RequestPart Long id){
        WareDto wareDto = wareService.getWare(id);
        return new ResponseEntity<>(wareDto, HttpStatus.CREATED);
    }

    @GetMapping("/waretype/{id}")
    public ResponseEntity<WareTypeDto> getWareType(@RequestPart Long id){
        WareTypeDto wareTypeDto = wareService.getWareType(id);
        return new ResponseEntity<>(wareTypeDto, HttpStatus.CREATED);
    }

    @PostMapping("/ware")
    public ResponseEntity<WareDto> createWare(@RequestBody Ware ware){
        WareDto wareDto = wareService.createWare(ware);
        return new ResponseEntity<>(wareDto, HttpStatus.CREATED);
    }

    @PostMapping("/waretype")
    public ResponseEntity<WareTypeDto> createWareType(@RequestBody WareType wareType){
        WareTypeDto wareTypeDto = wareService.createWareType(wareType);
        return new ResponseEntity<>(wareTypeDto, HttpStatus.CREATED);
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
        wareService.cleanAll();
        return "Orders Cleaned";
    }
}