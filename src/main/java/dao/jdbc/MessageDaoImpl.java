package dao.jdbc;

import dao.beans.MessageBean;
import dao.interfaces.MessageDao;
import model.Message;
import model.User;

import java.util.List;
import java.util.stream.Collectors;

@FunctionalInterface
public interface MessageDaoImpl extends MessageDao {

    String SQL_ADD_NEW =
            "INSERT INTO messages (sender, recipient, body) VALUES (?, ?, ?);";
    String SQL_GET_BY_USER =
            "SELECT id, date, sender, recipient, body FROM messages WHERE ((sender = ? and recipient = ?) or (sender = ? and recipient = ?)) ORDER BY date;";
    String SQL_GET_LAST =
            "SELECT id, date, sender, recipient, body " +
                    "FROM messages " +
                    "INNER JOIN ( SELECT MAX(id) AS most_recent_message_id " +
                    "               FROM messages " +
                    "               GROUP BY CASE WHEN sender > recipient THEN recipient ELSE sender END, " +
                    "                        CASE WHEN sender < recipient THEN recipient ELSE sender END " +
                    "             ) T ON T.most_recent_message_id = messages.id " +
                    "WHERE sender = ? OR recipient = ? " +
                    "ORDER BY date DESC;";


    @Override
    default void addNew(User sender, User recipient, String text) {
        insert(MessageBean.class, SQL_ADD_NEW, sender.getId(), recipient.getId(), text);
    }

    @Override
    default List<Message> getAll(User userOne, User userTwo) {

        final long userOneId = userOne.getId();
        final long userTwoId = userTwo.getId();
        final UserDaoImpl userDao = this::getConnection;

        List<MessageBean> messagesDb = (List<MessageBean>) selectCollection(MessageBean.class, SQL_GET_BY_USER, userOneId, userTwoId, userTwoId, userOneId);
        return messagesDb.stream()
                .map(m -> Message.from(m, userDao))
                .collect(Collectors.toList());

    }

    @Override
    default List<Message> getLast(User user) {

        final long userId = user.getId();
        final UserDaoImpl userDao = this::getConnection;

        List<MessageBean> messagesDb = (List<MessageBean>) selectCollection(MessageBean.class, SQL_GET_LAST, userId, userId);
        return messagesDb.stream()
                .map(m -> Message.from(m, userDao))
                .collect(Collectors.toList());

    }

}
