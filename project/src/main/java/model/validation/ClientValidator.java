package model.validation;

import model.Client;
import model.User;
import repository.client.ClientRepository;

import java.util.ArrayList;
import java.util.List;

public class ClientValidator {

    private final List<String> errors;

    private final Client client;

    public ClientValidator(Client client) {
        this.client=client;
        errors = new ArrayList<>();
    }

    public List<String> getErrors(){
        return errors;
    }

    public boolean validate(){
        validateIdentityCardNumber(client.getIdentityCardNumber());
        validatePersonalNumericalCode(client.getPersonalNumericalCode());
        return errors.isEmpty();
    }

    //IdentityCardNumber must have only digits

    private void validateIdentityCardNumber(Long identityCardNumber){
        int length = String.valueOf(identityCardNumber).length();
        if(length!=5)
            errors.add("The identity card number must be of exactly 13 characters!");
    }

    private void validatePersonalNumericalCode(Long personalNumericalCode){
        int length = String.valueOf(personalNumericalCode).length();
        if(length!=5)
            errors.add("The personal numerical code must be of exactly 13 characters!");
    }

}
