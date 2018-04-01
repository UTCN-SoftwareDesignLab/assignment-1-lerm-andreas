package service.client;

import database.DBConnectionFactory;
import model.Client;
import model.Role;
import model.User;
import model.builder.ClientBuilder;
import model.builder.UserBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.EntityNotFoundException;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepositoryMySQL;
import service.user.AuthenticationServiceMySQL;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Roles.EMPLOYEE;
import static org.junit.Assert.*;

public class ClientServiceMySQLTest {

    public static final  String TEST_NAME ="testName";
    public static final Long TEST_IDENTITY_CARD_NUMBER = 12345l;
    public static final Long TEST_PERSONAL_NUMERICAL_CODE = 12335l;
    public static final  String TEST_ADDRESS ="address";
    private static ClientService clientService;
    private static ClientRepository clientRepository;

    @BeforeClass
    public static void setUp() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(false).getConnection();
        clientRepository = new ClientRepositoryMySQL(connection);

        clientService = new ClientServiceMySQL(
                clientRepository
        );
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
    public void addClient() {
        Assert.assertTrue(clientService.addClient(TEST_NAME,TEST_IDENTITY_CARD_NUMBER,TEST_PERSONAL_NUMERICAL_CODE,TEST_ADDRESS).getResult());
    }

    @Test
    public void updateClient() throws EntityNotFoundException {
        clientService.addClient(TEST_NAME,TEST_IDENTITY_CARD_NUMBER,TEST_PERSONAL_NUMERICAL_CODE,TEST_ADDRESS);
        List<Client> clients = clientRepository.findAll();
        Client client1 = clients.get(clientRepository.findAll().size()-1);
        Long clientId = client1.getClientId();
        Assert.assertTrue(clientService.updateClient(TEST_NAME,TEST_IDENTITY_CARD_NUMBER,TEST_PERSONAL_NUMERICAL_CODE,TEST_ADDRESS,clientId).getResult());
    }

    @Test
    public void viewClient() {
        clientService.addClient(TEST_NAME,TEST_IDENTITY_CARD_NUMBER,TEST_PERSONAL_NUMERICAL_CODE,TEST_ADDRESS);
        List<Client> clients = clientRepository.findAll();
        Client client1 = clients.get(clientRepository.findAll().size()-1);
        assertEquals(client1.getName(),TEST_NAME);
    }
}