package repository.user;

import model.User;
import model.validation.Notification;
import repository.EntityNotFoundException;

import java.util.List;

/**
 * Created by Alex on 11/03/2017.
 */
public interface UserRepository {

    List<User> findAll();

    Notification<User> findByUsernameAndPassword(String username, String password) throws AuthenticationException;

    boolean save(User user);

    void removeAll();

    public void updateUser(User user,Long id);

    public void deleteUser(Long id);

    public User findUserById(Long userId) throws EntityNotFoundException;
}
