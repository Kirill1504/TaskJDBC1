package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static UserDao daoInstatnce = null;
    private static Connection connection;

    private UserDaoJDBCImpl() {

    }

    private UserDaoJDBCImpl(Connection connection) {
        connection = Util.getConnection();
        UserDaoJDBCImpl.connection = connection;
    }

    public static UserDao getDaoJDBCInstatnce() {
        if (daoInstatnce == null) {
            daoInstatnce = new UserDaoJDBCImpl(Util.getConnection());
        }
        return daoInstatnce;
    }

    public void createUsersTable() {

        try (Statement statement = connection.createStatement()) {
            int res = statement.executeUpdate("CREATE TABLE IF NOT EXISTS users(id BIGINT NOT NULL AUTO_INCREMENT" +
                    ", name VARCHAR(60)" +
                    ", lastName VARCHAR(60)" +
                    ", age TINYINT" +
                    ", PRIMARY KEY (id))");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void dropUsersTable() {

        try (Statement statement = connection.createStatement()) {
            int res = statement.executeUpdate("DROP TABLE IF EXISTS users");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(name, lastName, age) " +
                "VALUES(?, ?, ?)")) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.execute();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void removeUserById(long id) {

        try (Statement statement = Util.getConnection().createStatement()) {
            connection.setAutoCommit(false);
            int res = statement.executeUpdate("DELETE FROM users WHERE id = " + id);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        ResultSet resultSet = null;

        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            resultSet = statement.executeQuery("SELECT * FROM users");
            int columns = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                list.add(new User(resultSet.getString(2), resultSet.getString(3),
                        resultSet.getByte(4)));
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return list;
    }

    public void cleanUsersTable() {

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE users");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
