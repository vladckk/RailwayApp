package railway.java;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;

public class RouteFormController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private TableView<?> tableView1;

    @FXML
    private TableView<?> tableView2;

    @FXML
    private Button backButton;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        assert choiceBox != null : "fx:id=\"choiceBox\" was not injected: check your FXML file 'routeForm.fxml'.";
        assert tableView1 != null : "fx:id=\"tableView1\" was not injected: check your FXML file 'routeForm.fxml'.";
        assert tableView2 != null : "fx:id=\"tableView2\" was not injected: check your FXML file 'routeForm.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'routeForm.fxml'.";
        Main main = new Main();
        main.getField("Маршрут", choiceBox);
        String numb = choiceBox.getValue();
        getWagons(numb);
        getStationsOnRoute(numb);
        choiceBox.setOnAction(actionEvent -> {
            String number = choiceBox.getValue();
            System.out.println(number);
            try {
                getWagons(number);
                getStationsOnRoute(number);
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
     * Заполняет таблицу, используя процедуру СписокВагоновНаМаршрут(Номер поезда)
     * @param number номер поезда
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void getWagons(String number) throws SQLException, ClassNotFoundException {
        Main main = new Main();
        DatabaseHandler dbHandler = new DatabaseHandler();
        String sql = "{call dbo.СписокВагоновНаМаршрут(?)}";
        CallableStatement statement = dbHandler.getDbConnection().prepareCall(sql);
        statement.setString(1, number);
        ResultSet rs = statement.executeQuery();
        main.buildDataProcedures(tableView1, rs);
    }

    /**
     * Заполняет таблицу, используя процедуру СписокСтанцийМаршрута
     * @param number номер поезда
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void getStationsOnRoute(String number) throws SQLException, ClassNotFoundException {
        Main main = new Main();
        DatabaseHandler dbHandler = new DatabaseHandler();
        String sql = "{call dbo.СписокСтанцийМаршрута(?)}";
        CallableStatement statement = dbHandler.getDbConnection().prepareCall(sql);
        statement.setString(1, number);
        ResultSet rs = statement.executeQuery();
        main.buildDataProcedures(tableView2, rs);
    }

}
