package otus.billing;


import otus.lib.event.Event;

public interface AccountServiceInterface {
    AccountDto createAccount(Event event);
    AccountDto getAccount(long id);
    AccountDto updateAccount(long ig, double amount);
    void deleteAccount(long id);
}
