package Sample;

import database.DBConnectionFactory;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceMySQL;

import java.sql.Connection;

public class AdminConnectionFactory {

    private final AuthenticationService authenticationService;

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    private static AdminConnectionFactory instance;

    public static AdminConnectionFactory instance(Boolean componentsForTests) {
        if (instance == null) {
            instance = new AdminConnectionFactory(componentsForTests);
        }
        return instance;
    }

    private AdminConnectionFactory(Boolean componentsForTests) {
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
