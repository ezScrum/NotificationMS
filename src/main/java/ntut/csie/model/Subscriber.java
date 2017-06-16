package ntut.csie.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Subscriber {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

//    private Set<TokenModel> tokens;

    @NotBlank
    private String username;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "token_relation", joinColumns = @JoinColumn(name = "subscriber_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "token_id", referencedColumnName = "id"))
//    public Set<TokenModel> getTokens() {
//        return tokens;
//    }
//
//    public void setTokens(Set<TokenModel> tokens) {
//        this.tokens = tokens;
//    }
}
