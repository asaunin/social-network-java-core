package dao.interfaces;

import model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends Dao {

    long getNumberOfUsers(User currentUser);
    Optional<User> getById(long id);
    Optional<User> getById(long id, long profileId);
    Optional<User> getByEmail(String email);
    Optional<User> getByEmailPassword(String email, String password);
    List<User> getAll(User user, long recordsPerPage, long offset, String searchParam);
    Optional<User> addNew(String email, String password, String first_name, String last_name);
    void addFriend(User user, User friend);
    void removeFriend(User user, User friend);

}