package otus.billing;

import java.util.Random;

public class AccountMapper {

    // Convert User JPA Entity into AccountDto
    public static AccountDto mapToAccountDto(Account account) {
        AccountDto accountDto = new AccountDto(
                account.getId(),
                account.getUserId(),
                account.getSum()
        );
//        //случайное замедление
//        Random random = new Random();
//        int randomNumber = random.nextInt(1000);  // вернёт случайное число от 0 до 999
//        try{
//            Thread.sleep(100 + randomNumber);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } //конец случайного замедления

        return accountDto;
    }

    // Convert AccountDto into User JPA Entity
    public static Account mapToAccount(AccountDto accountDto) {
        Account account = new Account(
                accountDto.getId(),
                accountDto.getUserId(),
                accountDto.getSum()
        );
        return account;
    }
}

