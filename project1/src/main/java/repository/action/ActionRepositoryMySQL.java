package repository.action;

import model.Account;
import model.Action;
import model.Client;
import model.builder.ActionBuilder;
import model.builder.ClientBuilder;
import repository.EntityNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActionRepositoryMySQL implements ActionRepository {
    //add Action
    //get Actions for user

    private final Connection connection;

    public ActionRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean saveAction(Action action){
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO action" + " values (null, ?, ?, ?)");
            insertStatement.setString(1,action.getDescription());
            insertStatement.setLong(2, action.getUserId());
            insertStatement.setDate(3, new java.sql.Date(action.getDate().getTime()));
            insertStatement.executeUpdate();
            return  true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }
    }
    @Override
    public List<Action> findActionsForUser(Long userId) throws EntityNotFoundException {
        List<Action> actions = new ArrayList<>();
        try {

            Statement statement = connection.createStatement();
            String sql = "Select * from action where user_id =" + userId;
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                actions.add(getActionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return actions;
    }

    private Action getActionFromResultSet(ResultSet rs) throws SQLException {
        return new ActionBuilder()
                .setDescription(rs.getString("description"))
                .setDate(new java.sql.Date(rs.getDate("date").getTime()))
                .setUserId(rs.getLong("user_id"))
                .build();
    }
}
