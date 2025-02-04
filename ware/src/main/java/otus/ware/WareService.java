package otus.ware;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.Objects;


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
        Ware ware = wareRepository.findById(id).orElseThrow(() -> new SrvException(ErrorType.ORD_NOT_FOUND));
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
        WareDto wareDto = getWare(event.getWareId());

        //готовим событие к отправке
        event.setSource("ware");
        event.setWareId(wareDto.getId());

        long amount = wareDto.getAmount() - event.getAmount();
        if (amount >= 0) { // есть еще порох в пороховницах - списываем и отправляем событие
            wareDto.setAmount(amount);
            wareRepository.save(WareMapper.mapToWare(wareDto));
            event.setType(EventType.RESERVE_CREATE);
            eventProducer.sendMessage(event);
            res = true;
        }else { // отстатка на складе недостаточно, отменяем оплату и счет в целом
            event.setStatus(EventStatus.ERROR);
            event.setType(EventType.RESERVE_FAILED);
            eventProducer.sendMessage(event);
        }
        return res;
    }

    public void cleanAll(){
        wareRepository.deleteAll();
        wareTypeRepository.deleteAll();
        WareType wareType = new WareType();
        //формируем товары по умолчанию
        for( int i =0; i<10; i++){
            wareType.setWareName("Товар " + String.valueOf(i));
            wareType.setPrice(100 + i*10);
            wareTypeRepository.save(wareType);
        }
    }

}
