package railway.java;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Загружает стартовое окно
 */
public class Main extends Application {

    String tableName;

    //Отрисовывается главное окно
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/sample.fxml"));
        primaryStage.setTitle("Авторизация");
        primaryStage.setScene(new Scene(root, 457, 307));
        primaryStage.show();
    }

    /**
     * Этот метод открывает новое окно, а в параметраз мы передаём файл с расширением .fxml,
     * который отвечает за отрисовку всех элементов
     * @param fxml путь к файлу fxml
     * @throws IOException
     */
    public void openStage(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        loader.load();
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Этот метод используется для стандартного отображения всех таблиц, загрузку названий таблиц в ChoiceBox, и с помощью
     * элемента ChoiceBox менять таблицы в окне
     * @param choiceBox1 элемент, который переключает таблицы
     * @param tableView таблица
     * @param type тип загрузки таблицы, т.е. свойства таблиц изменяются в зависимости от нужд(для просмотра,
     *             для редактирования всех записей, для добавления записи)
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void defaultView(ChoiceBox<String> choiceBox1, TableView tableView, String type) throws SQLException, ClassNotFoundException {
        choiceBox1.setItems(Configs.tables);
        choiceBox1.setValue(Configs.tables.get(0));
        tableName = choiceBox1.getValue();
        String sqlMain = "SELECT * FROM ПредставлениеБилетов";
        if (type.equals("edit")) {
            buildEditableData(tableView, sqlMain);
        } else if (type.equals("add")) {
            buildAddableData(tableView, sqlMain, tableName);
        } else if (type.equals("view")) {
            buildData(tableView, sqlMain);
        }
        choiceBox1.setOnAction(actionEvent -> {
            tableName = choiceBox1.getValue();
            tableName = tableName.replace(" ", "_");
            if (tableName.equals("Билеты")) {
                tableName = "ПредставлениеБилетов";
            } else if (tableName.equals("Вагоны")) {
                tableName = "ПредставлениеВагонов";
            } else if (tableName.equals("Работники")) {
                tableName = "ПредставлениеРаботников";
            } else if (tableName.equals("Работники_в_рейсе")) {
                tableName = "ПредставлениеРаботниковВРейсе";
            } else if (tableName.equals("Станции_на_маршруте")) {
                tableName = "ПредставлениеСтанцийНаМаршруте";
            } else if (tableName.equals("Рейс")) {
                tableName = "ПредставлениеРейсов";
            }
            String sql = "SELECT * FROM " + tableName;
            try {
                System.out.println(type);
                if (type.equals("edit")) {
                    buildEditableData(tableView, sql);
                } else if (type.equals("add")){
                    buildAddableData(tableView, sql, tableName);
                } else if (type.equals("view")) {
                    buildData(tableView, sql);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println(tableView.getColumns().size());
        });
    }

    /**
     * Заносит уже готовые значения в таблицу, предназначенную для редактирования
     * @param tableView таблица
     * @param sql select-запрос
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void buildEditableData(TableView tableView, String sql) throws SQLException, ClassNotFoundException {
        ResultSet rs = fillingEditableTable(tableView, sql);
        ObservableList data = FXCollections.observableArrayList();
        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                row.add(rs.getString(i));
            }
            data.add(row);
        }
        tableView.setItems(data);
    }

    /**
     * Заносит готовые значения в таблицу, предназначенную для редактирования, а также добавляет дополнительную строку в
     * таблицу для добавления новой строки в таблицу
     * @param tableView таблица
     * @param sql select-запрос
     * @param table название таблицы
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void buildAddableData(TableView tableView, String sql, String table) throws SQLException, ClassNotFoundException {
        ResultSet rs = fillingEditableTable(tableView, sql);
        ObservableList data = FXCollections.observableArrayList();
        int columnCount = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getString(i));
            }
            data.add(row);
        }
        if (Configs.handBooks.contains(table)) {
            ObservableList<String> row = FXCollections.observableArrayList();
            row.add("(№)");
            row.add("Добавить запись...");
            for (int i = 0; i < columnCount - 2; i++) {
                row.add(null);
            }
            data.add(row);
        }
        tableView.setItems(data);
    }

    /**
     * Отрисовывает таблицу для редактирования значений внутри ячеек таблицы
     * @param tableView таблица
     * @param sql select-запрос
     * @return результаты выполнения запроса
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ResultSet fillingEditableTable(TableView tableView, String sql) throws SQLException, ClassNotFoundException {
        tableView.getColumns().clear();
        tableView.setEditable(true);
        DatabaseHandler dbHandler = new DatabaseHandler();
        Connection c = dbHandler.getDbConnection();
        ResultSet rs = null;
        try {
            rs = c.createStatement().executeQuery(sql);
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn column = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                column.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> {
                    if (param.getValue().get(j) == null) {
                        return new SimpleStringProperty(null);
                    } else {
                        String some = param.getValue().get(j).toString();
                        return new SimpleStringProperty(some);
                    }
                });
                if (j > 0) {
                    column.setCellFactory(TextFieldTableCell.forTableColumn());
                    column.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<ObservableList, String>>) table ->
                            table.getTableView().getItems().get(table.getTablePosition().getRow()).
                                    set(table.getTablePosition().getColumn(), table.getNewValue()));
                }
                tableView.getColumns().addAll(column);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on building data");
        }
        return rs;
    }

    /**
     * Этот метод отрисовывает таблицу, предназначенную для просмотра, т.е. значения её ячеек никак нельзя изменять
     * @param tableView
     * @param sql
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void buildData(TableView tableView, String sql) throws SQLException, ClassNotFoundException {
        tableView.getColumns().clear();
        tableView.setEditable(true);
        DatabaseHandler dbHandler = new DatabaseHandler();
        Connection c = dbHandler.getDbConnection();
        try {
            ObservableList data = FXCollections.observableArrayList();
            ResultSet rs = c.createStatement().executeQuery(sql);
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn columns = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                columns.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> {
                    if (param.getValue().get(j) == null) {
                        return new SimpleStringProperty(null);
                    } else {
                        String some = param.getValue().get(j).toString();
                        return new SimpleStringProperty(some);
                    }
                });
                tableView.getColumns().addAll(columns);
            }

            while(rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
            tableView.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on building data");
        }
    }

    /**
     * Данный метод использовался для отрисовки результатов процедур и функций, если они возвращают таблицу
     * @param tableView табличный элемент
     * @param rs данные, которые возвращаются в результате выполнения запроса
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void buildDataProcedures(TableView tableView, ResultSet rs) throws SQLException, ClassNotFoundException {
        tableView.getColumns().clear();
        tableView.setEditable(true);
        try {
            ObservableList data = FXCollections.observableArrayList();
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn columns = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                columns.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> {
                    if (param.getValue().get(j) == null) {
                        return new SimpleStringProperty(null);
                    } else {
                        String some = param.getValue().get(j).toString();
                        return new SimpleStringProperty(some);
                    }
                });
                tableView.getColumns().addAll(columns);
            }

            while(rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
            tableView.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on building data");
        }
    }

    /**
     * Данный метод использовался для занесения значений таблиц в ChoiceBox
     * @param tableName имя таблицы
     * @param choice компонент ChoiceBox
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void getField(String tableName, ChoiceBox<String> choice) throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet rs = null;
        String sql = "SELECT * FROM " + tableName;
        PreparedStatement preparedStatement = dbHandler.getDbConnection().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ObservableList<String> list = FXCollections.observableArrayList();
        rs = preparedStatement.executeQuery();
        rs.beforeFirst();
        while (rs.next()) {
            list.add(rs.getString(2));
        }
        choice.setItems(list);
        choice.setValue(list.get(0));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
