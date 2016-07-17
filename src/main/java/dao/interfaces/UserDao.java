package dao.interfaces;

import model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends Dao {

    Optional<User> getById(long id);
    Optional<User> getByEmail(String email);
    Optional<User> getByEmailPassword(String email, String password);
    List<User> getAll(String searchParam);
    Optional<User> addNew(String email, String password, String first_name, String last_name);

}