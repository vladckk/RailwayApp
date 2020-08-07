package railway.java;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import microsoft.sql.Types;

public class TypeFormController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private TableView<?> tableView;

    @FXML
    private Label resultLabel;

    @FXML
    private Button backButton;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        assert choiceBox != null : "fx:id=\"choiceBox\" was not injected: check your FXML file 'typeForm.fxml'.";
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'typeForm.fxml'.";
        assert resultLabel != null : "fx:id=\"resultLabel\" was not injected: check your FXML file 'typeForm.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'typeForm.fxml'.";
        Main main = new Main();
        main.getField("Типы_Вагонов", choiceBox);
        String type = choiceBox.getValue();
        int cost = getCheapestTicket(type);
        resultLabel.setText(String.valueOf(cost));
        getTypesAverage();
        choiceBox.setOnAction(actionEvent -> {
            String chosenType = choiceBox.getValue();
            int costOfType = getCheapestTicket(chosenType);
            resultLabel.setText(String.valueOf(costOfType));
        });
        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
        });
    }

    /**
     * Заполняет таблицу, используя функцию СредняяСтоимостьТипа
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void getTypesAverage() throws SQLException, ClassNotFoundException {
        Main main = new Main();
        DatabaseHandler dbHandler = new DatabaseHandler();
        String sql = "SELECT Тип_Вагона, dbo.СредняяСтоимостьТипа(Код) as [Средняя цена] FROM Типы_Вагонов";
        Statement statement = dbHandler.getDbConnection().createStatement();
        ResultSet rs = statement.executeQuery(sql);
        main.buildDataProcedures(tableView, rs);
    }

    /**
     * Возвращает стоимость используя процедуру СамыйДешёвыйБилет
     * @param type тип вагона
     * @return
     */
    public int getCheapestTicket(String type) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        String sql = "{? = call dbo.СамыйДешёвыйБилет(?)}";
        int cost = 0;
        try {
            CallableStatement statement = dbHandler.getDbConnection().prepareCall(sql);
            statement.registerOutParameter(1, Types.MONEY);
            statement.setString(2, type);
            statement.execute();
            cost = statement.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cost;
    }
}
