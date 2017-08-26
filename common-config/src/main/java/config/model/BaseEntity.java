package config.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Created by truongnguyen on 7/19/17.
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    protected Long id;

    public BaseEntity() {
    }

    public Long getId() {
        return id;
    }
}
