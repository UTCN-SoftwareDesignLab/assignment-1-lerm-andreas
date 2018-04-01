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

/**
 * Created by Alex on 18/03/2017.
 */
public class LoginComponentFactory {

    private final AuthenticationService authenticationService;

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final ActionRepository actionRepository;

    private static LoginComponentFactory instance;

    public static LoginComponentFactory instance(Boolean componentsForTests) {
        if (instance == null) {
            instance = new LoginComponentFactory(componentsForTests);
        }
        return instance;
    }

    private LoginComponentFactory(Boolean componentsForTests) {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(componentsForTests).getConnection();
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.actionRepository = new ActionRepositoryMySQL(connection) ;

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
