package model.builder;

import model.Account;
import model.Client;

import java.util.List;

public class ClientBuilder {

    private String name;

    private Long identityCardNumber;
    private Long personalNumericalCode;
    private String address;

    List<Account> accountList;

    private Client client;

    public ClientBuilder(){

        client = new Client();
    }

    public ClientBuilder setName(String name){

        client.setName(name);
        return this;
    }

    public ClientBuilder setIdentityCardNumber(Long identityCardNumber){

        client.setIdentityCardNumber(identityCardNumber);
        return this;
    }

    public ClientBuilder setPersonalNumericalCode(Long personalNumericalCode){

        client.setPersonalNumericalCode(personalNumericalCode);
        return this;
    }

    public ClientBuilder setAddress(String address){

        client.setAddress(address);
        return this;
    }

    public ClientBuilder setAccountList(List<Account> accountList){
        client.setAccountList(accountList);
        return this;
    }

    public ClientBuilder setId(Long clientId){

        client.setClientId(clientId);
        return this;
    }

    public Client build(){

        return client;
    }
}

