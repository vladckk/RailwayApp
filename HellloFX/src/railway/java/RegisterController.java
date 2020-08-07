package railway.java;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.xml.crypto.Data;

/**
 * Управляет окно с регистрации
 */
public class RegisterController {

    ObservableList<String> mode = FXCollections.observableArrayList("Клиент", "Работник");

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField password1Field;

    @FXML
    private PasswordField password2Field;

    @FXML
    private TextField fioTextField;

    @FXML
    private ChoiceBox<String> choiceBox1;

    @FXML
    private Button registerButton;

    @FXML
    private Button backButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Label fioErrorLabel;

    @FXML
    void initialize() {
        assert loginField != null : "fx:id=\"loginField\" was not injected: check your FXML file 'register.fxml'.";
        assert password1Field != null : "fx:id=\"password1Field\" was not injected: check your FXML file 'register.fxml'.";
        assert password2Field != null : "fx:id=\"password2Field\" was not injected: check your FXML file 'register.fxml'.";
        assert fioTextField != null : "fx:id=\"fioTextField\" was not injected: check your FXML file 'register.fxml'.";
        assert choiceBox1 != null : "fx:id=\"choiceBox1\" was not injected: check your FXML file 'register.fxml'.";
        assert registerButton != null : "fx:id=\"registerButton\" was not injected: check your FXML file 'register.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'register.fxml'.";
        assert errorLabel != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'register.fxml'.";
        assert fioErrorLabel != null : "fx:id=\"fioErrorLabel\" was not injected: check your FXML file 'register.fxml'.";

        Main main = new Main();
        //кнопка назад, возвращение на окно авторизации
        backButton.setOnAction(actionEvent -> {
            String fxml = "../view/sample.fxml";
            try {
                backButton.getScene().getWindow().hide();
                main.openStage(fxml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        choiceBox1.setItems(mode);
        choiceBox1.setValue(mode.get(0));
        errorLabel.setVisible(false);
        fioErrorLabel.setVisible(false);

        //кнопка регистрации
        registerButton.setOnAction(actionEvent -> {
            errorLabel.setVisible(false);
            String login = loginField.getText();
            String password = password1Field.getText();
            String fio = fioTextField.getText();
            int type = choiceBox1.getSelectionModel().getSelectedIndex();
            String fxml = "../view/main.fxml";
            if (Configs.checkRegister(fio)) {
                fioErrorLabel.setVisible(false);
                try {
                    if (registerUser(login, password, fio, type)) {
                        registerButton.getScene().getWindow().hide();
                        Configs.userLogin = login;
                        Configs.userType = type;
                        main.openStage(fxml);
                    } else {
                        System.out.println("Ошибка регистрации");
                        errorLabel.setVisible(true);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                fioErrorLabel.setVisible(true);
            }
        });
    }

    /**
     * Метод для регистрации пользователя
     * @param login
     * @param password
     * @param fio
     * @param type
     * @return
     */
    private boolean registerUser(String login, String password, String fio, int type) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        boolean check = false;
        if (login.isEmpty()) {
            System.out.println("User is empty!");
        } else if (password.isEmpty()) {
            System.out.println("Password is empty!");
        } else if (password1Field.getText().equals(password2Field.getText())){
            if (fio.isEmpty()){
                type = 2;
            }
            try {
                check = dbHandler.signUpUser(login, password, fio, type);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return check;
    }
}
