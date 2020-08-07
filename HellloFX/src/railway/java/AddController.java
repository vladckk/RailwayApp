package railway.java;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;

/**
 * Управляет элементами на окне с добавлением записи для таблиц-справочников
 */
public class AddController {

    ObservableList<String> values = FXCollections.observableArrayList();
    ObservableList<String> codeList1 = FXCollections.observableArrayList();
    ObservableList<String> codeList2 = FXCollections.observableArrayList();
    ObservableList<String> codeList3 = FXCollections.observableArrayList();
    String number = null;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<?> tableView;

    @FXML
    private Button addButton;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private Button backButton;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'add.fxml'.";
        assert addButton != null : "fx:id=\"addButton\" was not injected: check your FXML file 'add.fxml'.";
        assert choiceBox != null : "fx:id=\"choiceBox\" was not injected: check your FXML file 'add.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'add.fxml'.";
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.setOnKeyPressed(keyEvent -> {
            TablePosition tp = tableView.getFocusModel().getFocusedCell();
            if (keyEvent.getCode() == KeyCode.TAB) {
                tableView.getFocusModel().focusRightCell();
                Robot r = new Robot();
                r.keyPress(KeyCode.ENTER);
                TablePosition tpos = tableView.getFocusModel().getFocusedCell();
                tableView.edit(tpos.getRow(), tpos.getTableColumn());
            }
        });
        Main main = new Main();
        main.defaultView(choiceBox, tableView, "add");
        tableView.getColumns().get(0).setVisible(false);
        TableView.TableViewSelectionModel selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((ChangeListener<ObservableList<String>>) (val, oldval, newval) -> {
            if (newval!=null) {
                number = newval.get(0);
                if (number.equals("(№)")) {
                    int lastRow = tableView.getItems().size() - 1;
                    System.out.println(((ObservableList<String>) tableView.getItems().get(lastRow)).set(1, null));
                }
            }
        });

        addButton.setOnAction(actionEvent -> {
            if (Configs.handBooks.contains(main.tableName)) {
                int size = tableView.getItems().size() - 1;
                values = (ObservableList<String>) tableView.getItems().get(size);
                try {
                    addQuery(main.tableName, values);
                    confirmationWindow();
                } catch (SQLException e) {
                    showError(e.getMessage());
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                String sql = "SELECT * FROM " + main.tableName;
                try {
                    main.buildAddableData(tableView, sql, main.tableName);
                    tableView.getColumns().get(0).setVisible(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                String fxml = "../view/addChild.fxml";
                addButton.getScene().getWindow().hide();
                Configs.tableName = choiceBox.getValue();
                try {
                    main.openStage(fxml);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
                main.buildAddableData(tableView, sql, main.tableName);
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
     * Билдер для построения запросов на добавления записей в различные таблицы
     * @param tableName
     * @param count
     * @return
     */
    public String insertQueryBuilder(String tableName, int count) {
        StringBuilder sb = new StringBuilder("INSERT INTO ").append(tableName).append(" VALUES(");
        sb.append("?, ".repeat(Math.max(0, count)));
        sb.delete(sb.length() - 2, sb.length());
        sb.append(")");
        return sb.toString();
    }

    /**
     * Метод выполняет занесение записи в таблицу
     * @param tableName
     * @param value
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void addQuery(String tableName, List<String> value) throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        int length = values.size();
        String sql = insertQueryBuilder(tableName, length - 1);
        System.out.println(sql);
        PreparedStatement preparedStatement = dbHandler.getDbConnection().prepareStatement(sql);
        for(int i = 1; i < length; i++) {
            if (value.get(i - 1) == null) {
                preparedStatement.setInt(i, 0);
            } else {
                preparedStatement.setString(i, value.get(i - 1));
            }
        }

        preparedStatement.executeUpdate();
        System.out.println("Таблица успешно обновлена");
    }

    /**
     * Окно подтверждения
     */
    public void confirmationWindow() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Добавление");
        alert.setHeaderText("Запись успешно добавлена");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                alert.close();
            }
        });
    }

    /**
     * Окно с ошибкой
     * @param message значение сообщения
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
