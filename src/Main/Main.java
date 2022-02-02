package Main;

import Database.JDBC;
import Model.AppointmentDB;
import Model.User;
import Model.UserDB;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View/Login.fxml"));
        primaryStage.setScene(new Scene(root));
        String title = "Login";
        ResourceBundle bundle = ResourceBundle.getBundle("Controller/language", Locale.getDefault());

        if(Locale.getDefault().getLanguage().equals("fr")){
            title = bundle.getString("Login");
        }
        primaryStage.setTitle(title);
        root.setStyle("-fx-font-family: 'Times New Roman';");
        primaryStage.show();
    }


    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();


    }
}
