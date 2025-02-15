package otus.ware;

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
public class WareService implements WareServiceInterface {

    private final WareRepositoryInterface wareRepository;
    private final WareTypeRepositoryInterface wareTypeRepository;

    @Autowired
    private final EventProducer eventProducer;

    @Override
    public WareDto getWare(Long id){
//        Ware ware = wareRepository.findById(id).orElseThrow(() -> new SrvException(ErrorType.ORD_NOT_FOUND));
        Ware ware = wareRepository.findById(id).orElse(new Ware());// .orElseThrow(() -> new SrvException(ErrorType.ORD_NOT_FOUND));
        return WareMapper.mapToWareDto(ware);
    }

    @Override
    public WareTypeDto getWareType(Long id){
        WareType wareType = wareTypeRepository.findById(id).orElseThrow(() -> new SrvException(ErrorType.ORD_NOT_FOUND));
        return WareTypeMapper.mapToWareTypeDto(wareType);
    }


    @Override
    public WareDto createWare(Ware ware){
        Ware ware1 = null;
        try {
            ware1 = wareRepository.save(ware);
        }catch (RuntimeException ex){
            log.info(String.valueOf(ex));
        }
        assert ware1 != null;

        return WareMapper.mapToWareDto(ware1);
    }


    @Override
    public WareTypeDto createWareType(WareType wareType){
        WareType wareType1 = null;
        try {
            wareType1 = wareTypeRepository.save(wareType);
        }catch (RuntimeException ex){
            log.info(String.valueOf(ex));
        }
        assert wareType1 != null;

        return WareTypeMapper.mapToWareTypeDto(wareType1);
    }


    public boolean reserveWare(Event event){
        boolean res = false;

        EventType eventType = EventType.NONE;
        EventStatus eventStatus = EventStatus.ERROR;

        if( event.getWareId() == null) {
            return false;
        }
        WareDto wareDto = getWare(event.getWareId());
        if(wareDto == null){
//            eventStatus = EventStatus.ERROR;
            eventType = EventType.RESERVE_CREATING;
        }else {

            switch (event.getType()) {
                case ACCOUNT_PAID -> {
                    if(event.getStatus() == EventStatus.SUCCESS) { //счет действительно оплачен
                        eventType = EventType.RESERVE_CREATING;
                        if(wareDto.getAmount() != null) {
                            long amount = wareDto.getAmount() - event.getAmount();
                            if (amount >= 0) { // есть еще порох в пороховницах - списываем и отправляем событие
                                wareDto.setAmount(amount);
                                wareRepository.save(WareMapper.mapToWare(wareDto));
                                res = true;
                                eventStatus = EventStatus.SUCCESS;
                            }
                        }
                    }
                }
                case DELIVERING -> { //получилась ли доставка
                    if(event.getStatus() == EventStatus.ERROR) { //дым - в трубу, дрова - в исходную
                        eventType = EventType.RESERVE_CANCELING;
                        long amount = 0L;
                        if (wareDto.getAmount() != null) {amount += wareDto.getAmount();}
                        amount +=event.getAmount();
                        wareDto.setAmount(amount);
                        wareRepository.save(WareMapper.mapToWare(wareDto));
                        res = true;
                        eventStatus = EventStatus.SUCCESS;
                    }
                }
                default -> {}
            }
        }

        if(eventType != EventType.NONE) { //готовим событие к отправке
            event.setSource("ware");
            event.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            event.setStatus(eventStatus);
            event.setType(eventType);
            event.setMessage(event.getType().getDescription());
            eventProducer.sendMessage(event);
        }
        return res;
    }

    public void cleanAll(){
        wareRepository.deleteAll();
        for(int i=0; i<10; i++){
            Ware ware = new Ware();
            ware.setWareName("Товар " + (i+1));
            ware.setAmount(50L);
            ware.setPrice(100.0);
            wareRepository.save(ware);
        }
    }

}
