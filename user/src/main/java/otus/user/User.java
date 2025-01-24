package otus.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "public")
public class User {//} implements UserDetails {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String password;

//    public User(){}
//
//    public User(Long id, String firstName, String lastName, String email, String login) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.login = login;
//
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
}
