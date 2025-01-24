package otus.billing;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@AllArgsConstructor
//@EnableResourceServer
//@RequestMapping("api/users")
public class AccountController {

    private AccountService accountService;// = new AccountService();
//    private JwtService jwtService;

//    @Autowired
//    public AccountController(AccountService accountService){
//        this.accountService = accountService;
//    }


//    @Timed(value="user.create.time",description="time to create users",percentiles={0.5,0.95,0.99})
//    @PostMapping("/user")
//    public ResponseEntity<AccountDto> createUser(@RequestBody Account user){
//        AccountDto savedUser = accountService.createUser(user);
//        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
//    }

    @GetMapping("/account/{id}")
    public ResponseEntity<AccountDto> getAccpunt(
            @PathVariable("id") long id){
        AccountDto account = accountService.getAccount(id);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    // Build Update User REST API
    @PutMapping("/account/{id}")
    public ResponseEntity<AccountDto> updateUser(@PathVariable("id") long id,
                                                 @RequestParam double amount){
        AccountDto updatedAccount = accountService.updateAccount(id,amount);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    // Build Delete User REST API
    @DeleteMapping("/account/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") long id){
        accountService.deleteAccount(id);
        return new ResponseEntity<>("Account successfully deleted!", HttpStatus.OK);
    }

    @GetMapping("/health/")
    public String healthCheck(){
        return "OK";
    }

    @GetMapping("/")
    public String zeroPage(){
        return "It's zero page. Use '/health/' path ";
    }
}