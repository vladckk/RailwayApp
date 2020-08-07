package railway.java;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Класс управляет элементами на форме для добавления записей в дочерние талбицы
 */
public class AddChildController {

    ObservableList<String> list = FXCollections.observableArrayList();
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
    private Label label7;

    @FXML
    private Button addButton;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        assert label2 != null : "fx:id=\"label2\" was not injected: check your FXML file 'addChild.fxml'.";
        assert textField2 != null : "fx:id=\"textField2\" was not injected: check your FXML file 'addChild.fxml'.";
        assert label3 != null : "fx:id=\"label3\" was not injected: check your FXML file 'addChild.fxml'.";
        assert textField7 != null : "fx:id=\"textField7\" was not injected: check your FXML file 'addChild.fxml'.";
        assert textField3 != null : "fx:id=\"textField3\" was not injected: check your FXML file 'addChild.fxml'.";
        assert label4 != null : "fx:id=\"label4\" was not injected: check your FXML file 'addChild.fxml'.";
        assert textField4 != null : "fx:id=\"textField4\" was not injected: check your FXML file 'addChild.fxml'.";
        assert label5 != null : "fx:id=\"label5\" was not injected: check your FXML file 'addChild.fxml'.";
        assert textField5 != null : "fx:id=\"textField5\" was not injected: check your FXML file 'addChild.fxml'.";
        assert label6 != null : "fx:id=\"label6\" was not injected: check your FXML file 'addChild.fxml'.";
        assert textField6 != null : "fx:id=\"textField6\" was not injected: check your FXML file 'addChild.fxml'.";
        assert choiceBox2 != null : "fx:id=\"choiceBox2\" was not injected: check your FXML file 'addChild.fxml'.";
        assert choiceBox3 != null : "fx:id=\"choiceBox3\" was not injected: check your FXML file 'addChild.fxml'.";
        assert choiceBox6 != null : "fx:id=\"choiceBox6\" was not injected: check your FXML file 'addChild.fxml'.";
        assert choiceBox7 != null : "fx:id=\"choiceBox7\" was not injected: check your FXML file 'addChild.fxml'.";
        assert label7 != null : "fx:id=\"label7\" was not injected: check your FXML file 'addChild.fxml'.";
        assert addButton != null : "fx:id=\"addButton\" was not injected: check your FXML file 'addChild.fxml'.";

        Main main = new Main();
        System.out.println(Configs.tableName);
        switch (Configs.tableName) {
            case "Билеты":
                label2.setText("Вагон");
                label3.setText("Цена");
                label4.setText("Номер места");
                label5.setText("ФИО");
                label6.setText("Станция отправления");
                label7.setText("Станция прибытия");
                choiceBox3.setVisible(false);
                getNames("ПредставлениеВагонов", choiceBox2, codeList1);
                getNames("Станция", choiceBox6, codeList2);
                getNames("Станция", choiceBox7, codeList3);
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
                getNames("ПредставлениеРейсов", choiceBox2, codeList1);
                getNames("Типы_Вагонов", choiceBox3, codeList2);
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
                getNames("ПредставлениеРаботников", choiceBox2, codeList1);
                getNames("ПредставлениеРейсов", choiceBox3, codeList2);
                break;
            case "Рейс":
                label2.setText("Дата");
                label3.setText("Маршрут");
                setVisibilityTripleTables1();
                getNames("Маршрут", choiceBox3, codeList1);
                break;
            case "Работники":
                label2.setText("ФИО");
                label3.setText("Должность");
                setVisibilityTripleTables1();
                getNames("Должность", choiceBox3, codeList1);
                break;
            case "Станции на маршруте":
                label2.setText("Маршрут");
                label3.setText("Станция");
                label4.setText("Время прибытия");
                label5.setText("Время отправления");
                label6.setText("Номер на маршруте");
                label7.setText("Тип");
                textField4.setText(null);
                textField5.setText(null);
                choiceBox6.setVisible(false);
                choiceBox7.setVisible(false);
                getNames("Маршрут", choiceBox2, codeList1);
                getNames("Станция", choiceBox3, codeList2);
                break;
        }

        addButton.setOnAction(actionEvent -> {
            list.clear();
            switch (Configs.tableName) {
                case "Билеты":
                    String codeWagon = codeList1.get(choiceBox2.getSelectionModel().getSelectedIndex());
                    String codeStation1 = codeList2.get(choiceBox6.getSelectionModel().getSelectedIndex());
                    String codeStation2 = codeList3.get(choiceBox7.getSelectionModel().getSelectedIndex());
                    list.add(codeWagon);
                    list.add(textField3.getText());
                    list.add(textField4.getText());
                    list.add(textField5.getText());
                    list.add(codeStation1);
                    list.add(codeStation2);
                    addQuery("Билеты", list);
                    break;
                case "Вагоны":
                    String flight = choiceBox2.getValue();
                    String date = flight.substring(0, flight.indexOf('|')).trim();
                    String route = flight.substring(flight.indexOf('|') + 2);
                    String type = choiceBox3.getValue();
                    list.add(date);
                    list.add(route);
                    list.add(type);
                    list.add(textField4.getText());
                    addQuery("ПредставлениеВагонов", list);
                    break;
                case "Работники в рейсе":
                    String codePersonal = codeList1.get(choiceBox2.getSelectionModel().getSelectedIndex());
                    String codeFlight = codeList2.get(choiceBox3.getSelectionModel().getSelectedIndex());
                    list.add(codePersonal);
                    list.add(codeFlight);
                    addQuery("Работники_В_Рейсе", list);
                    break;
                case "Рейс":
                    String codeRoute = codeList1.get(choiceBox3.getSelectionModel().getSelectedIndex());
                    list.add(textField2.getText());
                    list.add(codeRoute);
                    addQuery("Рейс", list);
                    break;
                case "Работники":
                    String codePosition = codeList1.get(choiceBox3.getSelectionModel().getSelectedIndex());
                    list.add(textField2.getText());
                    list.add(codePosition);
                    addQuery("Работники", list);
                    break;
                case "Станции на маршруте":
                    codeRoute = codeList1.get(choiceBox2.getSelectionModel().getSelectedIndex());
                    String codeStation = codeList2.get(choiceBox3.getSelectionModel().getSelectedIndex());
                    list.add(codeRoute);
                    list.add(codeStation);
                    list.add(textField4.getText());
                    list.add(textField5.getText());
                    list.add(textField6.getText());
                    list.add(textField7.getText());
                    System.out.println(list.toString());
                    addQuery("Станции_На_Маршруте", list);
                    break;
            }
        });
    }

    public void setVisibilityTripleTables1() {
        label4.setVisible(false);
        label5.setVisible(false);
        label6.setVisible(false);
        label7.setVisible(false);
        textField3.setVisible(false);
        textField4.setVisible(false);
        textField5.setVisible(false);
        textField6.setVisible(false);
        textField7.setVisible(false);
        choiceBox2.setVisible(false);
        choiceBox6.setVisible(false);
        choiceBox7.setVisible(false);
    }

    /**
     * Заносит в ChoiceBox значения из представлений или таблиц-справочников
     * @param tableName
     * @param choice
     * @param codeList
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void getNames(String tableName, ChoiceBox<String> choice, List<String> codeList) throws SQLException, ClassNotFoundException {
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
    }

    public String insertQueryBuilder(String tableName, int count) {
        StringBuilder sb = new StringBuilder("INSERT INTO ").append(tableName).append(" VALUES(");
        sb.append("?, ".repeat(Math.max(0, count)));
        sb.delete(sb.length() - 2, sb.length());
        sb.append(")");
        return sb.toString();
    }

    /**
     * Метод для добавления записи через представление или таблицы
     * @param tableName название таблицы
     * @param value
     */
    public void addQuery(String tableName, List<String> value) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        int length = value.size();
        String sql = insertQueryBuilder(tableName, length);
        if (tableName.equals("ПредставлениеВагонов")) {
            sql = "INSERT INTO ПредставлениеВагонов (Дата, [Номер Поезда], [Тип Вагона], [Номер Вагона]) VALUES (?, ?, ?, ?)";
        }
        System.out.println(sql);
        try {
            PreparedStatement preparedStatement = dbHandler.getDbConnection().prepareStatement(sql);
            for(int i = 1; i <= length; i++) {
                preparedStatement.setString(i, value.get(i - 1));
            }
            preparedStatement.executeUpdate();
            confirmationWindow();
            System.out.println("Таблица успешно обновлена");
        } catch (SQLException e) {
            showError(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void confirmationWindow() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Добавление");
        alert.setHeaderText("Запись успешно добавлена");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                alert.close();
                addButton.getScene().getWindow().hide();
                Main main = new Main();
                String fxml = "../view/add.fxml";
                try {
                    main.openStage(fxml);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
