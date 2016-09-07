package dao.interfaces;

import model.Message;
import model.User;

import java.util.Collection;

public interface MessageDao extends Dao{

    void addNew(User sender, User recipient, String text);
    void removeUser(User user);
    Collection<Message> getAll(User userOne, User userTwo);
    Collection<Message> getLast(User userOne);

}
