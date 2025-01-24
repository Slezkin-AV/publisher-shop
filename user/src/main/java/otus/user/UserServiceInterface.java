package otus.user;

import otus.lib.jwt.JwtRequest;

public interface UserServiceInterface {
    UserDto registerUser(User user);
    User loginUser(JwtRequest request);
    UserDto createUser(User user);
    UserDto getUser(Long id);
    UserDto updateUser(Long ig, User user);
    void deleteUser(Long id);
}
