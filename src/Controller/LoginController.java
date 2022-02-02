package Controller;

import Model.AppointmentDB;
import Model.User;
import Model.UserDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    Stage stage;
    Parent scene;
    private boolean loginSuccess;

    private String alertTitle = "Invalid Username / Password";
    private String alertMessage = "Invalid Username or Password";
    @FXML
    private Label UsernameLbl;

    @FXML
    private Label PasswordLbl;

    @FXML
    private Button CancelBtn;

    @FXML
    private Label LocationLbl;

    @FXML
    private Button LoginBtn;

    @FXML
    private PasswordField PasswordTxt;

    @FXML
    private TextField Usernametxt;



    @FXML
    void onActionCancel(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onActionLogin(ActionEvent event) throws IOException {
        String name = Usernametxt.getText();
        String password = PasswordTxt.getText();

            // if username and password match set scene
            ObservableList<User> users = UserDB.getAllUsers();
            for(User i: users){
                if(i.getUserName().equals(name)&&i.getPassword().equals(password)){
                    setStage("/View/ViewAppointments.fxml",event, "Appointments");
                    loginSuccess = true;
                    loginLog(loginSuccess);

                    // check 15 min apptmt
                    if(AppointmentDB.fifteenMinCheck()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Appointment Soon");
                        alert.setContentText("You have an appointment within 15 minutes!\n\n Appointment ID: "+AppointmentDB.getFifteenMinStr());
                        alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
                        alert.show();
                        return;
                    }else{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("No Upcoming Appointments");
                        alert.setContentText("You do not have any upcoming appointments!");
                        alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
                        alert.show();
                    }
                    return;
                }
            }
            //if we finish and no matches found show error message

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(alertTitle);
            alert.setContentText(alertMessage);
            alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
            alert.show();
            loginSuccess = false;
            loginLog(loginSuccess);

    }

    /**
     * @param s takes a string s and on an event sets the scene to the path passed a s
     */
    void setStage(String s,ActionEvent event, String title) throws IOException {
        /**
         * get source of event, cast to button type, cast to stage
         */
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        /**
         * get path of new fxml doc
         */
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(s)));
        scene.setStyle("-fx-font-family: 'Times New Roman';");
        /**
         * set scene of new view
         */
        stage.setTitle(title);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public static void loginLog(boolean loginSuccess) throws IOException {
        // if its not there one will be created
        //track all attempts, for the date and time they occurred and whether or not successful
        String success;
        if(loginSuccess){
            success = "Successful";
        }else{
            success = "Unsuccessful";
        }

        FileWriter log = new FileWriter("login_activity.txt",true);
        BufferedWriter outPutFile = new BufferedWriter(log);
        outPutFile.write(success+" login attempted at "+ ZonedDateTime.now(ZoneOffset.UTC));
        outPutFile.newLine();
        outPutFile.close();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb){

        LocationLbl.setText(ZoneId.systemDefault().toString());

        ResourceBundle bundle = ResourceBundle.getBundle("Controller/language", Locale.getDefault());
        if(Locale.getDefault().getLanguage().equals("fr")){
           CancelBtn.setText(bundle.getString("Cancel"));
            LoginBtn.setText(bundle.getString("Login"));
            Usernametxt.setPromptText(bundle.getString("Username"));
            PasswordTxt.setPromptText(bundle.getString("Password"));
            UsernameLbl.setText(bundle.getString("Username"));
            PasswordLbl.setText(bundle.getString("Password"));
            alertTitle = bundle.getString("alertTitle");
            alertMessage = bundle.getString("alertMessage");

        }
    }


}
