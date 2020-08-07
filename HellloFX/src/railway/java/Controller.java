package railway.java;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Этот класс загружает окно с авторизацией
 */
public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInButton;

    @FXML
    private Button registerButton;

    @FXML
    private Label loginErrorLabel;

    @FXML
    void initialize() {
        assert loginField != null : "fx:id=\"loginField\" was not injected: check your FXML file 'sample.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'sample.fxml'.";
        assert signInButton != null : "fx:id=\"signInButton\" was not injected: check your FXML file 'sample.fxml'.";
        assert registerButton != null : "fx:id=\"registerButton\" was not injected: check your FXML file 'sample.fxml'.";
        assert loginErrorLabel != null : "fx:id=\"loginErrorLabel\" was not injected: check your FXML file 'sample.fxml'.";
        Main main = new Main();
        DatabaseHandler dbHandler = new DatabaseHandler();
        loginErrorLabel.setVisible(false);
        //кнопка для регистрации
        registerButton.setOnAction(event -> {
            System.out.println("Регистрация...");
            registerButton.getScene().getWindow().hide();
            String fxml = "../view/register.fxml";
            registerButton.getScene().getWindow().hide();
            try {
                main.openStage(fxml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //кнопка для авторизации
        signInButton.setOnAction(actionEvent -> {
            System.out.println("Вход...");
            String login = loginField.getText();
            String password = passwordField.getText();
            if (verifyLogin(login, password)) {
                String fxml = "../view/main.fxml";
                Configs.userLogin = login;
                Configs.userType = dbHandler.getType(login);
                signInButton.getScene().getWindow().hide();
                try {
                    main.openStage(fxml);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Проверяет логин и выводит ошибку, если логин уже существует
     * @param login
     * @param password
     * @return
     */
    private boolean verifyLogin(String login, String password) {
        loginField.setFocusTraversable(true);
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet resultSet = dbHandler.getUser(login, password);
        Configs.userType = dbHandler.getType(login);
        boolean check = false;
        try {
            if (resultSet.next()){
                check = true;
                loginErrorLabel.setVisible(false);
            } else {
                loginErrorLabel.setText("Неверный логин или пароль");
                loginErrorLabel.setVisible(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check;
    }

}
