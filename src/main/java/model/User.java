package model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(exclude = {"name", "isuserfriend", "isfriendofuser"})
public class User implements Serializable, Cloneable {

    private long id;
    private String email;
    private String first_name;
    private String last_name;
    private String name;
    private String sex;
    private boolean isuserfriend;
    private boolean isfriendofuser;
    private Date birth_date;
    private Timestamp reg_date;
    private String phone;

    public User() {
    }

    public User(User user) {
        this.id = user.id;
        this.email = user.email;
        this.first_name = user.first_name;
        this.last_name = user.last_name;
        this.name = user.name;
        this.sex = user.sex;
        this.isuserfriend = user.isuserfriend;
        this.isfriendofuser = user.isfriendofuser;
        this.birth_date = user.birth_date;
        this.reg_date = user.reg_date;
        this.phone = user.phone;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
        this.name = this.first_name + " " + this.last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
        this.name = this.first_name + " " + this.last_name;
    }

}
