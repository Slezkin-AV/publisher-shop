package otus.billing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otus.lib.event.*;
import otus.lib.exception.ErrorType;
import otus.lib.exception.SrvException;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService implements AccountServiceInterface {

    private final AccountRepositoryInterface accountRepository;

    @Autowired
    private final EventProducer eventProducer;


    public boolean processEvent(Event event){
        boolean res = false;

        EventType eventType = EventType.NONE;
        EventStatus eventStatus = EventStatus.ERROR;

        switch (event.getType()){
            case USER_CREATE -> {
                if(event.getStatus() == EventStatus.SUCCESS){
                    eventType = EventType.ACCOUNT_CREATE;
                    accountRepository.save(new Account(event));
                    eventStatus = EventStatus.SUCCESS;
                }
            }
            case ORDER_CREATED -> {
                if(event.getStatus() == EventStatus.SUCCESS){
                    eventType = EventType.ACCOUNT_PAID;
                    AccountDto accountDto = payAccount(event);
                    if(accountDto != null){eventStatus = EventStatus.SUCCESS;}
                }
            }
            case RESERVE_CREATING, DELIVERING -> {
                if(event.getStatus() == EventStatus.ERROR){
                    eventType = EventType.ACCOUNT_RETURN;
                    AccountDto accountDto = cashBackAccount(event);
                    if(accountDto != null){eventStatus = EventStatus.SUCCESS;}
                }
            }
//            case DELIVERING -> {}
            default -> {}
        }

        if(eventType != EventType.NONE) { //готовим событие к отправке
            event.setSource("billing");
            event.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            event.setStatus(eventStatus);
            event.setType(eventType);
            event.setMessage(event.getType().getDescription());
            eventProducer.sendMessage(event);
        }

        return res;
    }

    public AccountDto createAccount(Event event){
        Account account = new Account(event);
        accountRepository.save(account);

        // + to Kafka
        event.setSource("billing");
        event.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        event.setType(EventType.ACCOUNT_CREATE);
        event.setMessage(EventType.ACCOUNT_CREATE.getDescription());
        try {
            eventProducer.sendMessage(event);
        } catch( RuntimeException ex) {
            log.info(String.valueOf(ex));
        }
        return AccountMapper.mapToAccountDto(account);
    }

    public AccountDto getAccount(long id){
        Account account = accountRepository.findById(id).orElseThrow(() -> new SrvException(ErrorType.ACC_NOT_FOUND));
        return AccountMapper.mapToAccountDto(account);
    }

    public AccountDto updateAccount(long id, double sum){
        AccountDto accountDto = getAccount(id);
        if(accountDto.getSum() != null){
            sum += accountDto.getSum();
        }
        accountDto.setSum(sum);
        accountRepository.save(AccountMapper.mapToAccount(accountDto));

        // + to Kafka
        eventProducer.sendMessage(new Event(EventType.ACCOUNT_UPDATE, EventStatus.SUCCESS, "billing",
                EventType.ACCOUNT_UPDATE.getDescription(),
                id, null,null,null, sum,
                Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()), OrderStatus.NONE));

        return accountDto;
    }

    public AccountDto payAccount(Event event){

        AccountDto accountDto = getAccount(event.getUserId());


        if(event.getSum() != null && accountDto.getSum() != null){
            accountDto.setSum(accountDto.getSum() - event.getSum());
            if( accountDto.getSum() >= 0){
                accountRepository.save(AccountMapper.mapToAccount(accountDto));

            } else return null;
        } else return null;
        return accountDto;
    }

    public AccountDto cleanAccount(long id){
        AccountDto accountDto = getAccount(id);
        accountDto.setSum((double) 0);
        accountRepository.save(AccountMapper.mapToAccount(accountDto));
        return accountDto;
    }

    public void deleteAccount(long id){
        AccountDto accountDto = getAccount(id);
        if (accountDto.getSum() == null  || accountDto.getSum() == 0.0){
            accountRepository.deleteById(id);
        }
        return;
    }


    public AccountDto cashBackAccount(Event event){
        AccountDto accountDto = getAccount(event.getUserId());
        double sum = 0.0;
        if(event.getSum() != null){
            sum += event.getSum();
        } else {return null;}
        if(accountDto.getSum() != null) {
            sum += accountDto.getSum();
        }
        accountDto.setSum(sum);
        accountRepository.save(AccountMapper.mapToAccount(accountDto));

        return accountDto;
    }

    public void cleanAll(){
        accountRepository.deleteAll();
    }

}
