package repository.user;

import database.DBConnectionFactory;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.EntityNotFoundException;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static database.Constants.Roles.ADMINISTRATOR;
import static database.Constants.Roles.EMPLOYEE;
import static org.junit.Assert.*;

public class UserRepositoryMySQLTest {
    private static RightsRolesRepository rightsRolesRepository;
    private static UserRepository userRepository;

    @BeforeClass
    public static void setUpClass() {

        Connection connection = new DBConnectionFactory().getConnectionWrapper(false).getConnection();
        rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
    }
    @Before
    public void cleanUp() {
        userRepository.removeAll();
        List<Role> roles = new ArrayList<>();
        roles.add(rightsRolesRepository.findRoleByTitle(EMPLOYEE));
        User user = new UserBuilder()
                .setUsername("email@gmail.com")
                .setPassword("Password12;")
                .setRoles(roles)
                .build();
        userRepository.save(user);
    }

    @Test
    public void findAll() {
        List<Role> roles = new ArrayList<>();
        roles.add(rightsRolesRepository.findRoleByTitle(EMPLOYEE));
        User user = new UserBuilder()
                .setUsername("email1@gmail.com")
                .setPassword("Password12;")
                .setRoles(roles)
                .build();
        userRepository.save(user);
        User user2 = new UserBuilder()
                .setUsername("emai2l@gmail.com")
                .setPassword("Password12;")
                .setRoles(roles)
                .build();
        userRepository.save(user2);

        assertEquals( userRepository.findAll().size(),3);
}

    @Test
    public void save() {
        List<Role> roles = new ArrayList<>();
        roles.add(rightsRolesRepository.findRoleByTitle(EMPLOYEE));
        User user = new UserBuilder()
                .setUsername("username")
                .setPassword("password")
                .setRoles(roles)
                .build();
        assertTrue(userRepository.save(user));
    }

    @Test
    public void updateUser() throws EntityNotFoundException {
        List<User> users = userRepository.findAll();
        User user = users.get(userRepository.findAll().size()-1);
        Long userId = user.getId();

        List<Role> roles = new ArrayList<>();
        roles.add(rightsRolesRepository.findRoleByTitle(EMPLOYEE));
        User user1 = new UserBuilder()
                .setUsername("emaill@gmail.com")
                .setPassword("Password12;")
                .setRoles(roles)
                .build();

        userRepository.updateUser(user1,userId);
        String name = userRepository.findUserById(userId).getUsername();
        assertEquals(name,"emaill@gmail.com");
    }

    @Test
    public void findByUsernameAndPassword() {
        List<Role> roles = new ArrayList<>();
        roles.add(rightsRolesRepository.findRoleByTitle(ADMINISTRATOR));
        User user = new UserBuilder()
                .setUsername("username1")
                .setPassword("password1")
                .setRoles(roles)
                .build();
        userRepository.save(user);
        User user1=null;
        try{
            user1 = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword()).getResult();
        }
        catch (AuthenticationException e){
            e.printStackTrace();
        }
        String name = user.getUsername();
        String newName = user1.getUsername();
        assertEquals(name,newName);

    }


    @Test
    public void deleteUser() {
        List<User> users = userRepository.findAll();
        int size = userRepository.findAll().size();
        User user1 = users.get(userRepository.findAll().size()-1);
        Long userId = user1.getId();

        userRepository.deleteUser(userId);
        int finalSize = userRepository.findAll().size();
        assertEquals(Integer.valueOf(size),Integer.valueOf(finalSize+1));
    }

    @Test
    public void removeAll() {
        userRepository.removeAll();
        List<User> users = userRepository.findAll();
        assertEquals(users.size(), 0);
    }
}