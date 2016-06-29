package dao.interfaces;

import model.User;

import java.util.Optional;

public interface UserDao extends Dao {

    Optional<User> get(long id);
    Optional<User> get(String email, String pwdHash);

}
