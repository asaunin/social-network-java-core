package dao.jdbc;

import common.Private;
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
    String SQL_REMOVE_USER_MESSAGES =
            "DELETE FROM messages WHERE (sender = ? OR recipient = ?);";
    String SQL_GET_BY_USER =
            "SELECT id, date, sender, recipient, body FROM messages WHERE ((sender = ? and recipient = ?) or (sender = ? and recipient = ?)) ORDER BY date;";
    String SQL_GET_LAST =
            "SELECT id, date, sender, recipient, body "
                    .concat("FROM messages ")
                    .concat("INNER JOIN ( SELECT MAX(id) AS most_recent_message_id ")
                    .concat("FROM messages ")
                    .concat("   GROUP BY CASE WHEN sender > recipient THEN recipient ELSE sender END, ")
                    .concat("   CASE WHEN sender < recipient THEN recipient ELSE sender END ")
                    .concat(") T ON T.most_recent_message_id = messages.id ")
                    .concat("WHERE sender = ? OR recipient = ? ")
                    .concat("ORDER BY date DESC;");


    @Override
    default void addNew(User sender, User recipient, String text) {
        insert(MessageBean.class, SQL_ADD_NEW, sender.getId(), recipient.getId(), text);
    }

    @Private //Only for tests
    @Override
    default void removeUser(User user) {
        delete(SQL_REMOVE_USER_MESSAGES, user.getId(), user.getId());
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
