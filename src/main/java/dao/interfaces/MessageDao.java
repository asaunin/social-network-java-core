package dao.interfaces;

import model.Message;
import model.User;

import java.util.List;

public interface MessageDao extends Dao{

//    Optional<MessageDb> getById(long id);
    void addNew(User sender, User recipient, String text);
    List<Message> getAll(User userOne, User userTwo);

}
