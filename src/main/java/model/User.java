package model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable{

    private long id;
    private String email;
    private String first_name;
    private String last_name;
    private String name;
    private String sex;
    private Date birth_date;
    private Date reg_date;
    private String phone;

}
