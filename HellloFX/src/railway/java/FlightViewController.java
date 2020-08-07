package railway.java;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * Класс с окном для просмотров информации о рейсах
 */
public class FlightViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<?> tableView;

    @FXML
    private Button backButton;

    @FXML
    private Label countClientsLabel;

    @FXML
    private TableView<?> tableView2;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'flightView.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'flightView.fxml'.";
        assert countClientsLabel != null : "fx:id=\"countClientsLabel\" was not injected: check your FXML file 'flightView.fxml'.";
        Main main = new Main();
        String fullName = DatabaseHandler.getFullNameUser(Configs.userLogin);
        System.out.println(fullName);
        String code = DatabaseHandler.getWorkerCode(fullName);
        System.out.println(code);
        //выводим таблицу, полученную в функции СписокРейсов
        String sql = "SELECT * FROM СписокРейсов(" + code + ")";
        main.buildData(tableView, sql);
        countClientsLabel.setText(getClientsCount(code));
        String sql2 = "SELECT * FROM СписокРаботниковРейса()";
        main.buildData(tableView2, sql2);
        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
        });
    }

    /**
     * Этот метод получает значение из процедуры
     * @param code
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String getClientsCount(String code) throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        String sql = "{? = call dbo.КоличествоПассажиров(?)}";
        CallableStatement statement = dbHandler.getDbConnection().prepareCall(sql);
        statement.registerOutParameter(1, Types.VARCHAR);
        statement.setInt(2, Integer.parseInt(code));
        statement.execute();
        return statement.getString(1);
    }
}
