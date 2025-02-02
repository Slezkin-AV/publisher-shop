package otus.billing;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otus.lib.exception.ErrorType;
import otus.lib.exception.SrvException;
import otus.lib.event.Event;
import otus.lib.event.EventProducer;
import otus.lib.event.EventStatus;
import otus.lib.event.EventType;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService implements AccountServiceInterface {

    private final AccountRepositoryInterface accountRepository;

    @Autowired
    private final EventProducer eventProducer;


    public AccountDto createAccount(Event event){
        Account account = new Account(event);
        accountRepository.save(account);
        // + to Kafka
        try {
            Event event1 = new Event(EventType.ACCOUNT_CREATE, EventStatus.SUCCESS,
                    "billing","account created", account.getUserId(), -1,-1);
            eventProducer.sendMessage(event1);
        } catch( RuntimeException | JsonProcessingException ex) {
            log.info(String.valueOf(ex));
        }
        return AccountMapper.mapToAccountDto(account);
    }

    public AccountDto getAccount(long id){
        Account account = accountRepository.findById(id).orElseThrow(() -> new SrvException(ErrorType.ACC_NOT_FOUND));
        return AccountMapper.mapToAccountDto(account);
    }

    public AccountDto updateAccount(long ig, double amount){
        return new AccountDto();
    }

    public void deleteAccount(long id){
        return;
    }

    public void cleanAll(){
        accountRepository.deleteAll();
    }

}
