package railway.java;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.gembox.spreadsheet.ExcelFile;
import com.gembox.spreadsheet.ExcelWorksheet;
import com.gembox.spreadsheet.SpreadsheetInfo;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

/**
 * Класс предназначен для построения печатной формы
 */
public class PrintController {

    static {
        SpreadsheetInfo.setLicense("FREE-LIMITED-KEY");
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<?> tableView;

    @FXML
    private Button backButton;

    @FXML
    private Button printButton;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'print.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'print.fxml'.";
        assert printButton != null : "fx:id=\"printButton\" was not injected: check your FXML file 'print.fxml'.";
        assert choiceBox != null : "fx:id=\"choiceBox\" was not injected: check your FXML file 'print.fxml'.";
        Main main = new Main();
        main.defaultView(choiceBox, tableView, "view");
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

        main.tableName = choiceBox.getValue();
        tableView.getColumns().get(0).setVisible(false);
        System.out.println(main.tableName);

        printButton.setOnAction(actionEvent -> {
            try {
                save(main.tableName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Метод заносит значения из таблицы в файл .xlsx
     * @param tableName
     * @throws IOException
     */
    public void save(String tableName) throws IOException {
        ExcelFile file = new ExcelFile();
        ExcelWorksheet worksheet = file.addWorksheet(tableName);
        for (int column = 1; column < tableView.getColumns().size(); column++) {
            String header = tableView.getColumns().get(column).getText();
            header = header.replace("_", " ");
            worksheet.getCell(0, column - 1).setValue(header);
        }
        for (int row = 0; row < tableView.getItems().size(); row++) {
            ObservableList cells = (ObservableList) tableView.getItems().get(row);
            for (int column = 1; column < cells.size(); column++) {
                if (cells.get(column) != null)
                    worksheet.getCell(row + 1, column - 1).setValue(cells.get(column).toString());
            }
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx"),
                new FileChooser.ExtensionFilter("XLS files (*.xls)", "*.xls"),
                new FileChooser.ExtensionFilter("ODS files (*.ods)", "*.ods"),
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"),
                new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html")
        );
        File saveFile = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        file.save(saveFile.getAbsolutePath());
    }
}
