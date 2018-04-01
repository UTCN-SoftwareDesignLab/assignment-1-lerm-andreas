package model.validation;



import model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountValidator {
    private final List<String> errors;

    private final Account account;

    private final Long cost;

    public AccountValidator(Account account,Long cost) {
        this.account=account;
        this.cost = cost;
        errors = new ArrayList<>();
    }

    public List<String> getErrors(){
        return errors;
    }

    public boolean validate(){
        validateType(account.getType());
        validateAmountOfMoney(account.getAmountOfMoney(),cost);
        return errors.isEmpty();
    }

    private void validateType(String type){
        if(!type.equals("saving") && !type.equals("spending"))
            errors.add("There are only 2 types of account you can add: saving or spending!");
    }

    private void validateAmountOfMoney(Long availableAmountOfMoney,Long cost){
        if(cost > availableAmountOfMoney)
            errors.add("There is not enough money in the account");
    }
}
