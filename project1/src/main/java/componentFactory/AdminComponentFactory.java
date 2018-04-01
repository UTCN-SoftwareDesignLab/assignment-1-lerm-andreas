package componentFactory;

import database.DBConnectionFactory;
import repository.action.ActionRepository;
import repository.action.ActionRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceMySQL;

import java.sql.Connection;

public class AdminComponentFactory {

    private final AuthenticationService authenticationService;

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final ActionRepository actionRepository;

    private static AdminComponentFactory instance;

    public static AdminComponentFactory instance(Boolean componentsForTests) {
        if (instance == null) {
            instance = new AdminComponentFactory(componentsForTests);
        }
        return instance;
    }

    private AdminComponentFactory(Boolean componentsForTests) {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(componentsForTests).getConnection();
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.actionRepository = new ActionRepositoryMySQL(connection);
        this.authenticationService = new AuthenticationServiceMySQL(this.userRepository, this.rightsRolesRepository,this.actionRepository);
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
