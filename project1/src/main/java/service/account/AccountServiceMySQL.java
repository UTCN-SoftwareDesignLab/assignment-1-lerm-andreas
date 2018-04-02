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
        Notification<Boolean> addAccountNotification = new Notification<>();
        Client client = new Client();
        Account account = new AccountBuilder()
                .setType(type)
                .setAmountOfMoney(ammountOfMoney)
                .setDateOfCreation(creationDate)
                .build();

        AccountValidator accountValidator = new AccountValidator(account,0l);
        try {
            client = clientRepository.findClientById(clientId);
        }
        catch (EntityNotFoundException e){
            addAccountNotification.addError("The client does not exist!");
        }
        boolean accountValid = accountValidator.validate();

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
    public Notification<Boolean> transferMoney(Long sourceId,Long destinationId, Long amount) {
        Notification<Boolean> booleanNotification = new Notification<>();
        boolean test = false;
        Account sourceAccount = new Account();
        Account destinationAccount = new Account();
        try {
            sourceAccount = accountRepository.findAccountById(sourceId);

        }
        catch (EntityNotFoundException e){
            booleanNotification.addError("Source account does not exist!");
            test = true;
        }
        try {
             destinationAccount = accountRepository.findAccountById(destinationId);
        }
        catch (EntityNotFoundException e){
            booleanNotification.addError("Destination account does not exist!");
            test = true;
        }

        if(test == false){
        AccountValidator accountValidator = new AccountValidator(sourceAccount,amount);
        boolean enoughMoney = accountValidator.validate();
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
        }}

        return booleanNotification;
    }

    @Override
    public Notification<Boolean> processBill(Long cost, Long clientId, Long accountId){
        Notification<Boolean> booleanNotification = new Notification<>();
        Account sourceAccount = new Account();

        boolean enoughMoney = false;
        boolean test = false;
        Date date = new Date();

        try {
            Client client = clientRepository.findClientById(clientId);
        }
        catch (EntityNotFoundException e){
            booleanNotification.addError("The client does not exist!");
            test = true;
        }
        try {
             sourceAccount = accountRepository.findAccountById(accountId);
        }
        catch (EntityNotFoundException e){
            booleanNotification.addError("The acount does not exist!");
            test = true;
        }

        if(test == false){
        AccountValidator accountValidator = new AccountValidator(sourceAccount,cost);
        enoughMoney = accountValidator.validate();

        if(!enoughMoney){
            booleanNotification.setResult(Boolean.FALSE);
            accountValidator.getErrors().forEach(booleanNotification::addError);
        }
        else{
            Account newSourceAccount = new AccountBuilder()
                    .setType(sourceAccount.getType())
                    .setAmountOfMoney(sourceAccount.getAmountOfMoney() - cost)
                    .setDateOfCreation(new java.sql.Date(date.getTime()))
                    .build();
            accountRepository.updateAccount(newSourceAccount,sourceAccount.getId());
            booleanNotification.setResult(Boolean.TRUE);
        }}
        return booleanNotification;
    }

    @Override
    public Notification<Boolean> updateAccount(String type, Long amountOfMoney, Date creationDate, Long accountId){
        Notification<Boolean> booleanNotification = new Notification<>();
        Account sourceAccount= new Account();
        try {
            sourceAccount = accountRepository.findAccountById(accountId);
        }
        catch(EntityNotFoundException e){
            booleanNotification.addError("The account you want to update does not exist!");
        }
        Date date = new Date();
        Account account = new AccountBuilder()
                .setType(type)
                .setAmountOfMoney(amountOfMoney)
                .setDateOfCreation( new java.sql.Date(date.getTime()))
                .build();

        AccountValidator accountValidator = new AccountValidator(account,0l);
        boolean goodAccount = accountValidator.validate();

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
