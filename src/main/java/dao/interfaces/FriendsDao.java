package dao.interfaces;

import model.User;

import java.util.Optional;

public interface FriendsDao extends Dao {

    Optional<User> getById(long id, long profileId);
    void addFriend(User user, User friend);
    void removeFriend(User user, User friend);

}
