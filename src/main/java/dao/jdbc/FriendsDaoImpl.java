package dao.jdbc;

import dao.beans.FriendsBean;
import dao.beans.LongBean;
import dao.interfaces.FriendsDao;
import model.User;

import java.util.Collection;
import java.util.Optional;

@FunctionalInterface
public interface FriendsDaoImpl extends FriendsDao {

    @Override
    default void addFriend(User user, User friend) {
        final String SQL_ADD_FRIEND =
                "INSERT INTO friends (userid, friendid) VALUES (?, ?);";

        insert(FriendsBean.class, SQL_ADD_FRIEND, user.getId(), friend.getId());
    }

    @Override
    default void removeFriend(User user, User friend) {
        final String SQL_REMOVE_FRIEND =
                "DELETE FROM friends WHERE userid = ? AND friendid = ?;";

        delete(SQL_REMOVE_FRIEND, user.getId(), friend.getId());
    }

    @Override
    default long getNumberOfFriends(User user, String searchText) {
        final String SQL_GET_NUMBER_OF_FRIENDS =
                "SELECT count(friendid) AS id FROM friends "
                        .concat("WHERE friends.userid = ?;");
        final String SQL_GET_NUMBER_OF_FRIENDS_WITH_SEARCH_PARAMETER =
                "SELECT count(id) AS id FROM users "
                        .concat("INNER JOIN friends AS userfriends ON users.id = userfriends.friendid AND userfriends.userid = ? ")
                        .concat("WHERE LOWER(CONCAT_WS(' ', first_name, last_name)) LIKE LOWER(?) ");
        final Optional<LongBean> bean;

        if (searchText==null || searchText.isEmpty())
            bean = select(LongBean.class, SQL_GET_NUMBER_OF_FRIENDS, user.getId());
        else
            bean = select(LongBean.class, SQL_GET_NUMBER_OF_FRIENDS_WITH_SEARCH_PARAMETER, user.getId(), "%" + searchText + "%");
        return bean.get().getId();

    }

    @Override
    default Collection<User> getFriendList(User currentUser, int recordsPerPage, int offset, String searchText) {
        final String SQL_GET_FRIEND_LIST =
                "SELECT id, email, first_name, last_name, birth_date, phone, reg_date, sex, "
                        .concat("CONCAT_WS(' ', first_name, last_name) AS name, ")
                        .concat("CASE WHEN userfriends.friendid ISNULL THEN FALSE ELSE TRUE END AS isuserfriend, ")
                        .concat("CASE WHEN friendofuser.userid ISNULL THEN FALSE ELSE TRUE END AS isfriendofuser ")
                        .concat("FROM users ")
                        .concat("INNER JOIN friends AS userfriends ON users.id = userfriends.friendid AND userfriends.userid = ? ")
                        .concat("LEFT JOIN friends AS friendofuser ON users.id = friendofuser.userid AND friendofuser.friendid = ? ")
                        .concat("ORDER BY name ")
                        .concat("limit ? offset ?;");
        final String SQL_GET_FRIEND_LIST_WITH_SEARCH_PARAMETER =
                "SELECT id, email, first_name, last_name, birth_date, phone, reg_date, sex, "
                        .concat("CONCAT_WS(' ', first_name, last_name) AS name, ")
                        .concat("CASE WHEN userfriends.friendid ISNULL THEN FALSE ELSE TRUE END AS isuserfriend, ")
                        .concat("CASE WHEN friendofuser.userid ISNULL THEN FALSE ELSE TRUE END AS isfriendofuser ")
                        .concat("FROM users ")
                        .concat("INNER JOIN friends AS userfriends ON users.id = userfriends.friendid AND userfriends.userid = ? ")
                        .concat("LEFT JOIN friends AS friendofuser ON users.id = friendofuser.userid AND friendofuser.friendid = ? ")
                        .concat("WHERE LOWER(CONCAT_WS(' ', first_name, last_name)) LIKE LOWER(?) ")
                        .concat("ORDER BY name ")
                        .concat("limit ? offset ?;");

        if (searchText==null || searchText.isEmpty())
            return selectCollection(User.class,
                    SQL_GET_FRIEND_LIST,
                    currentUser.getId(),
                    currentUser.getId(),
                    recordsPerPage,
                    offset);
        else
            return selectCollection(User.class,
                    SQL_GET_FRIEND_LIST_WITH_SEARCH_PARAMETER,
                    currentUser.getId(),
                    currentUser.getId(),
                    "%" + searchText + "%",
                    recordsPerPage,
                    offset);

    }

}
