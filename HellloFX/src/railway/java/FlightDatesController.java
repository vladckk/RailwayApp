package railway.java;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

/**
 * Класс отображает элементы для окна с расписанием рейсов
 */
public class FlightDatesController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<?> tableView1;

    @FXML
    private TableView<?> tableView2;

    @FXML
    private Label currentDateLabel;

    @FXML
    private Button backButton;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        assert tableView1 != null : "fx:id=\"tableView1\" was not injected: check your FXML file 'flightDates.fxml'.";
        assert tableView2 != null : "fx:id=\"tableView2\" was not injected: check your FXML file 'flightDates.fxml'.";
        assert currentDateLabel != null : "fx:id=\"currentDateLabel\" was not injected: check your FXML file 'flightDates.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'flightDates.fxml'.";

        currentDateLabel.setText(String.valueOf(LocalDate.now()));
        String currentDate = String.valueOf(LocalDate.now());
        Main main = new Main();
        String fullName = DatabaseHandler.getFullNameUser(Configs.userLogin);
        String code = DatabaseHandler.getWorkerCode(fullName);

        main.buildData(tableView1, "SELECT Дата, [Номер Поезда] FROM Работники_В_Рейсе, ПредставлениеРейсов WHERE Дата < '" + currentDate
            + "' AND Код_Работника = " + code + " AND ПредставлениеРейсов.Код = Код_Рейса");
        main.buildData(tableView2, "SELECT Дата, [Номер Поезда] FROM Работники_В_Рейсе, ПредставлениеРейсов WHERE Дата > '" + currentDate
            + "' AND Код_Работника = " + code + " AND ПредставлениеРейсов.Код = Код_Рейса");

        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
        });
    }
}
