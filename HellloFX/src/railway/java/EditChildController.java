package railway.java;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Форма для корректировки значений дочерних таблиц
 */
public class EditChildController {

    ObservableList<String> values = FXCollections.observableArrayList();
    ObservableList<String> codeList1 = FXCollections.observableArrayList();
    ObservableList<String> codeList2 = FXCollections.observableArrayList();
    ObservableList<String> codeList3 = FXCollections.observableArrayList();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label label2;

    @FXML
    private TextField textField2;

    @FXML
    private Label label3;

    @FXML
    private TextField textField7;

    @FXML
    private TextField textField3;

    @FXML
    private Label label4;

    @FXML
    private TextField textField4;

    @FXML
    private Label label5;

    @FXML
    private TextField textField5;

    @FXML
    private Label label6;

    @FXML
    private TextField textField6;

    @FXML
    private ChoiceBox<String> choiceBox2;

    @FXML
    private ChoiceBox<String> choiceBox3;

    @FXML
    private ChoiceBox<String> choiceBox6;

    @FXML
    private ChoiceBox<String> choiceBox7;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private Label label7;

    @FXML
    private Button editButton;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        assert label2 != null : "fx:id=\"label1\" was not injected: check your FXML file 'editChild.fxml'.";
        assert textField2 != null : "fx:id=\"textField2\" was not injected: check your FXML file 'editChild.fxml'.";
        assert label3 != null : "fx:id=\"label3\" was not injected: check your FXML file 'editChild.fxml'.";
        assert textField7 != null : "fx:id=\"textField7\" was not injected: check your FXML file 'editChild.fxml'.";
        assert textField3 != null : "fx:id=\"textField3\" was not injected: check your FXML file 'editChild.fxml'.";
        assert label4 != null : "fx:id=\"label4\" was not injected: check your FXML file 'editChild.fxml'.";
        assert textField4 != null : "fx:id=\"textField4\" was not injected: check your FXML file 'editChild.fxml'.";
        assert label5 != null : "fx:id=\"label5\" was not injected: check your FXML file 'editChild.fxml'.";
        assert textField5 != null : "fx:id=\"textField5\" was not injected: check your FXML file 'editChild.fxml'.";
        assert label6 != null : "fx:id=\"label6\" was not injected: check your FXML file 'editChild.fxml'.";
        assert textField6 != null : "fx:id=\"textField6\" was not injected: check your FXML file 'editChild.fxml'.";
        assert choiceBox2 != null : "fx:id=\"choiceBox2\" was not injected: check your FXML file 'editChild.fxml'.";
        assert choiceBox3 != null : "fx:id=\"choiceBox3\" was not injected: check your FXML file 'editChild.fxml'.";
        assert choiceBox6 != null : "fx:id=\"choiceBox6\" was not injected: check your FXML file 'editChild.fxml'.";
        assert choiceBox7 != null : "fx:id=\"choiceBox7\" was not injected: check your FXML file 'editChild.fxml'.";
        assert button2 != null : "fx:id=\"button2\" was not injected: check your FXML file 'editChild.fxml'.";
        assert button3 != null : "fx:id=\"button3\" was not injected: check your FXML file 'editChild.fxml'.";
        assert label7 != null : "fx:id=\"label7\" was not injected: check your FXML file 'editChild.fxml'.";
        assert editButton != null : "fx:id=\"editButton\" was not injected: check your FXML file 'editChild.fxml'.";

        Main main = new Main();
        System.out.println(Configs.tableName);
        System.out.println(Configs.line.toString());
        button2.setVisible(false);
        button3.setVisible(false);
        switch (Configs.tableName) {
            case "Билеты":
                label2.setText("Вагон");
                label3.setText("Цена");
                label4.setText("Номер места");
                label5.setText("ФИО");
                label6.setText("Станция отправления");
                label7.setText("Станция прибытия");
                choiceBox3.setVisible(false);
                button3.setVisible(false);
                values = getLine("Билеты", Configs.line.get(0));
                getNames("ПредставлениеВагонов", choiceBox2, codeList1, values.get(1));
                getNames("Станция", choiceBox6, codeList2, values.get(5));
                getNames("Станция", choiceBox7, codeList3, values.get(6));
                textField3.setText(values.get(2));
                textField4.setText(values.get(3));
                textField5.setText(values.get(4));
                break;
            case "Вагоны":
                label2.setText("Рейс");
                label3.setText("Тип вагона");
                label4.setText("Номер вагона");
                label5.setVisible(false);
                label6.setVisible(false);
                label7.setVisible(false);
                textField5.setVisible(false);
                textField6.setVisible(false);
                textField7.setVisible(false);
                choiceBox6.setVisible(false);
                choiceBox7.setVisible(false);
                button3.setVisible(false);
                values = getLine("Вагоны", Configs.line.get(0));
                getNames("ПредставлениеРейсов", choiceBox2, codeList1, values.get(1));
                getNames("Типы_Вагонов", choiceBox3, codeList2, values.get(2));
                textField4.setText(values.get(3));
                break;
            case "Работники в рейсе":
                label2.setText("Работник");
                label3.setText("Рейс");
                label4.setVisible(false);
                label5.setVisible(false);
                label6.setVisible(false);
                label7.setVisible(false);
                textField2.setVisible(false);
                textField3.setVisible(false);
                textField4.setVisible(false);
                textField5.setVisible(false);
                textField6.setVisible(false);
                textField7.setVisible(false);
                choiceBox6.setVisible(false);
                choiceBox7.setVisible(false);
                values = getLine("Работники_В_Рейсе", Configs.line.get(0));
                getNames("ПредставлениеРаботников", choiceBox2, codeList1, values.get(1));
                getNames("ПредставлениеРейсов", choiceBox3, codeList2, values.get(2));
                break;
            case "Рейс":
                label2.setText("Дата");
                label3.setText("Маршрут");
                setVisibilityTripleTables1();
                values = getLine("Рейс", Configs.line.get(0));
                getNames("Маршрут", choiceBox3, codeList1, values.get(2));
                textField2.setText(values.get(1));
                break;
            case "Работники":
                label2.setText("ФИО");
                label3.setText("Должность");
                setVisibilityTripleTables1();
                values = getLine("Работники", Configs.line.get(0));
                getNames("Должность", choiceBox3, codeList1, values.get(2));
                textField2.setText(values.get(1));
                break;
            case "Станции на маршруте":
                label2.setText("Маршрут");
                label3.setText("Станция");
                label4.setText("Время прибытия");
                label5.setText("Время отправления");
                label6.setText("Номер на маршруте");
                label7.setText("Тип");
                choiceBox6.setVisible(false);
                choiceBox6.setVisible(false);
                choiceBox7.setVisible(false);
                button2.setVisible(false);
                button3.setVisible(false);
                values = getLine("Станции_На_Маршруте", Configs.line.get(0));
                getNames("Маршрут", choiceBox2, codeList1, values.get(1));
                getNames("Станция", choiceBox3, codeList2, values.get(2));
                textField4.setText(values.get(3));
                textField5.setText(values.get(4));
                textField6.setText(values.get(5));
                textField7.setText(values.get(6));
                break;
        }

        editButton.setOnAction(actionEvent -> {
            switch (Configs.tableName) {
                case "Билеты":
                    String codeWagon = codeList1.get(choiceBox2.getSelectionModel().getSelectedIndex());
                    String codeStation1 = codeList2.get(choiceBox6.getSelectionModel().getSelectedIndex());
                    String codeStation2 = codeList3.get(choiceBox7.getSelectionModel().getSelectedIndex());
                    updateTickets(Configs.line.get(0), codeWagon, textField3.getText(), textField4.getText(),
                            textField5.getText(), codeStation1, codeStation2);
                    break;
                case "Вагоны":
                    String flight = choiceBox2.getValue();
                    String date = flight.substring(0, flight.indexOf('|')).trim();
                    String route = flight.substring(flight.indexOf('|') + 2);
                    String type = choiceBox3.getValue();
                    updateWagonView(Configs.line.get(0), date, route, type, textField4.getText());
                    //String codeFlight = codeList1.get(choiceBox2.getSelectionModel().getSelectedIndex());
                    //String codeTypes = codeList2.get(choiceBox3.getSelectionModel().getSelectedIndex());
                    //updateWagon(Configs.line.get(0), codeFlight, codeTypes, textField4.getText());
                    break;
                case "Работники в рейсе":
                    String codePersonal = codeList1.get(choiceBox2.getSelectionModel().getSelectedIndex());
                    String codeFlight = codeList2.get(choiceBox3.getSelectionModel().getSelectedIndex());
                    updateTripleFieldTables("Работники_В_Рейсе", Configs.line.get(0), codePersonal, codeFlight);
                    break;
                case "Рейс":
                    String codeRoute = codeList1.get(choiceBox3.getSelectionModel().getSelectedIndex());
                    updateTripleFieldTables("Рейс", Configs.line.get(0), textField2.getText(), codeRoute);
                    break;
                case "Работники":
                    String codePosition = codeList1.get(choiceBox3.getSelectionModel().getSelectedIndex());
                    updateTripleFieldTables("Работники", Configs.line.get(0), textField2.getText(), codePosition);
                    break;
                case "Станции на маршруте":
                    codeRoute = codeList1.get(choiceBox2.getSelectionModel().getSelectedIndex());
                    String codeStation = codeList2.get(choiceBox3.getSelectionModel().getSelectedIndex());
                    updateStationsOnRoute(Configs.line.get(0), codeRoute, codeStation, textField4.getText(),
                            textField5.getText(), textField6.getText(), textField7.getText());
                    break;
            }
        });
    }


    public void setVisibilityTripleTables1() {
        label4.setVisible(false);
        label5.setVisible(false);
        label6.setVisible(false);
        label7.setVisible(false);
        textField2.setText(Configs.line.get(1));
        textField3.setVisible(false);
        textField4.setVisible(false);
        textField5.setVisible(false);
        textField6.setVisible(false);
        textField7.setVisible(false);
        choiceBox2.setVisible(false);
        choiceBox3.setVisible(true);
        choiceBox6.setVisible(false);
        choiceBox7.setVisible(false);
        button2.setVisible(false);
        button3.setVisible(false);
    }

    //данный метод заносит в элемент ChoiceBox значения из соответствующих представлений
    public void getNames(String tableName, ChoiceBox<String> choice, List<String> codeList, String code) throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet rs = null;
        String sql = "SELECT * FROM " + tableName;
        PreparedStatement preparedStatement = dbHandler.getDbConnection().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ObservableList<String> list = FXCollections.observableArrayList();
        rs = preparedStatement.executeQuery();
        codeList.clear();
        while (rs.next()) {
            codeList.add(rs.getString(1));
        }
        rs.beforeFirst();
        switch (tableName) {
            case "ПредставлениеВагонов":
                while (rs.next()) {
                    String line = rs.getString(2) + " | " + rs.getString(3) + " | " + rs.getString(4)
                            + " | " + rs.getString(5);
                    list.add(line);
                }
                break;
            case "ПредставлениеРейсов":
            case "ПредставлениеРаботников":
                while (rs.next()) {
                    String line = rs.getString(2) + " | " + rs.getString(3);
                    list.add(line);
                }
                break;
            default:
                while (rs.next()) {
                    list.add(rs.getString(2));
                }
                break;
        }
        choice.setItems(list);
        choice.setValue(list.get(codeList.indexOf(code)));
    }

    //получает значения из таблицы, а не представлений по выбранной строке
    public ObservableList<String> getLine(String table, String code) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ObservableList<String> line = FXCollections.observableArrayList();
        try {
            Statement statementCode = dbHandler.getDbConnection().createStatement();
            ResultSet rs = statementCode.executeQuery("SELECT * FROM " + table);
            String id = rs.getMetaData().getColumnName(1);
            String sql = "SELECT * FROM " + table + " WHERE " + id + " = ?";

            PreparedStatement statement = dbHandler.getDbConnection().prepareStatement(sql);
            statement.setString(1, code);
            rs = statement.executeQuery();
            if (rs.next()) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    line.add(rs.getString(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return line;
    }

    /**
     * Метод для обновления данных вагона через представление
     * @param id код
     * @param date дата
     * @param route номер поезда
     * @param type тип вагона
     * @param number номер вагона
     */
    public void updateWagonView(String id, String date, String route, String type, String number) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        try {
            Statement statement = dbHandler.getDbConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ПредставлениеВагонов");
            String columnId = resultSet.getMetaData().getColumnName(1);
            String column1 = resultSet.getMetaData().getColumnName(2);
            String column2 = resultSet.getMetaData().getColumnName(3);
            String column3 = resultSet.getMetaData().getColumnName(4);
            String column4 = resultSet.getMetaData().getColumnName(5);
            System.out.println(date);
            System.out.println(route);
            System.out.println(type);
            System.out.println(number);
            String updateQuery = "UPDATE ПредставлениеВагонов SET " + column1 + " = ?, [" + column2 + "] = ?, [" +
                    column3 + "] = ?, [" + column4 + "] = ? WHERE " + columnId + " = " + id;
            System.out.println(updateQuery);
            PreparedStatement preparedStatement = dbHandler.getDbConnection().prepareStatement(updateQuery);
            preparedStatement.setString(1, date);
            preparedStatement.setString(2, route);
            preparedStatement.setString(3, type);
            preparedStatement.setString(4, number);
            preparedStatement.executeUpdate();
            confirmationWindow();
        } catch (SQLException e) {
            e.printStackTrace();
            showError(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //обновление таблицы Вагоны
    public void updateWagon(String id, String idFlight, String idType, String number) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        try {
            Statement statement = dbHandler.getDbConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Вагоны");
            String columnId = resultSet.getMetaData().getColumnName(1);
            String column1 = resultSet.getMetaData().getColumnName(2);
            String column2 = resultSet.getMetaData().getColumnName(3);
            String column3 = resultSet.getMetaData().getColumnName(4);
            String updateQuery = "UPDATE Вагоны SET " + column1 + " = ?, " +
                    column2 + " = ?, " + column3 + " = ? WHERE " + columnId + " = " + id;
            PreparedStatement prepStatement = dbHandler.getDbConnection().prepareStatement(updateQuery);
            prepStatement.setString(1, idFlight);
            prepStatement.setString(2, idType);
            prepStatement.setString(3, number);
            prepStatement.executeUpdate();
            confirmationWindow();
        } catch (SQLException e) {
            e.printStackTrace();
            showError(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //обновление таблиц с 3 полями
    public void updateTripleFieldTables(String table, String id, String value1, String value2) {
        System.out.println("Обновление " + table);
        DatabaseHandler dbHandler = new DatabaseHandler();
        try {
        Statement statement = dbHandler.getDbConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);
        String columnId = resultSet.getMetaData().getColumnName(1);
        String column1 = resultSet.getMetaData().getColumnName(2);
        String column2 = resultSet.getMetaData().getColumnName(3);
        String updateQuery = "UPDATE " + table + " SET " + column1 + " = ?, " +
                column2 + " = ? WHERE "+ columnId + " = " + id;
            PreparedStatement prepStatement = dbHandler.getDbConnection().prepareStatement(updateQuery);
            prepStatement.setString(1, value1);
            prepStatement.setString(2, value2);
            prepStatement.executeUpdate();
            confirmationWindow();
        } catch (SQLException e) {
            showError(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //метод для обновления записей таблицы Билеты
    public void updateTickets(String id, String idWagon, String price, String number, String fio, String idDepartureStation, String idArrivalStation) {
        if (Configs.checkRegister(fio)) {
            DatabaseHandler dbHandler = new DatabaseHandler();
            try {
                Statement statement = dbHandler.getDbConnection().createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Билеты");
                String columnId = resultSet.getMetaData().getColumnName(1);
                String column1 = resultSet.getMetaData().getColumnName(2);
                String column2 = resultSet.getMetaData().getColumnName(3);
                String column3 = resultSet.getMetaData().getColumnName(4);
                String column4 = resultSet.getMetaData().getColumnName(5);
                String column5 = resultSet.getMetaData().getColumnName(6);
                String column6 = resultSet.getMetaData().getColumnName(7);
                String updateQuery = "UPDATE Билеты SET " + column1 + " = ?, " + column2 + " = ?, " +
                        column3 + " = ?, " + column4 + " = ?, " + column5 + " = ?, " + column6 + " = ? " +
                        "WHERE " + columnId + " = " + id;
                PreparedStatement prepStatement = dbHandler.getDbConnection().prepareStatement(updateQuery);
                prepStatement.setString(1, idWagon);
                prepStatement.setString(2, price);
                prepStatement.setString(3, number);
                prepStatement.setString(4, fio);
                prepStatement.setString(5, idDepartureStation);
                prepStatement.setString(6, idArrivalStation);
                prepStatement.executeUpdate();
                confirmationWindow();
            } catch (SQLException e) {
                showError(e.getMessage());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Неккоректная фамилия");
        }
    }

    /**
     * Метод для обновления записей в таблице станции на маршруте
     * @param id
     * @param idRoute
     * @param idStation
     * @param arrivalTime
     * @param departureTime
     * @param numberOnRoute
     * @param type
     */
    public void updateStationsOnRoute(String id, String idRoute, String idStation, String arrivalTime, String departureTime, String numberOnRoute, String type) {
        if (Configs.checkTime(arrivalTime) && Configs.checkTime(departureTime)) {
            DatabaseHandler dbHandler = new DatabaseHandler();
            try {
                Statement statement = dbHandler.getDbConnection().createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Станции_На_Маршруте");
                String columnId = resultSet.getMetaData().getColumnName(1);
                String column1 = resultSet.getMetaData().getColumnName(2);
                String column2 = resultSet.getMetaData().getColumnName(3);
                String column3 = resultSet.getMetaData().getColumnName(4);
                String column4 = resultSet.getMetaData().getColumnName(5);
                String column5 = resultSet.getMetaData().getColumnName(6);
                String column6 = resultSet.getMetaData().getColumnName(7);
            String updateQuery = "UPDATE Станции_На_Маршруте SET " + column1 + " = ?, " + column2 + " = ?, " + column3 + " = ?, " +
                    column4 + " = ?, " + column5 + " = ?, " + column6 + " = ? WHERE " + columnId + " = " + id;
                PreparedStatement prepStatement = dbHandler.getDbConnection().prepareStatement(updateQuery);
                prepStatement.setString(1, idRoute);
                prepStatement.setString(2, idStation);
                prepStatement.setString(3, arrivalTime);
                prepStatement.setString(4, departureTime);
                prepStatement.setString(5, numberOnRoute);
                prepStatement.setString(6, type);
                prepStatement.executeUpdate();
                confirmationWindow();
            } catch (SQLException e) {
                showError(e.getMessage());
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Ошибка во времени");
        }
    }

    /**
     * Окно подтверждения
     */
    public void confirmationWindow() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Редактирование");
        alert.setHeaderText("Запись успешно изменена");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                alert.close();
                editButton.getScene().getWindow().hide();
                Main main = new Main();
                String fxml = "../view/edit.fxml";
                try {
                    main.openStage(fxml);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Окно с ошибкой
     * @param message сообщение ошибки
     */
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(message);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                alert.close();
            }
        });
    }
}
