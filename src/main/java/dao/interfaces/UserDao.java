package dao.interfaces;

import model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends Dao {

    long getNumberOfUsers();
    Optional<User> getById(long id);
    Optional<User> getByEmail(String email);
    Optional<User> getByEmailPassword(String email, String password);
    List<User> getList(User currentUser, long recordsPerPage, long offset, String searchParam);
    Optional<User> addUser(String email, String password, String first_name, String last_name);
    void removeUser(User user);

}