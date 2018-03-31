package repository.client;

import model.Account;
import model.Book;
import model.Client;
import model.builder.ClientBuilder;
import repository.EntityNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.CLIENT;

public class ClientRepositoryMySQL implements ClientRepository{
    private final Connection connection;

    public ClientRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "Select * from client";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                clients.add(getClientFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }

    @Override
    public void addClient(Client client) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO " + CLIENT + " values (null, ?, ?, ?, ?)");
            insertStatement.setString(1, client.getName());
            insertStatement.setLong(2, client.getIdentityCardNumber());
            insertStatement.setLong(3,client.getPersonalNumericalCode());
            insertStatement.setString(4, client.getAddress());
            insertStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }


    @Override
    public Client findClientById(Long clientId) throws EntityNotFoundException {

        try {
            Statement statement = connection.createStatement();
            String sql = "Select * from client where client_id =" + clientId;
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                return getClientFromResultSet(rs);
            } else {
                throw new EntityNotFoundException(clientId, Client.class.getSimpleName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntityNotFoundException(clientId, Client.class.getSimpleName());
        }
    }

    @Override
    public boolean updateClient(Client client,Long clientId) {
        //private static final String updateStatementString = "UPDATE product SET productPrice =?, productName = ?, brand = ?, model = ?, nrInStock = ? WHERE productId = ? ";
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("UPDATE client SET name = ?, identity_card_number= ?, personal_numerical_code= ?, address= ? WHERE client_id = ?");
            insertStatement.setString(1,client.getName());
            insertStatement.setLong(2, client.getIdentityCardNumber());
            insertStatement.setLong(3, client.getPersonalNumericalCode());
            insertStatement.setString(4, client.getAddress());
            insertStatement.setLong(5, clientId);
            insertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return true;
        }
    }

    private Client getClientFromResultSet(ResultSet rs) throws SQLException {
        return new ClientBuilder()
                .setId(rs.getLong("client_id"))
                .setName(rs.getString("name"))
                .setIdentityCardNumber(rs.getLong("identity_card_number"))
                .setPersonalNumericalCode(rs.getLong("personal_numerical_code"))
                .setAddress(rs.getString("address"))
                .build();
    }

    public boolean saveClient(Client client) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO " + CLIENT + " values (null, ?, ?, ?, ?)");
            insertStatement.setString(1, client.getName());
            insertStatement.setLong(2, client.getIdentityCardNumber());
            insertStatement.setLong(3,client.getPersonalNumericalCode());
            insertStatement.setString(4, client.getAddress());
            insertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
