package dao.jdbc;

import dao.interfaces.MessageDao;
import model.Message;
import model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@FunctionalInterface
public interface MessageDaoImpl extends MessageDao{

//    String SQL_GET_BY_ID =
//            "SELECT id, date, sender, recipient, body FROM messages WHERE id = ?";
    String SQL_GET_BY_USER =
            "SELECT id, date, sender, recipient, body FROM messages WHERE ((sender = ? and recipient = ?) or (sender = ? and recipient = ?)) ORDER BY date";
    String SQL_ADD_NEW =
            "INSERT INTO messages (sender, recipient, body) VALUES (?, ?, ?)";

//    @Override
//    default Optional<MessageDb> getById(long id) {
//        return select(MessageDb.class, SQL_GET_BY_ID, id);
//    }

    @Override
    default List<Message> getAll(User userOne, User userTwo) {
        final long userOneId = userOne.getId();
        final long userTwoId = userTwo.getId();
        UserDaoImpl userDao = (UserDaoImpl) this::getConnection;

        List<MessageDb> messagesDb = (List<MessageDb>) selectCollection(MessageDb.class, SQL_GET_BY_USER, userOneId, userTwoId, userTwoId, userOneId);
        return messagesDb.stream()
                .map(m -> Message.from(m, userDao))
                .collect(Collectors.toList());
    }

    @Override
    default void addNew(User sender, User recipient, String text) {
        Optional<MessageDb> messageDb = insert(MessageDb.class, SQL_ADD_NEW, sender.getId(), recipient.getId(), text);
    }
}
