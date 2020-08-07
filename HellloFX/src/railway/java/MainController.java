package railway.java;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Этот класс управляет элементами главного окна
 */
public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<?> tableView;

    @FXML
    private ChoiceBox<String> tablesChoiceBox;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem modifyMenu;

    @FXML
    private MenuItem addMenu;

    @FXML
    private MenuItem deleteMenu;

    @FXML
    private Menu flightMenu;

    @FXML
    private MenuItem showFlightMenu;

    @FXML
    private MenuItem flightScheduleMenu;

    @FXML
    private MenuItem ticketForm;

    @FXML
    private MenuItem routeForm;

    @FXML
    private MenuItem typeForm;

    @FXML
    private Menu showTicketsMenu;

    @FXML
    private MenuItem buyTicketMenu;

    @FXML
    private MenuItem showTicketMenu;

    @FXML
    private Menu printMenu;

    @FXML
    private Menu exitMenu;

    @FXML
    private Button backLoginButton;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'main.fxml'.";
        assert tablesChoiceBox != null : "fx:id=\"tablesChoiceBox\" was not injected: check your FXML file 'main.fxml'.";
        assert menuBar != null : "fx:id=\"menuBar\" was not injected: check your FXML file 'main.fxml'.";
        assert modifyMenu != null : "fx:id=\"modifyMenu\" was not injected: check your FXML file 'main.fxml'.";
        assert addMenu != null : "fx:id=\"addMenu\" was not injected: check your FXML file 'main.fxml'.";
        assert deleteMenu != null : "fx:id=\"deleteMenu\" was not injected: check your FXML file 'main.fxml'.";
        assert flightMenu != null : "fx:id=\"flightMenu\" was not injected: check your FXML file 'main.fxml'.";
        assert showFlightMenu != null : "fx:id=\"showFlightMenu\" was not injected: check your FXML file 'main.fxml'.";
        assert flightScheduleMenu != null : "fx:id=\"flightScheduleMenu\" was not injected: check your FXML file 'main.fxml'.";
        assert ticketForm != null : "fx:id=\"ticketForm\" was not injected: check your FXML file 'main.fxml'.";
        assert routeForm != null : "fx:id=\"routeForm\" was not injected: check your FXML file 'main.fxml'.";
        assert typeForm != null : "fx:id=\"typeForm\" was not injected: check your FXML file 'main.fxml'.";
        assert showTicketsMenu != null : "fx:id=\"showTicketsMenu\" was not injected: check your FXML file 'main.fxml'.";
        assert buyTicketMenu != null : "fx:id=\"buyTicketMenu\" was not injected: check your FXML file 'main.fxml'.";
        assert showTicketMenu != null : "fx:id=\"showTicketMenu\" was not injected: check your FXML file 'main.fxml'.";
        assert printMenu != null : "fx:id=\"printMenu\" was not injected: check your FXML file 'main.fxml'.";
        assert exitMenu != null : "fx:id=\"exitMenu\" was not injected: check your FXML file 'main.fxml'.";
        assert backLoginButton != null : "fx:id=\"backLoginButton\" was not injected: check your FXML file 'main.fxml'.";


        if (Configs.userType == 0) {
            flightMenu.setVisible(false);
        } else if (Configs.userType == 1) {
            showTicketsMenu.setVisible(false);
        }
        Main main = new Main();
        tablesChoiceBox.setItems(Configs.tables);
        tablesChoiceBox.setValue(Configs.tables.get(0));
        main.buildData(tableView, "SELECT * FROM ПредставлениеБилетов");
        tableView.getColumns().get(0).setVisible(false);

        //переключает таблицы
        tablesChoiceBox.setOnAction(actionEvent -> {
            String table = tablesChoiceBox.getValue();
            table = table.replace(" ", "_");
            if (table.equals("Билеты")) {
                table = "ПредставлениеБилетов";
            } else if (table.equals("Вагоны")) {
                table = "ПредставлениеВагонов";
            } else if (table.equals("Работники")) {
                table = "ПредставлениеРаботников";
            } else if (table.equals("Работники_в_рейсе")) {
                table = "ПредставлениеРаботниковВРейсе";
            } else if (table.equals("Станции_на_маршруте")) {
                table = "ПредставлениеСтанцийНаМаршруте";
            } else if (table.equals("Рейс")) {
                table = "ПредставлениеРейсов";
            }
            String sql = "SELECT * FROM " + table;
            try {
                main.buildData(tableView, sql);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            tableView.getColumns().get(0).setVisible(false);
        });

        //открывает окно с редактированием записи
        modifyMenu.setOnAction(actionEvent -> {
            String fxml = "../view/edit.fxml";
            try {
                main.openStage(fxml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //открывает окно с добавлением записи
        addMenu.setOnAction(actionEvent -> {
            String fxml = "../view/add.fxml";
            try {
                main.openStage(fxml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //открывает окно для удаления записи
        deleteMenu.setOnAction(actionEvent -> {
            String fxml = "../view/delete.fxml";
            try {
                main.openStage(fxml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //открывает окно для печати
        Label label1 = new Label("Печать");
        label1.setOnMouseClicked(event -> {
            System.out.println("Printing");
            String fxml = "../view/print.fxml";
            try {
                main.openStage(fxml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        printMenu.setGraphic(label1);

        //выход из приложения через кнопку в меню
        Label exitLabel = new Label("Выход");
        exitLabel.setOnMouseClicked(mouseEvent -> {
            System.out.println("Exit");
            Platform.exit();
            System.exit(0);
        });
        exitMenu.setGraphic(exitLabel);

        //комбинации клавиш ctrl + P
        Platform.runLater(() -> {
            Scene scene = tableView.getScene();
            KeyCombination combo = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_ANY);
            Runnable rn = () -> {
                String fxml = "../view/print.fxml";
                try {
                    main.openStage(fxml);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            scene.getAccelerators().put(combo, rn);
        });

        //кнопка назад
        backLoginButton.setOnAction(actionEvent -> {
            backLoginButton.getScene().getWindow().hide();
            String fxml = "../view/sample.fxml";
            try {
                main.openStage(fxml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //кнопка из меню для просмотра рейсов работника
        showFlightMenu.setOnAction(actionEvent -> {
            String fxml = "../view/flightView.fxml";
            try {
                main.openStage(fxml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //кнопка из меню для просмотра респисания рейсов
        flightScheduleMenu.setOnAction(actionEvent -> {
            String fxml = "../view/flightDates.fxml";
            try {
                main.openStage(fxml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        ticketForm.setOnAction(actionEvent -> {
            String fxml = "../view/ticketForm.fxml";
            try {
                main.openStage(fxml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        routeForm.setOnAction(actionEvent -> {
            String fxml = "../view/routeForm.fxml";
            try {
                main.openStage(fxml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        typeForm.setOnAction(actionEvent -> {
            String fxml = "../view/typeForm.fxml";
            try {
                main.openStage(fxml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
