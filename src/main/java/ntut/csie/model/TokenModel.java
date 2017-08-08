package ntut.csie.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Set;

@Entity
public class TokenModel {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

//    private Set<SubscriberModel> subscribers;

    @NotBlank
    private String token;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }
}
