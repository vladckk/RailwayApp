package railway.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Этот класс содержит необходимые константы для работы
 */
public class Configs {

    protected String dbUser = "rwdb";
    protected String dbPassword = "qwertyuiop";
    protected static String tableName = "Билеты";
    protected static int typeTable = 0;
    protected static String selectedId = "0";
    protected static String userLogin;
    protected static int userType;
    protected static ObservableList<String> tables = FXCollections.observableArrayList("Билеты", "Вагоны", "Должность", "Маршрут"
            , "Работники", "Работники в рейсе", "Рейс", "Станции на маршруте", "Станция", "Типы вагонов");
    protected static ObservableList<String> handBooks = FXCollections.observableArrayList("Должность", "Маршрут" , "Станция",
            "Типы_вагонов");
    protected static ObservableList<String> line = FXCollections.observableArrayList();

    /**
     * Метод проверяет ФИО на правильность с помощью регулярных выражений
     * @param name ФИО
     * @return true/false
     */
    public static boolean checkRegister(String name) {
        Pattern pattern = Pattern.compile("([A-Z].(| )[A-Z][a-z]+)|([А-Я][а-я]+ [А-Я].(| )[А-Я].)");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            return true;
        }
        System.out.println("Ошибка!");
        return false;
    }

    /**
     * Этот метод проверяет значение времени на корректность  с помощью регулярного выражения
     * @param time время в строковом виде
     * @return true/false
     */
    public static boolean checkTime(String time) {
        if (time == null) {
            return true;
        }
        Pattern pattern = Pattern.compile("(([0-1][0-9])|([2][0-3])):([0-5][0-9])");
        Matcher matcher = pattern.matcher(time);
        if (matcher.find()) {
            return true;
        }
        System.out.println("Время должно иметь формат, как 23:59");
        return false;
    }
}
