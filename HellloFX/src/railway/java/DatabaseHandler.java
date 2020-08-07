package railway.java;

import java.sql.*;

/**
 * Класс который содержит методы для работы с базой данных и соединяет приложение с базой данных
 */
public class DatabaseHandler extends Configs{

    Connection dbConnection;

    /**
     * Метод возвращает значение соединения с базой данных
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Connection getDbConnection() throws SQLException, ClassNotFoundException {
        String connectionString = "jdbc:sqlserver://localhost:1433;database=Вокзал";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
        return dbConnection;
    }

    /**
     * Метод для регистрации пользователя и занесения его данных и таблицу users
     * @param user
     * @param password
     * @param fio
     * @param type
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public boolean signUpUser(String user, String password, String fio, int type) throws ClassNotFoundException, SQLException {
        String insertQuery = "INSERT INTO users VALUES('" + user + "', '"
             + password + "', '" + fio + "', " + type + ")";
        Statement statement = getDbConnection().createStatement();
        try {
            statement.execute(insertQuery);
            return true;
        } catch (SQLException e) {
            System.out.println("Данный логин уже занят");
            return false;
        }
    }

    /**
     * Метод для авторизации, который проверяет логин и пароль
     * @param user
     * @param password
     * @return
     */
    public ResultSet getUser(String user, String password) {
        ResultSet resultSet = null;
        String select = "SELECT * FROM users WHERE login=? and password=?";
        try {
            PreparedStatement prepStatement = getDbConnection().prepareStatement(select);
            prepStatement.setString(1, user);
            prepStatement.setString(2, password);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * Возвращает значения типа аккаунта
     * @param login
     * @return
     */
    public int getType(String login) {
        ResultSet rs = null;
        String sql = "SELECT * FROM users WHERE login=?";
        try {
            PreparedStatement prepStatement = getDbConnection().prepareStatement(sql);
            prepStatement.setString(1, login);
            rs = prepStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Возвращает код работника с помощью значения ФИО, полученного с аккаунта
     * @param name
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static String getWorkerCode(String name) throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        String sql = "SELECT * FROM Работники WHERE ФИО = ?";
        PreparedStatement statement = dbHandler.getDbConnection().prepareStatement(sql);
        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return rs.getString(1);
        } else {
            return null;
        }
    }

    /**
     * Метод возвращает ФИО пользователя
     * @param login
     * @return ФИО пользователя
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static String getFullNameUser(String login) throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        String sql = "SELECT * FROM users WHERE login = ?";
        PreparedStatement statement = dbHandler.getDbConnection().prepareStatement(sql);
        statement.setString(1, login);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return rs.getString(4);
        } else {
            return null;
        }
    }
}
