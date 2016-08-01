package dao.jdbc;

import dao.beans.FriendsBean;
import dao.interfaces.FriendsDao;
import model.User;

import java.util.Optional;

@FunctionalInterface
public interface FriendsDaoImpl extends FriendsDao {

    String SQL_ADD_FRIEND =
            "INSERT INTO friends (userid, friendid) VALUES (?, ?);";
    String SQL_REMOVE_FRIEND =
            "DELETE FROM friends WHERE userid = ? AND friendid = ?;";
    String SQL_GET_BY_ID_AND_FRIEND =
            "SELECT id, email, first_name, last_name, birth_date, reg_date, sex, " +
                    "CONCAT_WS(' ', first_name, last_name) AS name, " +
                    "CASE WHEN userfriends.friendid ISNULL THEN FALSE ELSE TRUE END AS isuserfriend, " +
                    "CASE WHEN friendofuser.userid ISNULL THEN FALSE ELSE TRUE END AS isfriendofuser " +
                    "FROM users " +
                    "LEFT JOIN friends AS userfriends ON users.id = userfriends.friendid AND userfriends.userid = ? " +
                    "LEFT JOIN friends AS friendofuser ON users.id = friendofuser.userid AND friendofuser.friendid = ? " +
                    "WHERE id = ?;";

    @Override
    default void addFriend(User user, User friend) {
        insert(FriendsBean.class, SQL_ADD_FRIEND, user.getId(), friend.getId());
    }

    @Override
    default void removeFriend(User user, User friend) {
        delete(SQL_REMOVE_FRIEND, user.getId(), friend.getId());
    }

    @Override
    default Optional<User> getById(long id, long profileid) {
        return select(User.class, SQL_GET_BY_ID_AND_FRIEND, id, profileid, id);
    }


}
