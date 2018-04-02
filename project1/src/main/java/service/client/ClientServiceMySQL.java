package service.client;

import model.Client;
import model.builder.ClientBuilder;
import model.validation.ClientValidator;
import model.validation.Notification;
import repository.EntityNotFoundException;
import repository.client.ClientRepository;

public class ClientServiceMySQL implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceMySQL(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    @Override
    public Notification<Boolean> addClient(String name, Long identityCardNumber, Long personalNumericalCode, String address) {
        Client client = new ClientBuilder()
                .setName(name)
                .setIdentityCardNumber(identityCardNumber)
                .setPersonalNumericalCode(personalNumericalCode)
                .setAddress(address)
                .build();

        ClientValidator clientValidator = new ClientValidator(client);
        boolean clientValid = clientValidator.validate();
        Notification<Boolean> clientAddNotification = new Notification<>();

        if(!clientValid) {
            clientValidator.getErrors().forEach(clientAddNotification::addError);
            clientAddNotification.setResult(Boolean.FALSE);
        }
        else{
            clientAddNotification.setResult(clientRepository.saveClient(client));
        }
        return clientAddNotification;
    }

    @Override
    public Notification<Boolean> updateClient(String name, Long identityCardNumber, Long personalNumericalCode, String address, Long id) {
        Notification<Boolean> booleanNotification = new Notification<>();
        Client client = new ClientBuilder()
                .setName(name)
                .setIdentityCardNumber(identityCardNumber)
                .setPersonalNumericalCode(personalNumericalCode)
                .setAddress(address)
                .build();
        try {
            Client sourceClient = clientRepository.findClientById(id);
        }
        catch(EntityNotFoundException e){
            booleanNotification.addError("The client you are trying to update does not exist!");
        }
        ClientValidator clientValidator = new ClientValidator(client);

        boolean goodClient = clientValidator.validate();


        if(!goodClient){
            clientValidator.getErrors().forEach(booleanNotification::addError);
            booleanNotification.setResult(Boolean.FALSE);
        }
        else {
            booleanNotification.setResult(clientRepository.updateClient(client,id));
        }

        return booleanNotification;
    }



    @Override
    public Notification<Client> viewClient(Long id) throws EntityNotFoundException{
        Notification<Client> clientViewNotification = new Notification<>();

            clientViewNotification.setResult(clientRepository.findClientById(id));

        return clientViewNotification;
    }
}

