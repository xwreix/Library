package com.lab.library.dao.DBService;

import com.lab.library.dao.beans.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckElement {
    public static Status checkReader(String email, Connection connection){
        Status result = null;

        try {
            connection.setAutoCommit(false);

            String SQL = "SELECT id FROM reader WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            int idReader = 0;
            if (!resultSet.next()) {
                return Status.NON_EXISTENT;
            } else {
                idReader = resultSet.getInt("id");
            }

            SQL = "SELECT COUNT(*) FROM issue WHERE readerId = ? and returnDate IS NULL";
            statement = connection.prepareStatement(SQL);
            statement.setInt(1, idReader);
            resultSet = statement.executeQuery();

            int count = 0;

            while(resultSet.next()){
                count = resultSet.getInt(1);
            }

            if(count == 0){
                result = Status.AVAIlABLE;
            } else {
                result = Status.LOCKED;
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

        return result;
    }
}
