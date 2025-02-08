package otus.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otus.lib.jwt.JwtRequest;
import otus.lib.event.*;
import otus.lib.exception.*;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserRepositoryInterface userRepository;

    @Autowired
    private final EventProducer eventProducer;


    @Override
    public UserDto registerUser(User user){
        if( user.getLogin() == null || user.getLogin().isEmpty()){throw new SrvException(ErrorType.USR_LOGIN_EMPTY);}
        if( user.getEmail() ==null || user.getEmail().isEmpty()){throw new SrvException(ErrorType.USR_EMAIL_EMPTY);}
        if( !userRepository.findByEmail(user.getEmail()).isEmpty() ){throw new SrvException(ErrorType.USR_EMAIL_DUBLICATE);}
        if( !userRepository.findByLogin(user.getLogin()).isEmpty() ){throw new SrvException(ErrorType.USR_LOGIN_DUBLICATE);}
        User usr = userRepository.save(user);
        try {
            Event event = new Event(EventType.USER_CREATE, EventStatus.SUCCESS,
                    "user", EventType.USER_CREATE.getDescription(), usr.getId(), null, null, null,null,
                    Timestamp.valueOf(LocalDateTime.now()),
                    Timestamp.valueOf(LocalDateTime.now()),OrderStatus.NONE);
            eventProducer.sendMessage(event);
        }catch (RuntimeException ex){
            log.info(String.valueOf(ex));
        }
        return UserMapper.mapToUserDto(usr);
    }

    @Override
    public User loginUser(JwtRequest request){
        List<User> lst = userRepository.findByLogin(request.getLogin());
        if(lst.isEmpty() ){throw new SrvException(ErrorType.USR_NOT_FOUND);}
        User usr = lst.get(0);
        if( usr.getPassword() != null && request.getPassword() != null){ //сравнение если оба не null
            if (!(usr.getPassword().equals(request.getPassword()))) {
                throw new SrvException(ErrorType.USR_INCORRECT_PASSWORD);
            }
        }
        if( !(usr.getPassword() == null || usr.getPassword().isEmpty()) //сравнение если null или пустые
           && (request.getPassword() == null) || request.getLogin().isEmpty()) {
            throw new SrvException(ErrorType.USR_INCORRECT_PASSWORD);
        }
        return (usr);
    }

    @Override
    public UserDto createUser(User user){
        User usr = userRepository.save(user);
        return UserMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
//    @PostAuthorize("returnedObject.userId == principal.userId")
    public UserDto getUser(Long userId){
        User usr = userRepository.findById(userId).orElseThrow(() -> new SrvException(ErrorType.USR_NOT_FOUND));
        return UserMapper.mapToUserDto(usr);
    }

    @Override
    public UserDto updateUser(Long userId, @org.jetbrains.annotations.NotNull User user){
        if( user.getEmail() ==null || user.getEmail().isEmpty()){throw new SrvException(ErrorType.USR_EMAIL_EMPTY);}
        User usr = userRepository.findById(userId).orElseThrow(() -> new SrvException(ErrorType.USR_NOT_FOUND));

        usr.setFirstName(user.getFirstName());
        usr.setLastName(user.getLastName());
        usr.setEmail(user.getEmail());

        return UserMapper.mapToUserDto(userRepository.save(usr));
    }

    @Override
    public void deleteUser(Long userId){
        userRepository.findById(userId).orElseThrow(() -> new SrvException(ErrorType.USR_NOT_FOUND));
        userRepository.deleteById(userId);
        return;
    }

    public void cleanAll(){
        userRepository.deleteAll();
    }

//    public List<UserDto> getAllUsers(){
//        List<UserDto> ls = new ArrayList<UserDto>();
//        UserDto usr = new UserDto();
//        ls.add(usr);
//        return ls;
//    }

    //    public UserDto createUser(String firstName, String secondName, String email){
//        UserDto usr = new UserDto(firstName, secondName, email);
//        return usr;
//    }

}
