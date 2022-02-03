package Controller;
/**
 *
 * @author Matt DiPerna
 */
import Model.Appointment;
import Model.AppointmentDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class UpdateAppointmentController implements Initializable {
    /**
     * an element of the gui
     */
    @FXML
    private TextField AppointmentIDTxt;
    /**
     * an element of the gui
     */
    @FXML
    private Button CancelBtn;
    /**
     * an element of the gui
     */
    @FXML
    private ComboBox<String> ContactCombo;
    /**
     * an element of the gui
     */
    @FXML
    private ComboBox<Integer> CustomerIDCombo;
    /**
     * an element of the gui
     */
    @FXML
    private DatePicker DateDatePicker;
    /**
     * an element of the gui
     */
    @FXML
    private TextField DescriptionTxt;
    /**
     * an element of the gui
     */
    @FXML
    private ComboBox<String> EndCombo;
    /**
     * an element of the gui
     */
    @FXML
    private TextField LocationTxt;
    /**
     * an element of the gui
     */
    @FXML
    private Button SaveBtn;
    /**
     * an element of the gui
     */
    @FXML
    private ComboBox<String> StartCombo;
    /**
     * an element of the gui
     */
    @FXML
    private TextField TitleTxt;
    /**
     * an element of the gui
     */
    @FXML
    private TextField TypeTxt;
    /**
     * an element of the gui
     */
    @FXML
    private ComboBox<Integer> UserIDCombo;

    /**
     * @param event the event triggers function to switch back to the view appointments page
     */
    @FXML
    void OnActionCancel(ActionEvent event) throws IOException {

        LoginController lg = new LoginController();
        lg.setStage("/View/ViewAppointments.fxml",event,"Appointments");
    }

    /**
     * @param event the event triggers function to save the updated appointment info and checks for errors
     */
    @FXML
    void OnActionSave(ActionEvent event) throws SQLException, IOException {


        //save appointment
        //no apptmt id its generated automatically
        int appmtId = Integer.parseInt(AppointmentIDTxt.getText());
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
        ZonedDateTime newStart = LocalDateTime.of(newDate,newStartTime).atZone(ZoneId.systemDefault());
        ZonedDateTime newEnd = LocalDateTime.of(newDate,newEndTime).atZone(ZoneId.systemDefault());
        if (title.isEmpty() || description.isEmpty() || location.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incomplete Customer");
            alert.setContentText("Please fill out all required items!");
            alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
            alert.show();
            return;
        }

        //check times are in order
        if(newStart.isAfter(newEnd)||newStart.isEqual(newEnd)){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Time Format");
            alert.setContentText("Start Time cannot be the same as or after End Time");
            alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
            alert.show();
            return;

        }

        //check if customer times overlap
        if(AppointmentDB.repeatCustomerAppointment(appmtId,custId,newStart,newEnd)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Customer Times Overlap");
            alert.setContentText("This customer already has an appointment scheduled during this time!");
            alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
            alert.show();
            return;
        }



        // we need to convert times to EST and check that they fall within the range of 8am-10pm EST
        if(AppointmentDB.estCheck(newStart,newEnd,newDate)){

            //update appointment based on appointment id
            AppointmentDB.saveUpdatedAppointment(appmtId,title,description,location,contact,type,newStart,newEnd,custId,userId);

            //switch back to view screen
            LoginController lg = new LoginController();
            lg.setStage("/View/ViewAppointments.fxml",event,"Appointments");

        }


    }
    /**
     * @param appointment used to load the selected appointment info
     */
    public void sendAppointment(Appointment appointment){
        ObservableList<String> contacts = FXCollections.observableArrayList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        //not working correctly need to get the data from the actual db
        // we need to pass what kind we want
        for(Integer i : AppointmentDB.getIdsForComboBox("contacts")){
            contacts.add(AppointmentDB.contactIdToContact(i));
        }

        //setting the fields
        AppointmentIDTxt.setText(String.valueOf(appointment.getApptmtId()));
        TitleTxt.setText(String.valueOf(appointment.getTitle()));
        DescriptionTxt.setText(String.valueOf(appointment.getDescription()));
        LocationTxt.setText(String.valueOf(appointment.getLocation()));
        ContactCombo.setItems(contacts);
        ContactCombo.setValue(appointment.getContact());
        TypeTxt.setText(String.valueOf(appointment.getType()));
        DateDatePicker.setValue(appointment.getStart().toLocalDate());
        StartCombo.setItems(AppointmentDB.createTimes());
        StartCombo.setValue(appointment.getStart().format(formatter));
        EndCombo.setItems(AppointmentDB.createTimes());
        EndCombo.setValue(appointment.getEnd().format(formatter));
        CustomerIDCombo.setItems(AppointmentDB.getIdsForComboBox("customer"));
        CustomerIDCombo.setValue(appointment.getCustomerId());
        UserIDCombo.setItems(AppointmentDB.getIdsForComboBox("user"));
        UserIDCombo.setValue(appointment.getUserId());

    }

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

}
