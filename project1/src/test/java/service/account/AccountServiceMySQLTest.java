package service.account;

import database.DBConnectionFactory;
import model.Account;
import model.Client;
import model.builder.AccountBuilder;
import model.builder.ClientBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.EntityNotFoundException;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import service.client.ClientServiceMySQL;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class AccountServiceMySQLTest {

    public static final String ACCOUNT_TYPE ="spending";
    public static final Long AMOUNT_OF_MONEY = 100l;
    public static final Long TRANSACTION_COST = 10l;

    private static AccountService accountService;
    private static AccountRepository accountRepository;
    private static ClientRepository clientRepository;
    @BeforeClass
    public static void setUp() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(false).getConnection();
        accountRepository = new AccountRepositoryMySQL(connection);

        accountService = new AccountServiceMySQL(
                accountRepository,clientRepository
        );
    }
    @Before
    public void cleanUp() {
        accountRepository.removeAll();
        Date date1 = new Date();
        Account account = new AccountBuilder()
                .setType("spending")
                .setAmountOfMoney(111l)
                .setDateOfCreation(new java.sql.Date(date1.getTime()))
                .build();
        Long id = 1l;
        accountRepository.addAccountToClient(account,id);

    }
    @Test
    public void addAccountToClient() {
        Long id = accountRepository.findAll().get(accountRepository.findAll().size()-1).getId()+1;
        Date date = new Date();
        Assert.assertTrue(accountService.addAccountToClient(ACCOUNT_TYPE,AMOUNT_OF_MONEY,date,id).getResult());
    }

    @Test
    public void viewAccount() {
        Long id = accountRepository.findAll().get(accountRepository.findAll().size()-1).getId()+1;
        Date date = new Date();
        accountService.addAccountToClient(ACCOUNT_TYPE,AMOUNT_OF_MONEY,date,id);
        List<Account> accounts = accountRepository.findAll();
        Account account = accounts.get(accountRepository.findAll().size()-1);
        assertEquals(account.getAmountOfMoney(),AMOUNT_OF_MONEY);
    }

    @Test
    public void transferMoney() throws EntityNotFoundException {
        Long destinationId = accountRepository.findAll().get(accountRepository.findAll().size()-1).getId();
        Long sourceId = accountRepository.findAll().get(0).getId();
        Assert.assertTrue(accountService.transferMoney(sourceId,destinationId,TRANSACTION_COST).getResult());
    }


    @Test
    public void updateAccount() throws EntityNotFoundException {
        Long id = accountRepository.findAll().get(accountRepository.findAll().size()-1).getId()+1;
        Date date = new Date();
        accountService.addAccountToClient(ACCOUNT_TYPE,AMOUNT_OF_MONEY,date,id);
        List<Account> accounts = accountRepository.findAll();
        Account account = accounts.get(accountRepository.findAll().size()-1);
        Long accountId = account.getId();
        Assert.assertTrue(accountService.updateAccount(ACCOUNT_TYPE,AMOUNT_OF_MONEY,date,accountId).getResult());
    }

}