package Sample;

import database.DBConnectionFactory;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceMySQL;

import java.sql.Connection;

/**
 * Created by Alex on 18/03/2017.
 */
public class MyComponentFactory {

    private final AuthenticationService authenticationService;

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    private static MyComponentFactory instance;

    public static MyComponentFactory instance(Boolean componentsForTests) {
        if (instance == null) {
            instance = new MyComponentFactory(componentsForTests);
        }
        return instance;
    }

    private MyComponentFactory(Boolean componentsForTests) {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(componentsForTests).getConnection();
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.authenticationService = new AuthenticationServiceMySQL(this.userRepository, this.rightsRolesRepository);
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }
}
