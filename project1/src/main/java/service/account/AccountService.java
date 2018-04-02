package service.account;

import model.Account;
import model.validation.Notification;
import repository.EntityNotFoundException;

import java.util.Date;

public interface AccountService {

    Notification<Boolean> addAccountToClient(String type, Long ammountOfMoney, Date creationDate, Long clientId);

    public Notification<Boolean> updateAccount(String type, Long amountOfMoney, Date creationDate, Long accountId);

    Notification<Account> viewAccount(Long id) throws EntityNotFoundException;

    void deleteAccount(Long id) throws EntityNotFoundException;

    Notification<Boolean> transferMoney(Long sourceId,Long destinationId, Long amount);

    Notification<Boolean> processBill(Long cost, Long clientId, Long accountId);

}
