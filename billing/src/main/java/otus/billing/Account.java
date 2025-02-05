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
@NoArgsConstructor
@Entity
@Table(schema = "public")
public class Account {//} implements UserDetails {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private Long userId;
    private Double sum;


    public Account(Event event){
        this.id = event.getUserId();
        this.userId = event.getUserId();
        this.sum = event.getSum();
    }

}
