package Controller;

import Model.AppointmentDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {
    @FXML
    private TextField AppointmentIDTxt;

    @FXML
    private Button CancelBtn;

    @FXML
    private ComboBox<String> ContactCombo;

    @FXML
    private ComboBox<Integer> CustomerIDCombo;

    @FXML
    private DatePicker DateDatePicker;

    @FXML
    private TextField DescriptionTxt;

    @FXML
    private ComboBox<String> EndCombo;

    @FXML
    private TextField LocationTxt;

    @FXML
    private Button SaveBtn;

    @FXML
    private ComboBox<String> StartCombo;

    @FXML
    private TextField TitleTxt;

    @FXML
    private TextField TypeTxt;

    @FXML
    private ComboBox<Integer> UserIDCombo;

    @FXML
    void OnActionCancel(ActionEvent event) throws IOException {
        LoginController lg = new LoginController();
        lg.setStage("/View/ViewAppointments.fxml",event,"Appointments");
    }

    @FXML
    void OnActionSave(ActionEvent event) throws SQLException, IOException {
        // need to query sql
        //save appointment
        try {
            int appmtId = Integer.MIN_VALUE;
            String title = String.valueOf(TitleTxt.getText());
            String description = String.valueOf(DescriptionTxt.getText());
            String location = String.valueOf(LocationTxt.getText());
            String contact = String.valueOf(ContactCombo.getValue());
            // System.out.println(contact);
            String type = String.valueOf(TypeTxt.getText());
            int custId = Integer.parseInt(String.valueOf(CustomerIDCombo.getValue()));
            int userId = Integer.parseInt(String.valueOf(UserIDCombo.getValue()));

            //make start and end back into localdatetimes combined with date
            LocalTime newStartTime = LocalTime.parse(StartCombo.getValue());
            LocalTime newEndTime = LocalTime.parse(EndCombo.getValue());
            LocalDate newDate = LocalDate.parse(String.valueOf(DateDatePicker.getValue()));

            // new start and ends
            ZonedDateTime newStart = LocalDateTime.of(newDate, newStartTime).atZone(ZoneId.systemDefault());
            ZonedDateTime newEnd = LocalDateTime.of(newDate, newEndTime).atZone(ZoneId.systemDefault());
            boolean contactEmpty = ContactCombo.getSelectionModel().isEmpty();

            if (title.isEmpty() || description.isEmpty() || location.isEmpty() || contactEmpty) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incomplete Customer");
                alert.setContentText("Please fill out all required items!");
                alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
                alert.show();
                return;
            }

            //check times are in order
            if (newStart.isAfter(newEnd) || newStart.isEqual(newEnd)) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Time Format");
                alert.setContentText("Start Time cannot be the same as or after End Time");
                alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
                alert.show();
                return;

            }

            //check if customer times overlap
            if (AppointmentDB.repeatCustomerAppointment(appmtId, custId, newStart, newEnd)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Customer Times Overlap");
                alert.setContentText("This customer already has an appointment scheduled during this time!");
                alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
                alert.show();
                return;
            }


            // we need to convert times to EST and check that they fall within the range of 8am-10pm EST
            if (AppointmentDB.estCheck(newStart, newEnd, newDate)) {

                //update appointment based on appointment id
                AppointmentDB.saveAddedAppointment(title, description, location, contact, type, newStart, newEnd, custId, userId);

                //switch back to view screen
                LoginController lg = new LoginController();
                lg.setStage("/View/ViewAppointments.fxml", event, "Appointments");

            }
        }catch(NumberFormatException  | DateTimeParseException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incomplete Appointment");
            alert.setContentText("Please fill out all required items!");
            alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
            alert.show();
        }
    }

    public void setFields(){
        ObservableList<String> contacts = FXCollections.observableArrayList();
        for(Integer i : AppointmentDB.getIdsForComboBox("contacts")){
            contacts.add(AppointmentDB.contactIdToContact(i));
        }
        ContactCombo.setItems(contacts);
        StartCombo.setItems(AppointmentDB.createTimes());
        EndCombo.setItems(AppointmentDB.createTimes());
        CustomerIDCombo.setItems(AppointmentDB.getIdsForComboBox("customer"));
        UserIDCombo.setItems(AppointmentDB.getIdsForComboBox("user"));

    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        setFields();
    }

}
