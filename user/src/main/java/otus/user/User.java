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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String password;

//    public User(String firstName, String lastName, @NonNull String login, @NonNull String email, String password){
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.login = login;
//        this.email = email;
//        this.password = password;
//    }
}
