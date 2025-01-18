package otus.user;

import java.util.Random;

public class UserMapper {

    // Convert User JPA Entity into UserDto
    public static UserDto mapToUserDto(UserPub user) {
        UserDto userDto = new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getLogin(),
                user.getPassword()
        );
        //случайное замедление
        Random random = new Random();
        int randomNumber = random.nextInt(1000);  // вернёт случайное число от 0 до 999
        try{
            Thread.sleep(100 + randomNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } //конец случайного замедления

        return userDto;
    }

    // Convert UserDto into User JPA Entity
    public static UserPub mapToUser(UserDto userDto) {
        UserPub user = new UserPub(
                userDto.getId(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail(),
                userDto.getLogin(),
                userDto.getPassword()
        );
        return user;
    }
}

