package otus.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otus.exception.SrvException;
import otus.jwt.JwtRequest;
import otus.kafka.Event;
import otus.kafka.EventProducer;
import otus.kafka.EventStatus;
import otus.kafka.EventType;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserRepositoryInterface userRepository;

    @Autowired
    private final EventProducer eventProducer;

//    @Autowired
//    public UserService(UserRepositoryInterface userRepository){
//        this.userRepository = userRepository;
//    }

    @Override
    public UserDto registerUser(UserPub user){
        if( user.getLogin() == null || user.getLogin().isEmpty()){throw new SrvException(UserErrorType.ERR_LOGIN_EMPTY);}
        if( user.getEmail() ==null || user.getEmail().isEmpty()){throw new SrvException(UserErrorType.ERR_EMAIL_EMPTY);}
        if( !userRepository.findByEmail(user.getEmail()).isEmpty() ){throw new SrvException(UserErrorType.ERR_EMAIL_DUBLICATE);}
        if( !userRepository.findByLogin(user.getLogin()).isEmpty() ){throw new SrvException(UserErrorType.ERR_LOGIN_DUBLICATE);}
        eventProducer.sendMessage("ok", new Event(EventType.USER_CREATE, EventStatus.SUCCESS, "user", "ok"));
        return UserMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    public UserPub loginUser(JwtRequest request){
        List<UserPub> lst = userRepository.findByLogin(request.getLogin());
        if(lst.isEmpty() ){throw new SrvException(UserErrorType.ERR_NOT_FOUND);}
        UserPub usr = lst.getFirst();
        if( usr.getPassword() != null && request.getPassword() != null){ //сравнение если оба не null
            if (!(usr.getPassword().equals(request.getPassword()))) {
                throw new SrvException(UserErrorType.ERR_INCORRECT_PASSWORD);
            }
        }
        if( !(usr.getPassword() == null || usr.getPassword().isEmpty()) //сравнение если null или пустые
           && (request.getPassword() == null) || request.getLogin().isEmpty()) {
            throw new SrvException(UserErrorType.ERR_INCORRECT_PASSWORD);
        }
        return (usr);
    }

    @Override
    public UserDto createUser(UserPub user){
        UserPub usr = userRepository.save(user);
        return UserMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
//    @PostAuthorize("returnedObject.userId == principal.userId")
    public UserDto getUser(Long userId){
        UserPub usr = userRepository.findById(userId).orElseThrow(() -> new SrvException(UserErrorType.ERR_NOT_FOUND));
        return UserMapper.mapToUserDto(usr);
    }

    @Override
    public UserDto updateUser(Long userId, UserPub user){
        if( user.getEmail() ==null || user.getEmail().isEmpty()){throw new SrvException(UserErrorType.ERR_EMAIL_EMPTY);}
        UserPub usr = userRepository.findById(userId).orElseThrow(() -> new SrvException(UserErrorType.ERR_NOT_FOUND));

        usr.setFirstName(user.getFirstName());
        usr.setLastName(user.getLastName());
        usr.setEmail(user.getEmail());

        return UserMapper.mapToUserDto(userRepository.save(usr));
    }

    @Override
    public void deleteUser(Long userId){
        userRepository.findById(userId).orElseThrow(() -> new SrvException(UserErrorType.ERR_NOT_FOUND));
        userRepository.deleteById(userId);
        return;
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
