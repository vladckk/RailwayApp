package railway.java;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DeleteController {

    ObservableList<String> values = FXCollections.observableArrayList();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<ObservableList<ObservableList<String>>> tableView;

    @FXML
    private Button deleteButton;

    @FXML
    private Button backButton;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'delete.fxml'.";
        assert deleteButton != null : "fx:id=\"deleteButton\" was not injected: check your FXML file 'delete.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'delete.fxml'.";
        assert choiceBox != null : "fx:id=\"choiceBox\" was not injected: check your FXML file 'delete.fxml'.";
        Main main = new Main();
        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
        });
        choiceBox.setItems(Configs.tables);
        choiceBox.setValue(Configs.tables.get(0));
        main.buildData(tableView, "SELECT * FROM ПредставлениеБилетов");
        tableView.getColumns().get(0).setVisible(false);
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
                main.buildData(tableView, sql);
                tableView.getColumns().get(0).setVisible(false);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        TableView.TableViewSelectionModel selectionModel = tableView.getSelectionModel();
        deleteButton.setOnAction(actionEvent -> {
            values = (ObservableList<String>) selectionModel.getSelectedItems().get(0);
            String id = values.get(0);
            System.out.println(id);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Удаление");
            alert.setHeaderText("Вы действительно хотите удалить данную запись?");
            alert.setContentText("Удаление данной записи может повлиять на данные в других таблицах!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                    try {
                        main.tableName = choiceBox.getValue();
                        main.tableName = main.tableName.replace(" ", "_");
                        deleteQuery(main.tableName, id);
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
                        main.buildData(tableView, sql);
                        tableView.getColumns().get(0).setVisible(false);
                    } catch (SQLException e) {
                        showError(e.getMessage());
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (rs == ButtonType.CANCEL) {
                    alert.close();
                    System.out.println("Pressed CANCEL.");
                }
            });
        });
    }

    /**
     * Метод, который удаляет записи из таблиц или представлений(в случае таблицы "Вагоны")
     * @param tableName название таблицы
     * @param code код выбранной записи
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void deleteQuery(String tableName, String code) throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        if (tableName.equals("Вагоны")) {
            tableName = "ПредставлениеВагонов";
        }
        String sql = "DELETE FROM " + tableName + " WHERE Код = ?";
        System.out.println(sql);
        PreparedStatement preparedStatement = dbHandler.getDbConnection().prepareStatement(sql);
        preparedStatement.setString(1, code);
        preparedStatement.executeUpdate();
        System.out.println("Строка успешно удалена");
    }

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
