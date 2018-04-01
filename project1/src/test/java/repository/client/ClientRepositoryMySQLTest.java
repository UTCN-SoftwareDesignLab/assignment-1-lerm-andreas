package repository.client;

import database.DBConnectionFactory;
import model.Client;
import model.builder.ClientBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.EntityNotFoundException;

import java.sql.Connection;
import java.util.List;

import static org.junit.Assert.*;

public class ClientRepositoryMySQLTest {

    private static ClientRepository clientRepository;


    @BeforeClass
    public static void setupClass() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(false).getConnection();
        clientRepository = new ClientRepositoryMySQL(connection);
    }

    @Before
    public void cleanUp() {
        clientRepository.removeAll();

        Client client = new ClientBuilder()
                .setName("John")
                .setIdentityCardNumber(111111111l)
                .setPersonalNumericalCode(111111111l)
                .setAddress("Street nr 1")
                .build();

        clientRepository.addClient(client);
    }

    @Test
    public void findAll() {
        List<Client> accounts = clientRepository.findAll();
        assertEquals(accounts.size(), 1);
    }

    @Test
    public void addClient() {
        Client client = new ClientBuilder()
                .setName("Chris")
                .setIdentityCardNumber(11l)
                .setPersonalNumericalCode(12l)
                .setAddress("Street nr 1")
                .build();
        clientRepository.addClient(client);
        int size = clientRepository.findAll().size();
        assertEquals(size,2);
    }

    @Test
    public void findClientById() throws EntityNotFoundException {
        Client client = new ClientBuilder()
                .setName("Chris")
                .setIdentityCardNumber(11l)
                .setPersonalNumericalCode(12l)
                .setAddress("Street nr 1")
                .build();
        clientRepository.addClient(client);
        try{
            client = clientRepository.findClientById(clientRepository.findAll().get(clientRepository.findAll().size()-1).getClientId());
        }
        catch (EntityNotFoundException e){

        }

        String name = client.getName();
        assertEquals(name,"Chris");
    }

    @Test
    public void updateClient() throws EntityNotFoundException {
        List<Client> clients = clientRepository.findAll();
        Client client = clients.get(clientRepository.findAll().size()-1);
        Long clientId = client.getClientId();

        Client client1 = new ClientBuilder()
                .setName("Andrew")
                .setIdentityCardNumber(11l)
                .setPersonalNumericalCode(12l)
                .setAddress("Street nr 1")
                .build();
        clientRepository.updateClient(client1,clientId);
        String name = clientRepository.findClientById(clientId).getName();
        assertEquals(name,"Andrew");
    }

    @Test
    public void saveClient() {
        assertTrue(clientRepository.saveClient(new ClientBuilder()
                .setName("Chris")
                .setIdentityCardNumber(11l)
                .setPersonalNumericalCode(12l)
                .setAddress("Street nr 1")
                .build()));
    }
}