package service.client;

import model.Client;
import model.validation.Notification;
import repository.EntityNotFoundException;

public interface ClientService {

    Notification<Boolean> addClient(String name, Long identityCardNumber, Long personalNumericalCode, String address);

    Notification<Boolean> updateClient(String name, Long identityCardNumber, Long personalNumericalCode, String address, Long id) throws EntityNotFoundException;

    Notification<Client> viewClient(Long id) throws EntityNotFoundException;

}
