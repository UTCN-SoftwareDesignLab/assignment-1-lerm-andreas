package Sample;

import controller.EmployeeController;
import database.DBConnectionFactory;
import model.Account;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import repository.user.UserRepositoryMySQL;
import service.account.AccountService;
import service.account.AccountServiceMySQL;
import service.client.ClientService;
import service.client.ClientServiceMySQL;

import java.sql.Connection;

public class EmployeeConnectionFactory {

    private final ClientService clientService;

    private final ClientRepository clientRepository;

    private final AccountService accountService;

    private final AccountRepository accountRepository;

     private static EmployeeConnectionFactory instance;

    public static EmployeeConnectionFactory instance(Boolean componentForTests){
        if (instance == null) {
            instance = new EmployeeConnectionFactory(componentForTests);
        }
        return instance;
    }

    private EmployeeConnectionFactory(Boolean componentForTests){
        Connection connection = new DBConnectionFactory().getConnectionWrapper(componentForTests).getConnection();

        this.clientRepository = new ClientRepositoryMySQL(connection);
        this.clientService  = new ClientServiceMySQL(clientRepository);
        this.accountRepository = new AccountRepositoryMySQL(connection);
        this.accountService = new AccountServiceMySQL(accountRepository,clientRepository);
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
}
