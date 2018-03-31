package service.user;

import model.User;
import model.validation.Notification;
import repository.EntityNotFoundException;
import repository.user.AuthenticationException;

/**
 * Created by Alex on 11/03/2017.
 */
public interface AuthenticationService {

    Notification<Boolean> register(String username, String password);

    Notification<User> login(String username, String password) throws AuthenticationException;
    public Notification<User> viewUser(Long id) throws EntityNotFoundException;
    boolean logout(User user);
    public Notification<Boolean> updateUser(String userName, String password, Long userId) throws EntityNotFoundException;
    public void deleteUser(Long id) throws EntityNotFoundException;
}
