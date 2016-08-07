package dao.interfaces;

import model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends Dao {

    long getNumberOfUsers(long currentUserId);
    long getNumberOfUsers(long currentUserId, String searchText);
    Optional<User> getUserById(long id);
    Optional<User> getUserById(long id, long isFriendId);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByEmailPassword(String email, String password);
    List<User> getUserList(User currentUser, int recordsPerPage, int offset, String searchText);
    Optional<User> addUser(String email, String password, String first_name, String last_name);
    void updateUser(User user);
    void removeUser(User user);
    void changePassword(User user, String password);

}