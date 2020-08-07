package railway.java;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class EditChildTableController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<?> tableView;

    @FXML
    private Button buttonOK;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'editChildTable.fxml'.";
        assert buttonOK != null : "fx:id=\"buttonOK\" was not injected: check your FXML file 'editChildTable.fxml'.";
        Main main = new Main();
        if (Configs.tableName.equals("Билеты")) {
            main.buildData(tableView, "SELECT * FROM ПредставлениеВагонов");
        } else if (Configs.tableName.equals("Вагоны")) {
            main.buildData(tableView, "SELECT * FROM ПредставлениеРейсов");
        } else {
            if (Configs.typeTable == 1) {
                main.buildData(tableView, "SELECT * FROM ПредставлениеРаботников");
            } else if (Configs.typeTable == 2) {
                main.buildData(tableView, "SELECT * FROM ПредставлениеРейсов");
            }
        }
        //tableView.getColumns().get(0).setVisible(false);
        buttonOK.setOnAction(actionEvent -> {
            TableView.TableViewSelectionModel selectionModel = tableView.getSelectionModel();
            ObservableList<String> list = (ObservableList<String>) selectionModel.getSelectedItems().get(0);
            System.out.println(list.get(0));
            Configs.selectedId = list.get(0);
            Stage stage = (Stage) buttonOK.getScene().getWindow();
            Stage owner = (Stage) stage.getOwner();
            Scene scene = owner.getScene();
            Parent root = scene.getRoot();
            ChoiceBox<String> choiceBox = (ChoiceBox<String>) root.lookup("choiceBox2");
            choiceBox.setValue("ajslkfj");
        });
    }
}
