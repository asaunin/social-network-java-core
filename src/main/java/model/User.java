package model;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private long id;
    private String first_name;
    private String last_name;
    private Date birth_date;
    private Date reg_date;
    private String email;
    private String password;
    private String phone;
}
