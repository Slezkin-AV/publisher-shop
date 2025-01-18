package otus.user;

import otus.jwt.JwtRequest;

public interface UserServiceInterface {
    UserDto registerUser(UserPub user);
    UserPub loginUser(JwtRequest request);
    UserDto createUser(UserPub user);
    UserDto getUser(Long id);
    UserDto updateUser(Long ig, UserPub user);
    void deleteUser(Long id);
}
