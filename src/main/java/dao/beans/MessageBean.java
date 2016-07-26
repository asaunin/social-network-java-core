package dao.beans;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MessageBean {

    private long id;
    private Timestamp date;
    private long sender;
    private long recipient;
    private String body;

}
