package ntut.csie.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Entity
public class TokenRelationModel {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long subscriberid;

    private Long tokenid;

    private boolean logon;

    public Long getId(){return id;}

    public void setId(Long id){this.id = id;}

    public Long getSubscriberId(){return subscriberid;}

    public void setSubscriberId(Long subscriberid){this.subscriberid = subscriberid;}

    public Long getTokenId(){return tokenid;}

    public void setTokenId(Long token_id){this.tokenid = token_id;}

    public boolean getLogon(){return logon;}

    public void setLogon(boolean logon){this.logon = logon;}
}
