package otus.billing;


import otus.lib.event.Event;

public interface AccountServiceInterface {
    boolean processEvent(Event event);
    AccountDto createAccount(Event event);
    AccountDto getAccount(long id);
    AccountDto updateAccount(long ig, double amount);
    AccountDto cleanAccount(long ig);
    void deleteAccount(long id);
    AccountDto payAccount(Event event);
    AccountDto cashBackAccount(Event event);
}
