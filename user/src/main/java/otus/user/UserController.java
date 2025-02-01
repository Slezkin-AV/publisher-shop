package otus.user;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import otus.lib.jwt.*;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@AllArgsConstructor
//@EnableResourceServer
//@RequestMapping("api/users")
public class UserController {

    private UserService userService;// = new UserService();
    private JwtService jwtService;

//    @Autowired
//    public UserController(UserService userService){
//        this.userService = userService;
//    }

    //register
    @Timed(value="user.register.time",description="time to register users",percentiles={0.5,0.95,0.99})
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody User user){
        UserDto savedUser = userService.registerUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    //login
    @Timed(value="user.login.time",description="time to login users",percentiles={0.5,0.95,0.99})
    @PostMapping("/login")
    public ResponseEntity<JwtResponce> loginUser(
            @RequestBody JwtRequest request){
        User savedUser = userService.loginUser(request);
        Map<String, Object> claims = new HashMap<String,Object>();
        claims.put("id",savedUser.getId().toString());
        claims.put("login", savedUser.getLogin());
        claims.put("email", savedUser.getEmail());
        JwtResponce responce = new JwtResponce(savedUser.getId(),jwtService.generateAccessToken(savedUser.getLogin(), claims));
        return new ResponseEntity<>(responce, HttpStatus.CREATED);
    }

    @Timed(value="user.create.time",description="time to create users",percentiles={0.5,0.95,0.99})
    @PostMapping("/user")
    public ResponseEntity<UserDto> createUser(@RequestBody User user){
        UserDto savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
//
//    @GetMapping("/user")
//    public String getUser(@AuthenticationPrincipal UserDetails userDetails) {
//        return userDetails.getUsername();
//    }
//    @GetMapping(value = "/username")
//    public String currentUserNameSimple(HttpServletRequest request) {
//        Principal principal = request.getUserPrincipal();
//        return principal.getName();
//    }

    @Timed(value="user.find.time",description="time to create users",percentiles={0.5,0.95,0.99})
    @GetMapping("/user/{id}")
//    @PostAuthorize("authentication.principal is not null")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable("id") Long userId){
//            @RequestHeader("Authorization") String bearerToken){
//        log.info("get");
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if( auth != null) {
//            log.info(auth.getPrincipal().toString(), auth.isAuthenticated());
//        }
//        if( !jwtService.validateId(userId, bearerToken)) throw new SrvException(ErrorType.ERR_NOT_FOUND);
        UserDto user = userService.getUser(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Build Update User REST API
    @Timed(value="user.update.time",description="time to create users",percentiles={0.5,0.95,0.99})
    @PutMapping("/user/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long userId,
                                              @RequestBody User user){
//        user.setId(userId);
        UserDto updatedUser = userService.updateUser(userId,user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // Build Delete User REST API
    @Timed(value="user.delete.time",description="time to create users",percentiles={0.5,0.95,0.99})
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>("User successfully deleted!", HttpStatus.OK);
    }

    @Timed("user.health_check.time")
    @GetMapping("/health/")
    public String healthCheck(){
        return "OK";
    }

    @GetMapping("/")
    public String zeroPage(){
        return "It's zero page. Use '/health/' path ";
    }

    @PostMapping("/clean")
    public String cleanAll(){
        userService.cleanAll();
        return "Users Cleaned";
    }


    //    // Build Get All Users REST API
//    // http://localhost:8080/api/users
//    @GetMapping("/users")
//    public ResponseEntity<List<UserDto>> getAllUsers(){
//        List<UserDto> users = userService.getAllUsers();
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }
}