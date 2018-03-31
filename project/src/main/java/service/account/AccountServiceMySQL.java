package service.account;

import model.Account;
import model.Client;
import model.builder.AccountBuilder;
import model.validation.AccountValidator;
import model.validation.Notification;
import repository.EntityNotFoundException;
import repository.account.AccountRepository;
import repository.client.ClientRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountServiceMySQL implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public AccountServiceMySQL(AccountRepository accountRepository, ClientRepository clientRepository){
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Notification<Boolean> addAccountToClient(String type, Long ammountOfMoney, Date creationDate,Long clientId){

        Account account = new AccountBuilder()
                .setType(type)
                .setAmountOfMoney(ammountOfMoney)
                .setDateOfCreation(creationDate)
                .build();

        AccountValidator accountValidator = new AccountValidator(account,0l);

        boolean accountValid = accountValidator.validate();
        Notification<Boolean> addAccountNotification = new Notification<>();

        if(!accountValid){
            accountValidator.getErrors().forEach(addAccountNotification::addError);
            addAccountNotification.setResult(Boolean.FALSE);
        }
        else{
            addAccountNotification.setResult(accountRepository.addAccountToClient(account,clientId));
        }
        return addAccountNotification;
    }


    @Override
    public Notification<Account> viewAccount(Long id) throws EntityNotFoundException{

        Notification<Account> accountNotification = new Notification<>();

        accountNotification.setResult(accountRepository.findAccountById(id));

        return accountNotification;
    }

    @Override
    public Notification<Boolean> transferMoney(Long sourceId,Long destinationId, Long amount) throws EntityNotFoundException {
        Account sourceAccount = accountRepository.findAccountById(sourceId);
        Account destinationAccount = accountRepository.findAccountById(destinationId);

     //   boolean enoughMoney = sourceAccount.getAmountOfMoney() > amount;
        AccountValidator accountValidator = new AccountValidator(sourceAccount,amount);
        boolean enoughMoney = accountValidator.validate();
        Notification<Boolean> booleanNotification = new Notification<>();

        if(!enoughMoney){
           booleanNotification.setResult(Boolean.FALSE);
            accountValidator.getErrors().forEach(booleanNotification::addError);
        }else{
            Account newSourceAccount = new AccountBuilder()
                    .setType(sourceAccount.getType())
                    .setAmountOfMoney(sourceAccount.getAmountOfMoney() - amount)
                    .setDateOfCreation(new java.sql.Date(sourceAccount.getDateOfCreation().getTime()))
                    .build();
            Account newDestinationAccount = new AccountBuilder()
                    .setType(destinationAccount.getType())
                    .setAmountOfMoney(destinationAccount.getAmountOfMoney() + amount)
                    .setDateOfCreation(new java.sql.Date(destinationAccount.getDateOfCreation().getTime()))
                    .build();

            accountRepository.updateAccount(newSourceAccount,sourceId);
            accountRepository.updateAccount(newDestinationAccount,destinationId);
            booleanNotification.setResult(Boolean.TRUE);
        }
        return booleanNotification;
    }

    @Override
    public Notification<Boolean> processBill(Long cost, Long clientId, Long accountId) throws EntityNotFoundException {
        Client client = clientRepository.findClientById(clientId);
        Account sourceAccount = accountRepository.findAccountById(accountId);

        AccountValidator accountValidator = new AccountValidator(sourceAccount,cost);
        boolean enoughMoney = accountValidator.validate();
        Notification<Boolean> booleanNotification = new Notification<>();

        if(!enoughMoney){
            booleanNotification.setResult(Boolean.FALSE);
            accountValidator.getErrors().forEach(booleanNotification::addError);
        }
        else{
            Account newSourceAccount = new AccountBuilder()
                    .setType(sourceAccount.getType())
                    .setAmountOfMoney(sourceAccount.getAmountOfMoney() - cost)
                    .setDateOfCreation(new java.sql.Date(sourceAccount.getDateOfCreation().getTime()))
                    .build();
            accountRepository.updateAccount(newSourceAccount,sourceAccount.getId());
            booleanNotification.setResult(Boolean.TRUE);
        }
        return booleanNotification;
    }

    @Override
    public Notification<Boolean> updateAccount(String type, Long amountOfMoney, Date creationDate, Long accountId) throws EntityNotFoundException {
        Account sourceAccount = accountRepository.findAccountById(accountId);
        Account account = new AccountBuilder()
                .setType(type)
                .setAmountOfMoney(amountOfMoney)
                .setDateOfCreation( new java.sql.Date(sourceAccount.getDateOfCreation().getTime()))
                .build();

        AccountValidator accountValidator = new AccountValidator(account,0l);
        boolean goodAccount = accountValidator.validate();
        Notification<Boolean> booleanNotification = new Notification<>();

        if(!goodAccount){
            booleanNotification.setResult(Boolean.FALSE);
            accountValidator.getErrors().forEach(booleanNotification::addError);
        }
        else{
            accountRepository.updateAccount(account,accountId);
            booleanNotification.setResult(Boolean.TRUE);
        }
        return booleanNotification;
    }

    @Override
    public void deleteAccount(Long id) throws EntityNotFoundException {

        Account account = accountRepository.findAccountById(id);

        accountRepository.deleteAccount(id);

    }
}
