package ntut.csie.model;


import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FilterModel {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long subscriberId;

    private String filter;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setSubscriberId(Long subscriberId){
        this.subscriberId = subscriberId;
    }

    public Long getSubscriberId(){
        return subscriberId;
    }

    public void setFilter(String filter){
        this.filter = filter;
    }

    public String getFilter(){
        return filter;
    }
}
