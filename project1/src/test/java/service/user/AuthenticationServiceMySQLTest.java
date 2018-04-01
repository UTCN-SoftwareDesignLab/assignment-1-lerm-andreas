package service.user;

import database.DBConnectionFactory;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import repository.EntityNotFoundException;
import repository.action.ActionRepository;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Roles.EMPLOYEE;
import static org.junit.Assert.assertEquals;

/**
 * Created by Alex on 11/03/2017.
 */
public class AuthenticationServiceMySQLTest {

    public static final String TEST_USERNAME = "test@username.com";
    public static final String TEST_PASSWORD = "TestPassword1@";
    private static AuthenticationService authenticationService;
    private static UserRepository userRepository;
    private static RightsRolesRepository rightsRolesRepository;
    private static ActionRepository actionRepository;
    @BeforeClass
    public static void setUp() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(false).getConnection();
        rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);

        authenticationService = new AuthenticationServiceMySQL(
                userRepository,
                rightsRolesRepository,
                actionRepository
        );
    }

    @Before
    public void cleanUp() {
        userRepository.removeAll();
        List<Role> roles = new ArrayList<>();
        roles.add(rightsRolesRepository.findRoleByTitle(EMPLOYEE));
        User user = new UserBuilder()
                .setUsername("emailtest@gmail.com")
                .setPassword("Password12;")
                .setRoles(roles)
                .build();
        userRepository.save(user);
    }


    @Test
    public void register() throws Exception {
        Assert.assertTrue(
                authenticationService.register(TEST_USERNAME, TEST_PASSWORD).getResult()
        );
    }

    @Test
    public void login() throws Exception {
        authenticationService.register(TEST_USERNAME, TEST_PASSWORD).getResult();
        User user = authenticationService.login(TEST_USERNAME, TEST_PASSWORD).getResult();
        Assert.assertNotNull(user);
    }

    @Test
    public void logout() throws Exception {

    }

    @Test
    public void updateUser() throws EntityNotFoundException {
        Assert.assertTrue(
                authenticationService.updateUser(TEST_USERNAME, TEST_PASSWORD,userRepository.findAll().get(userRepository.findAll().size()-1).getId()).getResult());
    }

    @Test
    public void deleteUser() throws EntityNotFoundException {
        List<User> users = userRepository.findAll();
        int size = userRepository.findAll().size();
        User user1 = users.get(userRepository.findAll().size()-1);
        Long userId = user1.getId();

        authenticationService.deleteUser(userId);
        int finalSize = userRepository.findAll().size();
        assertEquals(Integer.valueOf(size),Integer.valueOf(finalSize+1));
    }

    @Test
    public void viewUser(){
        authenticationService.register(TEST_USERNAME, TEST_PASSWORD);
        List<User> users = userRepository.findAll();
        User user1 = users.get(userRepository.findAll().size()-1);
        assertEquals(user1.getUsername(),TEST_USERNAME);
    }
}