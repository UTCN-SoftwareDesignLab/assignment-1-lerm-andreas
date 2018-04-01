package repository.account;

import database.DBConnectionFactory;
import model.Account;
import model.builder.AccountBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.Cache;
import repository.EntityNotFoundException;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class AccountRepositoryMySQLTest {

    private static AccountRepository accountRepository;


    @BeforeClass
    public static void setupClass() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(false).getConnection();
        accountRepository = new AccountRepositoryMySQL(connection);
    }

    @Before
    public void cleanUp() {
        accountRepository.removeAll();
        Date date = new Date();
        Account account = new AccountBuilder()
                .setType("spending")
                .setAmountOfMoney(111l)
                .setDateOfCreation(new java.sql.Date(date.getTime()))
                .build();
        Long id = 3l;
        accountRepository.addAccountToClient(account,id);
    }

    @Test
    public void findAll() {
        List<Account> accounts = accountRepository.findAll();
        assertEquals(accounts.size(), 1);
    }

    @Test
    public void addAccountToClient() {
        Date date = new Date();
        Account account = new AccountBuilder()
                .setType("saving")
                .setAmountOfMoney(111l)
                .setDateOfCreation(new java.sql.Date(date.getTime()))
                .build();
        Long id = 4l;
        accountRepository.addAccountToClient(account,id);
        int accountSize = accountRepository.findAll().size();
        assertEquals(accountSize, 2);
    }

    @Test
    public void findAccountById() {
        Date date = new Date();
        Account account = new AccountBuilder()
                .setType("spending")
                .setAmountOfMoney(111l)
                .setDateOfCreation(new java.sql.Date(date.getTime()))
                .build();
        Long id = 3l;
        accountRepository.addAccountToClient(account,id);

        try {
            account = accountRepository.findAccountById(accountRepository.findAll().get(accountRepository.findAll().size()-1).getId());
        }
        catch(EntityNotFoundException e){

        }
        Long amount  = account.getAmountOfMoney();
        assertEquals(amount,Long.valueOf(111l));
    }

    @Test
    public void updateAccount() throws EntityNotFoundException {
        List<Account> accounts = accountRepository.findAll();
        Account account = accounts.get(accountRepository.findAll().size()-1);
        Long accountId = account.getId();
        Date date = new Date();
        Account account1 = new AccountBuilder()
                .setType("spending")
                .setAmountOfMoney(1112l)
                .setDateOfCreation(new java.sql.Date(date.getTime()))
                .build();
        accountRepository.updateAccount(account1,accountId);
        Long value = accountRepository.findAccountById(accountId).getAmountOfMoney();
        assertEquals(Long.valueOf(value),Long.valueOf(1112l));
    }

    @Test
    public void deleteAccount() {
        List<Account> accounts = accountRepository.findAll();
        int size = accountRepository.findAll().size();
        Account account1 = accounts.get(accountRepository.findAll().size()-1);
        Long account1Id = account1.getId();

        accountRepository.deleteAccount(account1Id);
        int finalSize = accountRepository.findAll().size();
        assertEquals(Integer.valueOf(size),Integer.valueOf(finalSize+1));
    }

    @Test
    public void removeAll() {
        accountRepository.removeAll();
        List<Account> accounts = accountRepository.findAll();
        assertEquals(accounts.size(), 0);
    }

}