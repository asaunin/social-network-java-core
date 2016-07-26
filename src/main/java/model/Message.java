package model;

import dao.beans.MessageBean;
import dao.jdbc.UserDaoImpl;
import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;


@Log4j
@Data
public class Message implements Serializable {

    private long id;
    private Timestamp date;
    private User sender;
    private User recipient;
    private String body;
    private String formatted_date;

    public static Message from(MessageBean messageBean, User sender, User recipient) {

        Message message = new Message();
        message.id = messageBean.getId();
        message.date = messageBean.getDate();
        message.body = messageBean.getBody();
        message.sender = sender;
        message.recipient = recipient;

        //Message date formatting
        LocalDateTime messageDate = message.date.toLocalDateTime();
        LocalDateTime currentDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        if (messageDate.compareTo(currentDate) > 0)
            message.formatted_date = messageDate.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        else
            message.formatted_date = messageDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));

        return message;

    }

    public static Message from(MessageBean messageBean, UserDaoImpl userDao) {
        User sender = userDao.getById(messageBean.getSender()).get();
        User recipient = userDao.getById(messageBean.getRecipient()).get();
        return from(messageBean, sender, recipient);
    }

}
