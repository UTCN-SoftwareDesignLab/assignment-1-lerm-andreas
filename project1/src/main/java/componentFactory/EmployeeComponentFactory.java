package componentFactory;

import database.DBConnectionFactory;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.action.ActionRepository;
import repository.action.ActionRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.account.AccountService;
import service.account.AccountServiceMySQL;
import service.client.ClientService;
import service.client.ClientServiceMySQL;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceMySQL;

import java.sql.Connection;

public class EmployeeComponentFactory {

    private final ClientService clientService;

    private final ClientRepository clientRepository;

    private final AccountService accountService;

    private final AccountRepository accountRepository;

    private final ActionRepository actionRepository;

    private final AuthenticationService authenticationService;

    private final RightsRolesRepository rightsRolesRepository;

    private final UserRepository userRepository;

     private static EmployeeComponentFactory instance;

    public static EmployeeComponentFactory instance(Boolean componentForTests){
        if (instance == null) {
            instance = new EmployeeComponentFactory(componentForTests);
        }
        return instance;
    }

    private EmployeeComponentFactory(Boolean componentForTests){
        Connection connection = new DBConnectionFactory().getConnectionWrapper(componentForTests).getConnection();
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection,rightsRolesRepository);
        this.actionRepository = new ActionRepositoryMySQL(connection);
        this.clientRepository = new ClientRepositoryMySQL(connection);
        this.clientService  = new ClientServiceMySQL(clientRepository);
        this.accountRepository = new AccountRepositoryMySQL(connection);
        this.accountService = new AccountServiceMySQL(accountRepository,clientRepository);
        this.authenticationService = new AuthenticationServiceMySQL(this.userRepository, this.rightsRolesRepository, this.actionRepository) {
        };
    }


    public AccountService getAccountService() {
        return accountService;
    }

    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public ClientRepository getClientRepository() {
        return clientRepository;
    }

    public ActionRepository getActionRepository() {
        return actionRepository;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
