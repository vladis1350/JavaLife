package com.vladis1350.db_configuration;

import com.vladis1350.bean.Human;
import com.vladis1350.constants.Constant;

import java.sql.*;

public class DatabaseHandler extends DBConfig {
    Connection dbConnection;
    ResultSet resultSet;
    Statement statement;

    public Connection getDbConnection() throws SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?useUnicode=true&serverTimezone=UTC&useSSL=true&verifyServerCertificate=false";
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public void save(Human human) throws SQLException {
        String insert = "INSERT INTO " + Constant.HUMAN_TABLE + "(" + Constant.HUMANS_NAME + "," + Constant.HUMANS_AGE +
                ", " + Constant.HUMANS_DATE + ")" + "VALUES(?,?,?)";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
        preparedStatement.setString(1, human.getName());
        preparedStatement.setString(2, String.valueOf(human.getAge()));
        preparedStatement.setString(3, String.valueOf(human.getBirthday()));

        preparedStatement.executeUpdate();
    }

    public ResultSet findAll() throws SQLException {
        dbConnection = getDbConnection();
        return resultSet = dbConnection.createStatement().executeQuery("SELECT * FROM human");
    }

    public void delete(Long id) throws SQLException {
        dbConnection = getDbConnection();
        statement = dbConnection.createStatement();
        statement.executeUpdate(" DELETE FROM human WHERE id_human=" + id);
    }

    public void update(Human human) throws SQLException {
        dbConnection = getDbConnection();
        statement = dbConnection.createStatement();
        statement.executeUpdate(" UPDATE human SET name='" + human.getName() + "', age='" + human.getAge() + "', birthday='" +
                human.getBirthday() + "'  WHERE id_human=" + human.getId() + "");
    }
}
