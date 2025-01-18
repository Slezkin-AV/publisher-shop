package otus.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String login;
    private String password;

//    public UserDto() {}
//
//    public UserDto(Long id, String firstName, String lastName, String email, String login){
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.login = login;
//    }
//    public UserDto(String firstName, String lastName, String email){
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//    }
//
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
}