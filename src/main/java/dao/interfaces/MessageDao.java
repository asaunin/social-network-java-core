package dao.interfaces;

import model.Message;
import model.User;

import java.util.List;

public interface MessageDao extends Dao{

    void addNew(User sender, User recipient, String text);
    List<Message> getAll(User userOne, User userTwo);
    List<Message> getLast(User userOne);

}
