package otus.billing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otus.lib.exception.ErrorType;
import otus.lib.exception.SrvException;
import otus.lib.event.Event;
import otus.lib.event.EventProducer;
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
        event.setSource("billing");
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
        accountDto.setSum(sum + accountDto.getSum());
        accountRepository.save(AccountMapper.mapToAccount(accountDto));
        return accountDto;
    }

    public AccountDto cleanAccount(long id){
        AccountDto accountDto = getAccount(id);
        accountDto.setSum(0);
        accountRepository.save(AccountMapper.mapToAccount(accountDto));
        return accountDto;
    }
    public void deleteAccount(long id){
        return;
    }

    public void cleanAll(){
        accountRepository.deleteAll();
    }

}
