package repository.client;
import model.Account;
import model.Client;
import repository.EntityNotFoundException;

import java.util.List;

public interface ClientRepository {

   List<Client> findAll();

   void addClient(Client client);

   boolean saveClient(Client client);

   Client findClientById(Long clientId) throws EntityNotFoundException;

   boolean updateClient(Client client,Long clientId);

}
