package model;

import dao.jdbc.MessageDb;
import dao.jdbc.UserDaoImpl;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;


@Data
public class Message {

    private long id;
    private Timestamp date;
    private User author;
    private String body;
    private String formatted_date;

    public static Message from(MessageDb messageDb, User sender) {

        Message message = new Message();
        message.id = messageDb.getId();
        message.date = messageDb.getDate();
        message.body = messageDb.getBody();
        message.author = sender;

        //Message date formatting
        LocalDateTime messageDate = message.date.toLocalDateTime();
        LocalDateTime currentDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        if (messageDate.compareTo(currentDate) > 0)
            message.formatted_date = messageDate.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        else
            message.formatted_date = messageDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));

        return message;

    }

    public static Message from(MessageDb messageDb, UserDaoImpl userDao) {

        return from(messageDb, userDao.getById(messageDb.getSender()).get());

    }

}
