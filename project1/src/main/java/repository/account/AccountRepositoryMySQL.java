package repository.account;

import model.Account;
import model.builder.AccountBuilder;
import repository.EntityNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;

import static database.Constants.Tables.ACCOUNT;

public class AccountRepositoryMySQL implements AccountRepository {

    private final Connection connection;

    public AccountRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Account> findAll() {

        List<Account> accounts = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "Select * from account";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                accounts.add(getAccountFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    @Override
    public boolean addAccountToClient(Account account, Long clientId) {

        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO " + ACCOUNT + " values (null, ?, ?, ?,?)");
            insertStatement.setLong(1, clientId);
            insertStatement.setString(2, account.getType());
            insertStatement.setLong(3,account.getAmountOfMoney());
            insertStatement.setDate(4, new java.sql.Date(account.getDateOfCreation().getTime()));
            insertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Account findAccountById(Long accountId)throws EntityNotFoundException {
        try {
            Statement statement = connection.createStatement();
            String sql = "Select * from account where id=" + accountId;
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                return getAccountFromResultSet(rs);
            } else {
                throw new EntityNotFoundException(accountId, Account.class.getSimpleName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntityNotFoundException(accountId, Account.class.getSimpleName());
        }
    }

    @Override
    public void updateAccount(Account account,Long accountId) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("UPDATE account SET type= ?, amount_of_money= ?, date_of_creation= ? WHERE id = ?");
            insertStatement.setString(1, account.getType());
            insertStatement.setLong(2, account.getAmountOfMoney());
            insertStatement.setDate(3,new java.sql.Date(account.getDateOfCreation().getTime()));
            insertStatement.setLong(4, accountId);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAccount(Long  accountId) {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from account where id =" + accountId;
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Account getAccountFromResultSet(ResultSet rs) throws SQLException {
        return new AccountBuilder()
                .setId(rs.getLong("id"))
                .setType(rs.getString("type"))
                .setAmountOfMoney(rs.getLong("amount_of_money"))
                .setDateOfCreation(new java.sql.Date(rs.getDate("date_of_creation").getTime()))
                .build();
    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from account where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


