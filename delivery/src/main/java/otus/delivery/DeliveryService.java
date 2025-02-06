package otus.delivery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otus.lib.event.Event;
import otus.lib.event.EventProducer;
import otus.lib.event.EventStatus;
import otus.lib.event.EventType;
import otus.lib.exception.ErrorType;
import otus.lib.exception.SrvException;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService implements DeliveryServiceInterface {

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

        //готовим событие к отправке
        event.setSource("delivery");
        event.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        event.setType(EventType.DELIVERING);
        event.setMessage(event.getType().getDescription());

        if((event.getWareId() != null)  && (event.getAmount() != null)
                && event.getUserId() != null && event.getOrderId() != null) {
            Delivery delivery = new Delivery(event);
            if(delivered(delivery)) {
                res = true;
            } else { // что-то пошло не так...
                    delivery.setStatus(EventStatus.ERROR);
                    event.setStatus(EventStatus.ERROR);
            }
            deliveryRepository.save(delivery);
        }else{event.setStatus(EventStatus.ERROR);}
        // + to Kafka
        eventProducer.sendMessage(event);
        return res;
    }

    public boolean delivered(Delivery delivery){
        return true;
    }

    public void cleanAll(){
        deliveryRepository.deleteAll();
    }

}
