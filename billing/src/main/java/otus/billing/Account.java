package otus.billing;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import otus.lib.event.Event;
//import java.util.*;
//import org.springframework.security.*;

//import org.springframework.security.core.userdetails.UserDetails;


@Data
@AllArgsConstructor
//@NoArgsConstructor
@Entity
@Table(schema = "public")
public class Account {//} implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    private long userId;
    private double amount;

    public Account(Event event){
        this.userId = event.getUserId();
        this.amount = event.getAmount();
    }

}
