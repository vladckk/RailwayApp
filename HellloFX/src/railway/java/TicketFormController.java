package railway.java;

import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

public class TicketFormController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DatePicker dateField;

    @FXML
    private TableView<?> tableView;

    @FXML
    private Button showButton;

    @FXML
    private ChoiceBox<String> choiceBox1;

    @FXML
    private TextField percentField;

    @FXML
    private RadioButton rb1;

    @FXML
    private RadioButton rb2;

    @FXML
    private Button countButton;

    @FXML
    private Button backButton;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        assert dateField != null : "fx:id=\"dateField\" was not injected: check your FXML file 'ticketForm.fxml'.";
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'ticketForm.fxml'.";
        assert showButton != null : "fx:id=\"showButton\" was not injected: check your FXML file 'ticketForm.fxml'.";
        assert choiceBox1 != null : "fx:id=\"choiceBox1\" was not injected: check your FXML file 'ticketForm.fxml'.";
        assert rb1 != null : "fx:id=\"rb1\" was not injected: check your FXML file 'ticketForm.fxml'.";
        assert rb2 != null : "fx:id=\"rb2\" was not injected: check your FXML file 'ticketForm.fxml'.";
        assert countButton != null : "fx:id=\"countButton\" was not injected: check your FXML file 'ticketForm.fxml'.";
        assert percentField != null : "fx:id=\"percentField\" was not injected: check your FXML file 'ticketForm.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'ticketForm.fxml'.";
        Main main = new Main();
        main.buildData(tableView, "SELECT * FROM ПредставлениеБилетов");
        ToggleGroup group = new ToggleGroup();
        rb1.setToggleGroup(group);
        rb2.setToggleGroup(group);
        tableView.getColumns().get(0).setVisible(false);

        showButton.setOnAction(actionEvent -> {
            System.out.println(dateField.getValue());
            String date = dateField.getValue().toString();
            System.out.println(date);
            try {
                getTicketsByDate(date);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        main.getField("Типы_Вагонов", choiceBox1);
        countButton.setOnAction(actionEvent -> {
            String percent = percentField.getText();
            float percent1 = Float.parseFloat(percent);
            String type = choiceBox1.getValue();
            if (rb1.isSelected()) {
                percent1 /= 100;
                percent1 = 1 + percent1;
                System.out.println("rb1");
            } else if (rb2.isSelected()) {
                percent1 /= 100;
                percent1 = 1 - percent1;
                System.out.println("rb2");
            }
            String sql = "SELECT * FROM ПредставлениеБилетов";
            try {
                upriseTickets(type, percent1);
                main.buildData(tableView, sql);
                tableView.getColumns().get(0).setVisible(false);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
        });
    }

    /**
     * Поднимает или опускает цену на билеты, используя процедуру ПоднятьЦену
     * @param type тип вагона
     * @param percent коэффициент
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void upriseTickets(String type, float percent) throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        System.out.println(type);
        System.out.println(percent);
        String sql = "{call dbo.ПоднятьЦену(?, ?)}";
        CallableStatement statement = dbHandler.getDbConnection().prepareCall(sql);
        statement.setString(1, type);
        statement.setFloat(2, percent);
        statement.executeUpdate();
    }

    /**
     * Заполняет таблицу, используя процедуру СписокБилетов и дату
     * @param date дата рейса
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void getTicketsByDate(String date) throws SQLException, ClassNotFoundException {
        Main main = new Main();
        DatabaseHandler dbHandler = new DatabaseHandler();
        String sql = "{call dbo.СписокБилетов(?)}";
        CallableStatement statement = dbHandler.getDbConnection().prepareCall(sql);
        statement.setString(1, date);
        ResultSet rs = statement.executeQuery();
        main.buildDataProcedures(tableView, rs);
    }
}
