package railway.java;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Класс управляет элементами на окне для редактирования и записей
 */
public class EditController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<?> tableView;

    @FXML
    private Button editButton;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private Button backButton;

    @FXML
    private Label handBookLabel;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'edit.fxml'.";
        assert editButton != null : "fx:id=\"editButton\" was not injected: check your FXML file 'edit.fxml'.";
        assert choiceBox != null : "fx:id=\"choiceBox\" was not injected: check your FXML file 'edit.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'edit.fxml'.";
        assert handBookLabel != null : "fx:id=\"handBookLabel\" was not injected: check your FXML file 'edit.fxml'.";

        Main main = new Main();
        main.defaultView(choiceBox, tableView, "edit");
        tableView.getColumns().get(0).setVisible(false);
        handBookLabel.setVisible(false);
        //элемент для переключения таблиц
        choiceBox.setOnAction(actionEvent -> {
            try {
                main.tableName = choiceBox.getValue();
                main.tableName = main.tableName.replace(" ", "_");
                if (main.tableName.equals("Билеты")) {
                    main.tableName = "ПредставлениеБилетов";
                } else if (main.tableName.equals("Вагоны")) {
                    main.tableName = "ПредставлениеВагонов";
                } else if (main.tableName.equals("Работники")) {
                    main.tableName = "ПредставлениеРаботников";
                } else if (main.tableName.equals("Работники_в_рейсе")) {
                    main.tableName = "ПредставлениеРаботниковВРейсе";
                } else if (main.tableName.equals("Станции_на_маршруте")) {
                    main.tableName = "ПредставлениеСтанцийНаМаршруте";
                } else if (main.tableName.equals("Рейс")) {
                    main.tableName = "ПредставлениеРейсов";
                }
                String sql = "SELECT * FROM " + main.tableName;
                main.buildEditableData(tableView, sql);
                if (Configs.handBooks.contains(main.tableName)) {
                    tableView.getColumns().get(0).setMaxWidth(0);
                    handBookLabel.setVisible(true);
                } else {
                    tableView.getColumns().get(0).setVisible(false);
                    handBookLabel.setVisible(false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        //кнопка для изменения данных таблиц. Если выбрана дочерняя таблица, то будет открываться специальная форма
        editButton.setOnAction(actionEvent -> {
            TableView.TableViewSelectionModel selectionModel = tableView.getSelectionModel();
            Configs.line = (ObservableList<String>) selectionModel.getSelectedItems().get(0);
            Configs.tableName = choiceBox.getValue();
            if (Configs.handBooks.contains(main.tableName)) {
                try {
                    main.tableName = main.tableName.replace(" ", "_");
                    System.out.println("Готова к обновлению");
                    System.out.println(Configs.line.toString());
                    updateHandbooks(main.tableName, Configs.line.get(0), Configs.line.get(1));
                    confirmationWindow();
                    System.out.println("Таблица обновлена");
                } catch (SQLException e) {
                    showError(e.getMessage());
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                String fxml = "../view/editChild.fxml";
                try {
                    editButton.getScene().getWindow().hide();
                    main.openStage(fxml);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String sql = "SELECT * FROM " + main.tableName;
            try {
                main.buildEditableData(tableView, sql);
                tableView.getColumns().get(0).setMaxWidth(0);
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
     * Метод для редактирования таблиц справочников
     * @param table
     * @param id
     * @param value1
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void updateHandbooks(String table, String id, String value1) throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        String columnId = tableView.getColumns().get(0).getText();
        String column1 = tableView.getColumns().get(1).getText();
        String updateQuery = "UPDATE " + table + " SET " + column1 + " = ? WHERE " + columnId + " = " + id;
        PreparedStatement prepStatement = dbHandler.getDbConnection().prepareStatement(updateQuery);
        prepStatement.setString(1, value1);
        prepStatement.executeUpdate();
        System.out.println("Обновление " + table);
    }

    /**
     * Окно с подтверждением
     */
    public void confirmationWindow() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Редактирование");
        alert.setHeaderText("Запись успешно изменена");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                alert.close();
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

