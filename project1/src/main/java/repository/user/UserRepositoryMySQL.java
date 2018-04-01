package repository.user;

import model.Account;
import model.Client;
import model.Role;
import model.User;
import model.builder.ClientBuilder;
import model.builder.UserBuilder;
import model.validation.Notification;
import repository.EntityNotFoundException;
import repository.security.RightsRolesRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static database.Constants.Roles.EMPLOYEE;
import static database.Constants.Tables.USER;

/**
 * Created by Alex on 11/03/2017.
 */
public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "Select * from user";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                users.add(getUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) throws AuthenticationException {
        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "Select * from `" + USER + "` where `username`=\'" + username + "\' and `password`=\'" + password + "\'";
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);
            if (userResultSet.next()) {
                List<Role> roles = new ArrayList<>();
                roles.add(rightsRolesRepository.findRoleByTitle(EMPLOYEE));
                User user = new UserBuilder()
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(roles)
                        .build();
                findByUsernameAndPasswordNotification.setResult(user);
                return findByUsernameAndPasswordNotification;
            } else {
                findByUsernameAndPasswordNotification.addError("Invalid email or password!");
                return findByUsernameAndPasswordNotification;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AuthenticationException();
        }
    }

    @Override
    public User findByUsernameAndPassword2(String username, String password) throws AuthenticationException {
        User user = null;
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "Select * from `" + USER + "` where `username`=\'" + username + "\' and `password`=\'" + password + "\'";
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);
            if (userResultSet.next()) {
                List<Role> roles = new ArrayList<>();
                roles.add(rightsRolesRepository.findRoleByTitle(EMPLOYEE));
                 user = new UserBuilder()
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(roles)
                        .build();

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AuthenticationException();
        }
        return user;
    }




    @Override
    public boolean save(User user) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?)");
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            user.setId(userId);
            rightsRolesRepository.addRolesToUser(user, user.getRoles());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void updateUser(User user,Long id)
    {
        try
        {
            PreparedStatement updateStatement=connection
                    .prepareStatement("UPDATE user SET username=?, password=? WHERE id=?");
            updateStatement.setString(1,user.getUsername());
            updateStatement.setString(2,user.getPassword());
            updateStatement.setLong(3,id);
            updateStatement.executeUpdate();
        }
        catch(SQLException e)
        {

        }
    }

    @Override
    public User findUserById(Long userId) throws EntityNotFoundException {

        try {
            Statement statement = connection.createStatement();
            String sql = "Select * from user where id =" + userId;
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                return getUserFromResultSet(rs);
            } else {
                throw new EntityNotFoundException(userId, User.class.getSimpleName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntityNotFoundException(userId, User.class.getSimpleName());
        }

    }


    @Override
    public void deleteUser(Long id) {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id =" + id;
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User getUserFromResultSet(ResultSet rs) throws SQLException {
        Role customerRole = rightsRolesRepository.findRoleByTitle(EMPLOYEE);
        return new UserBuilder()
                .setId(rs.getLong("id"))
                .setUsername(rs.getString("userName"))
                .setPassword(rs.getString("password"))
                .setRoles(Collections.singletonList(customerRole))
                .build();
    }


}
