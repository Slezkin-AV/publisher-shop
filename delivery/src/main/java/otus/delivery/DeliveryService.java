package otus.delivery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otus.lib.event.*;
import otus.lib.exception.ErrorType;
import otus.lib.exception.SrvException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;


@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService implements DeliveryServiceInterface {
    private final int fail_level = 7;

    private final DeliverRepositoryInterface deliveryRepository;

    @Autowired
    private final EventProducer eventProducer;

    @Override
    public DeliveryDto getDelivery(Long id){
        Delivery delivery = deliveryRepository.findById(id).orElseThrow(() -> new SrvException(ErrorType.ORD_NOT_FOUND));
        return DeliveryMapper.mapToWareDto(delivery);
    }

    public boolean deliverWare(Event event){
        boolean res = false;
        EventType eventType = EventType.NONE;
        EventStatus eventStatus = EventStatus.ERROR;

        switch (event.getType()){
            case RESERVE_CREATING -> { //from "ware"
                if (event.getStatus() == EventStatus.SUCCESS) { //есть порох...
                    eventType = EventType.DELIVERING;
                    if (delivered(event)) {
                        eventStatus = EventStatus.SUCCESS;
                    }
                }
            }
        }

        if(eventType != EventType.NONE) {

            //готовим событие к отправке
            event.setSource("delivery");
            event.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            event.setStatus(eventStatus);
            event.setType(eventType);
            event.setMessage(event.getType().getDescription());

            deliveryRepository.save(new Delivery(event)); //фиксируем итоги доставки

            // + to Kafka
            eventProducer.sendMessage(event);
        }
        return res;
    }

    public boolean delivered(Event event){
        Random rand = new Random();
        int rr = rand.nextInt(10);
        return event.getUserId() % 2 != 0;
    }

    public void cleanAll(){
        deliveryRepository.deleteAll();
    }

}
