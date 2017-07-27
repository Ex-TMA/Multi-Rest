package account.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created by truongnguyen on 7/18/17.
 */
@Entity
public class Access extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Role access;

    public Access() {
    }

    public Access(Role access) {
        this.access = access;
    }

    public Role getAccess() {
        return access;
    }

    public void setAccess(Role access) {
        this.access = access;
    }
    @Override
    public String toString(){
        return String.format("Access[id=%d, access=%s]",id, access);
    }
}
