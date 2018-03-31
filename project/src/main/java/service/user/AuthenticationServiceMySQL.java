package service.user;

import model.Account;
import model.Client;
import model.Role;
import model.User;
import model.builder.AccountBuilder;
import model.builder.UserBuilder;
import model.validation.AccountValidator;
import model.validation.Notification;
import model.validation.UserValidator;
import repository.EntityNotFoundException;
import repository.security.RightsRolesRepository;
import repository.user.AuthenticationException;
import repository.user.UserRepository;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.Date;

import static database.Constants.Roles.EMPLOYEE;

/**
 * Created by Alex on 11/03/2017.
 */
public class AuthenticationServiceMySQL implements AuthenticationService {

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AuthenticationServiceMySQL(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<Boolean> register(String username, String password) {
        Role customerRole = rightsRolesRepository.findRoleByTitle(EMPLOYEE);
        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRoles(Collections.singletonList(customerRole))
                .build();

        UserValidator userValidator = new UserValidator(user);
        boolean userValid = userValidator.validate();
        Notification<Boolean> userRegisterNotification = new Notification<>();

        if (!userValid) {
            userValidator.getErrors().forEach(userRegisterNotification::addError);
            userRegisterNotification.setResult(Boolean.FALSE);
        } else {
            user.setPassword(encodePassword(password));
            userRegisterNotification.setResult(userRepository.save(user));
        }
        return userRegisterNotification;
    }

    @Override
    public Notification<User> login(String username, String password) throws AuthenticationException {
        return userRepository.findByUsernameAndPassword(username, encodePassword(password));
    }

    @Override
    public boolean logout(User user) {
        return false;
    }

    @Override
    public Notification<User> viewUser(Long id) throws EntityNotFoundException {

        Notification<User> userNotification = new Notification<>();

        userNotification.setResult(userRepository.findUserById(id));

        return userNotification;
    }
    @Override
    public Notification<Boolean> updateUser(String userName, String password, Long userId) throws EntityNotFoundException {
        Role customerRole = rightsRolesRepository.findRoleByTitle(EMPLOYEE);
        User user = new UserBuilder()
                .setUsername(userName)
                .setPassword(password)
                .setRoles((Collections.singletonList(customerRole)))
                .build();

        User sourceUser = userRepository.findUserById(userId);

        UserValidator userValidator = new UserValidator(user);
        boolean goodUser= userValidator.validate();
        Notification<Boolean> booleanNotification = new Notification<>();

        if(!goodUser){
            booleanNotification.setResult(Boolean.FALSE);
            userValidator.getErrors().forEach(booleanNotification::addError);
        }
        else{
            user.setPassword(encodePassword(password));
            userRepository.updateUser(user,userId);
            booleanNotification.setResult(Boolean.TRUE);
        }
        return booleanNotification;
    }

    @Override
    public void deleteUser(Long id) throws EntityNotFoundException {

        User user = userRepository.findUserById(id);

        userRepository.deleteUser(id);

    }
    private String encodePassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
