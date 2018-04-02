package service.user;

import model.Action;
import model.User;
import model.validation.Notification;
import repository.EntityNotFoundException;
import repository.user.AuthenticationException;

import java.util.List;

/**
 * Created by Alex on 11/03/2017.
 */
public interface AuthenticationService {

    Notification<Boolean> register(String username, String password);

    Notification<User> login(String username, String password) throws AuthenticationException;
    public Notification<User> viewUser(Long id) throws EntityNotFoundException;
    boolean logout(User user);
   Notification<Boolean> updateUser(String userName, String password, Long userId);
    public void deleteUser(Long id) throws EntityNotFoundException;
    public Notification<User> viewUserByUserNameAndPassword(String username, String password) throws EntityNotFoundException, AuthenticationException;
    public void saveAction(String description, Long userId);
    public List<Action> getActionsForUser(Long id) throws EntityNotFoundException;
}
