import database.DBConnectionFactory;
import model.Account;
import model.Action;
import model.Client;
import model.User;
import model.builder.AccountBuilder;
import model.builder.ActionBuilder;
import model.builder.ClientBuilder;
import model.builder.UserBuilder;
import model.validation.Notification;
import repository.Cache;
import repository.EntityNotFoundException;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.action.ActionRepositoryMySQL;
import repository.client.ClientRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.AuthenticationException;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class main {

    public static void main(String[] args) throws EntityNotFoundException, AuthenticationException {


        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();

        AccountRepositoryMySQL accountRepository = new AccountRepositoryMySQL(connection);
        RightsRolesRepositoryMySQL rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        UserRepositoryMySQL userRepositoryMySQL = new UserRepositoryMySQL(connection,rightsRolesRepository);
        ActionRepositoryMySQL actionRepositoryMySQL = new ActionRepositoryMySQL(connection);
        /////// TEST CREATE ACCOUNT
        //Long id, String type, Long amountOfMoney, Date dateOfCreation

       /* Long id=2L;
        String clientName = "Andrew";
        String type = "ie";
        Long amountOfMoney = 111L;
        Date date = new Date();

        Account account = new AccountBuilder()
                .setClientName(clientName)
                .setType("newAccount")
                .setAmountOfMoney(111L)
                .setDateOfCreation(new Date())
                .build();

       accountRepository.addAccount(account);

        List<Account> accountList = new ArrayList<>();
       System.out.println(accountRepository.findAll().size());
        accountList = accountRepository.findAll();
        for(Account account1: accountList)
        {   System.out.println(account1.getClientName());
            System.out.println(account1.getAmountOfMoney());
            System.out.println(account1.getType());
        }
    */
       //////TEST FIND ACCOUNT BY ID

       /* Long id = 2L;
        Account account1 = accountRepository.findAccountById(id);
        System.out.println(account1.getClientName());
        System.out.println(account1.getAmountOfMoney());
        System.out.println(account1.getType());
        */

        /////////TEST DELETE ACCOUNT

        //accountRepository.deleteAccount((long) 4);
        //TEST account update

        Account account = new AccountBuilder()
                .setType("newAccount")
                .setAmountOfMoney(111L)
                .setDateOfCreation(new Date())
                .build();

      //  accountRepository.updateAccount(account,1l);

        ////TEST CLIENT FIND ALL

        ClientRepositoryMySQL clientRepositoryMySQL = new ClientRepositoryMySQL(connection);

        /*List<Client> clients = new ArrayList<>();

        clients = clientRepositoryMySQL.findAll();

        for(Client client1: clients)
        {
            System.out.println(client1.getAddress());
        }
        */
        //TEST ADD CLIENT
        Client client = new ClientBuilder()
                .setId(6l)
                .setName("octav")
                .setIdentityCardNumber(111L)
                .setPersonalNumericalCode(12l)
                .setAddress("Address")
                .build();

        clientRepositoryMySQL.addClient(client);

      //  clientRepositoryMySQL.updateClient(client,1l);

      /*  List<Client> clients = new ArrayList<>();

        clients = clientRepositoryMySQL.findAll();

        for(Client client1: clients)
        {
            System.out.println(client1.getAddress());
            System.out.println(client1.getClientId());
        }

        accountRepository.addAccountToClient(account,client);
    */
//
        //TEST FIND CLIENT BY NAME

        /*Client client2 = clientRepositoryMySQL.findClientByName("John");

        System.out.println(client2.getName());
        System.out.println(client2.getAddress());
        */


       /* Date date = new Date();
       Account account1 = new AccountBuilder()
               .setType("spending")
               .setAmountOfMoney(111l)
               .setDateOfCreation(new java.sql.Date(date.getTime()))
               .build();

       //accountRepository.deleteAccount(7l);

        Date date1 = new Date();
        Account account2 = new AccountBuilder()
                .setType("spending")
                .setAmountOfMoney(111l)
                .setDateOfCreation(new java.sql.Date(date1.getTime()))
                .build();
        Long id = 3l;
        accountRepository.addAccountToClient(account2,id);
        Long accountId = account2.getId();
        System.out.println(accountId);
        int initSize = accountRepository.findAll().size();
        accountRepository.deleteAccount(accountId);
        int finalSize = accountRepository.findAll().size();

        System.out.println(initSize);
        System.out.println(finalSize);

        */
       Date date = new Date();
        Action action = new ActionBuilder()
                .setDescription("This is a new description!!")
                .setUserId(1l)
                .setDate(date)
                .build();
      // actionRepositoryMySQL.saveAction(action);

       /* Notification<User> notification = userRepositoryMySQL.findByUsernameAndPassword("andy@gmail.com","aB123456;");
        User userNt = notification.getResult();

        System.out.println(userNt.getUsername());*/

        List<Action> actions = actionRepositoryMySQL.findActionsForUser(1l);

        for(Action action1:actions){
            System.out.println(action1.getDescription());
        }
    }


}