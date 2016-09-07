package model;

import dao.beans.MessageBean;
import dao.jdbc.UserDaoImpl;
import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.io.Serializable;
import java.sql.Timestamp;

@Log4j
@Data
public class Message implements Serializable {

    private long id;
    private Timestamp date;
    private User sender;
    private User recipient;
    private String body;

    private static Message from(MessageBean messageBean, User sender, User recipient) {

        Message message = new Message();
        message.id = messageBean.getId();
        message.date = messageBean.getDate();
        message.body = messageBean.getBody();
        message.sender = sender;
        message.recipient = recipient;

        return message;

    }

    public static Message from(MessageBean messageBean, UserDaoImpl userDao) {
        User sender = userDao.getUserById(messageBean.getSender()).get();
        User recipient = userDao.getUserById(messageBean.getRecipient()).get();
        return from(messageBean, sender, recipient);
    }

}
