package Controller;
/**
 *
 * @author Matt DiPerna
 */
import Model.Appointment;
import Model.AppointmentDB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewAppointmentsController implements Initializable {
    /**
     * for setting the Stage
     */
    private Stage stage;
    /**
     * for setting the Scene
     */
    private Parent scene;
    /**
     * an element of the gui
     */
    @FXML
    private Button viewCustomersBtn;
    /**
     * an element of the gui
     */
    @FXML
    private Button ReportsBtn;
    /**
     * an element of the gui
     */
    @FXML
    private Button DeleteBtn;
    /**
     * an element of the gui
     */
    @FXML
    private Button AddBtn;
    /**
     * an element of the gui
     */
    @FXML
    private Button UpdateBtn;
    /**
     * an element of the gui
     */
    @FXML
    private RadioButton AllButton;
    /**
     * an element of the gui
     */
    @FXML
    private TableView<Appointment> ApptmtsView;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?,?> ContactCol;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> CustIDCol;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> DescriptionCol;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> EndCol;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?,?> IDCol;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> LocationCol;
    /**
     * an element of the gui
     */
    @FXML
    private RadioButton MonthButton;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> StartCol;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> TitleCol;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> TypeCol;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> UserIDCol;
    /**
     * an element of the gui
     */
    @FXML
    private RadioButton WeekButton;

    /**
     * @param event the event triggers function to switch to the add appointments view
     */
    @FXML
    void OnActionAdd(ActionEvent event) throws IOException {
        LoginController lg = new LoginController();
        lg.setStage("/View/AddAppointments.fxml",event,"Add Appointment");
    }

    /**
     * @param event the event triggers function to switch to the add appointments view
     */
    @FXML
    void OnActionViewCustomers(ActionEvent event) throws IOException {
        LoginController lg = new LoginController();
        lg.setStage("/View/ViewCustomers.fxml",event,"Customers");
    }
    /**
     * @param event the event triggers function to switch to the view reports view
     */
    @FXML
    void OnActionViewReports(ActionEvent event) throws IOException {
        LoginController lg = new LoginController();
        lg.setStage("/View/ViewReports.fxml",event,"Reports");

    }
    /**
     * @param event the event triggers function to switch to the view reports view
     */
    @FXML
    void OnActionDelete(ActionEvent event) {
        try{
            int id = ApptmtsView.getSelectionModel().getSelectedItem().getApptmtId();
            String type = ApptmtsView.getSelectionModel().getSelectedItem().getType();
            for(Appointment i : AppointmentDB.getAllAppointments()){
                if(i.getApptmtId()==id){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Deletion");
                    alert.setContentText("Are you sure you wish to delete the selected appointment?");
                    alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");

                    //we need to check if they press cancel if they do we do not delete
                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.isPresent() && result.get()==ButtonType.CANCEL){
                        break;
                    }
                    //need an sql query for delete
                    String str = AppointmentDB.deleteAppointment(id,type);
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Appointment Deleted");
                    alert2.setContentText("Your appointment was deleted\n"+str);
                    alert2.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
                    alert2.show();
                    setTable(AppointmentDB.getAllAppointments());
                    break;

                }
            }
        }catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: No Appointment Selected");
            alert.setContentText("Please select the appointment you wish to delete");
            alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
            alert.show();
        }
    }
    /**
     * @param event the event triggers function to switch to the update appointments view
     */
    @FXML
    void OnActionUpdate(ActionEvent event) throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/UpdateAppointments.fxml"));
            loader.load();
            UpdateAppointmentController controller = loader.getController();
            //send selected appointment
            controller.sendAppointment(ApptmtsView.getSelectionModel().getSelectedItem());
            //setting stage
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            scene.setStyle("-fx-font-family: 'Times New Roman';");
            stage.setScene(new Scene(scene));
            stage.setTitle("Update Appointments");
            stage.show();
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection Found");
            alert.setContentText("Please select an appointment");
            alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
            alert.show();
        }

    }
    /**
     * @param event the event triggers function to set the table with all appointments
     */
    @FXML
    void OnActionAll(ActionEvent event) {
        setTable(AppointmentDB.getAllAppointments());
    }
    /**
     * @param event the event triggers function to pass the dates to the filteredsearch
     */

    @FXML
    void OnActionMonth(ActionEvent event) {

        // we need to pass the dates to our filteredsearch in AppointmentDB into UTC
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime end = now.plusMonths(1).withZoneSameInstant(ZoneOffset.UTC);
        setTable(AppointmentDB.getFilteredAppointments(now, end));
    }

    /**
     * @param event the event triggers function to pass the dates to the filteredsearch
     */
    @FXML
    void OnActionWeek(ActionEvent event) {

        // we need to pass the dates to our filteredsearch in AppointmentDB into UTC
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime end = now.plusWeeks(1).withZoneSameInstant(ZoneOffset.UTC);

        setTable(AppointmentDB.getFilteredAppointments(now, end));
    }


    /**
     * @param appointments set the table with the appointments ObservableList
     */
    public void setTable(ObservableList<Appointment> appointments){

        ApptmtsView.setItems(appointments);
        //automatically calls get methods

        IDCol.setCellValueFactory(new PropertyValueFactory<>("apptmtId"));
        TitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        DescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        LocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        ContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        TypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        StartCol.setCellValueFactory((new PropertyValueFactory<>("startDateAttr")));
        EndCol.setCellValueFactory(new PropertyValueFactory<>("endDateAttr"));
        CustIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        UserIDCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

    }

    /**
     * sets the table with all appointments
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){

        setTable(AppointmentDB.getAllAppointments());

    }


}
