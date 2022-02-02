package Controller;

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
    private Stage stage;
    private Parent scene;

    @FXML
    private Button viewCustomersBtn;

    @FXML
    private Button ReportsBtn;

    @FXML
    private Button DeleteBtn;

    @FXML
    private Button AddBtn;

    @FXML
    private Button UpdateBtn;

    @FXML
    private RadioButton AllButton;

    @FXML
    private TableView<Appointment> ApptmtsView;

    @FXML
    private TableColumn<?,?> ContactCol;

    @FXML
    private TableColumn<?, ?> CustIDCol;

    @FXML
    private TableColumn<?, ?> DescriptionCol;

    @FXML
    private TableColumn<?, ?> EndCol;

    @FXML
    private TableColumn<?,?> IDCol;

    @FXML
    private TableColumn<?, ?> LocationCol;

    @FXML
    private RadioButton MonthButton;

    @FXML
    private TableColumn<?, ?> StartCol;

    @FXML
    private TableColumn<?, ?> TitleCol;

    @FXML
    private TableColumn<?, ?> TypeCol;

    @FXML
    private TableColumn<?, ?> UserIDCol;

    @FXML
    private RadioButton WeekButton;

    @FXML
    void OnActionAdd(ActionEvent event) throws IOException {
        LoginController lg = new LoginController();
        lg.setStage("/View/AddAppointments.fxml",event,"Add Appointment");
    }

    @FXML
    void OnActionViewCustomers(ActionEvent event) throws IOException {
        LoginController lg = new LoginController();
        lg.setStage("/View/ViewCustomers.fxml",event,"Customers");
    }
    @FXML
    void OnActionViewReports(ActionEvent event) throws IOException {
        LoginController lg = new LoginController();
        lg.setStage("/View/ViewReports.fxml",event,"Reports");

    }

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

    @FXML
    void OnActionAll(ActionEvent event) {
        setTable(AppointmentDB.getAllAppointments());
    }

    @FXML
    void OnActionMonth(ActionEvent event) {

        // we need to pass the dates to our filteredsearch in AppointmentDB into UTC
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime end = now.plusMonths(1).withZoneSameInstant(ZoneOffset.UTC);

        setTable(AppointmentDB.getFilteredAppointments(now, end));
    }

    @FXML
    void OnActionWeek(ActionEvent event) {

        // we need to pass the dates to our filteredsearch in AppointmentDB into UTC
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime end = now.plusWeeks(1).withZoneSameInstant(ZoneOffset.UTC);

        setTable(AppointmentDB.getFilteredAppointments(now, end));
    }



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

    @Override
    public void initialize(URL url, ResourceBundle rb){

        setTable(AppointmentDB.getAllAppointments());

    }


}
