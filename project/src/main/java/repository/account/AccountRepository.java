package repository.account;

import model.Account;
import model.Client;
import repository.EntityNotFoundException;

import java.util.Date;
import java.util.List;

public interface AccountRepository {

   /* void createAccount(Long id,String clientName, String type, Long amountOfMoney, Date dateOfCreation);

    void updateAccount(Account account);

    void deleteAccount(Account account);

    Account findRoleById(Long id);
    */

    List<Account> findAll();

    boolean addAccountToClient(Account account, Long clientId);

    Account findAccountById(Long accountId) throws EntityNotFoundException;

    void updateAccount(Account account,Long accountId);

    void deleteAccount(Long accountId);

    void deleteAll();

}
