package model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.sql.Date;

@Data
public class User implements Serializable{

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

}
