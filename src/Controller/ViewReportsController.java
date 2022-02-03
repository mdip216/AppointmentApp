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
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class ViewReportsController implements Initializable {

    private ArrayList<String> months = new ArrayList<>();

    /**
     * an element of the gui
     */
    @FXML
    private TableView<Appointment> ApptmtsView;
    /**
     * an element of the gui
     */
    @FXML
    private Button BackBtn;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> ContactCol;
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
    private Button GenerateBtn;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> IDCol;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> LocationCol;

    /**
     * an element of the gui
     */
    @FXML
    private ComboBox<String> MonthCombo;
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
    private Button UpdateBtn;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> UserIDCol;
    /**
     * an element of the gui
     */
    @FXML
    private ComboBox<String> contactCombo;
    /**
     * an element of the gui
     */
    @FXML
    private ComboBox<String> TypeCombo;
    /**
     * an element of the gui
     */
    @FXML
    private Button UserLogButton;
    /**
     * @param event the event triggers an alert for how many times there has been a successful login for the current year
     */
    @FXML
    void OnActionUserLog(ActionEvent event) throws FileNotFoundException {
        // need to read the file and return successful and unsuccessful attempts for current year
        int successNumber = 0;
        String filename = "login_activity.txt", item;
        String thisYear = String.valueOf(LocalDate.now().getYear());
        File file = new File(filename);
        Scanner inputFile = new Scanner(file);
        while(inputFile.hasNext()){
            item = inputFile.nextLine();

            if(item.contains("Successful")&&item.contains("at "+thisYear)){

                successNumber++;

            }
        }
        inputFile.close();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful Logins for Current Year");
        alert.setContentText("There were "+successNumber+" successful logins for the current year "+thisYear);
        alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
        alert.show();
    }


    /**
     * @param event the event switches screens to view appointments
     */
    @FXML
    void OnActionBack(ActionEvent event) throws IOException {
        LoginController lg = new LoginController();
        lg.setStage("/View/ViewAppointments.fxml",event,"Appointments");
    }
    /**
     * @param event the event triggers an alert to display the number of appointments in a current month and type
     */
    @FXML
    void OnActionGenerate(ActionEvent event) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            int currMonth = 0;
            String type = TypeCombo.getValue();
            for(int i = 0; i < months.size(); i++){
                if(MonthCombo.getValue()==months.get(i)){
                    currMonth = i+1;
                }
            }
            int apptmts = AppointmentDB.getCount(type, currMonth);
            alert.setTitle("Appointments by Type & Month");
            alert.setContentText("Appointments: "+apptmts+"\n\n Type: "+type+"\n\n Month: "+MonthCombo.getValue());
            alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
            alert.show();

    }


    /**
     * @param event the event triggers the table to be updated based on the name of the contact
     */

    @FXML
    void OnActionUpdateCombo(ActionEvent event) {
        setTable(AppointmentDB.getFilteredAppointmentsByContact(contactCombo.getValue()));

    }
    /**
     * @param appointments sets the table with the ObservableList appointments items
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
     *  sets the contact combo box
     *  lambda used to set the contact combo box, i used this because it was less lines of code than
     *      *  a for loop and easier to read
     */

    public void setContactCombo(){
        ObservableList<String> contacts = FXCollections.observableArrayList();

        //lambda
        AppointmentDB.getIdsForComboBox("contacts").stream()
                .forEach(i->contacts.add(AppointmentDB.contactIdToContact(i)));


        contactCombo.setItems(contacts);
        contactCombo.setValue(contacts.get(0));

    }

    /**
     *  sets the month combo box
     *
     */

    public void setMonthCombo(){

        ObservableList<String> obsMonths = FXCollections.observableArrayList();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");

        for(int i = 0; i < months.size(); i++){
            obsMonths.add(months.get(i));
        }

        MonthCombo.setItems(obsMonths);
        MonthCombo.setValue(obsMonths.get(0));

    }

    /**
     *  sets the type combo box
     *  lambda used to set the type combo box, i used this because it was less lines of code than
     *  a for loop and easier to read
     */
    public void setTypeCombo(){
        ObservableList<String> types = FXCollections.observableArrayList();
        HashSet<String> set = new HashSet<>();
        //lambda
        AppointmentDB.getAllAppointments().stream().forEach(i->set.add(i.getType()));

        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            types.add(String.valueOf(it.next()));
        }


        TypeCombo.setItems(types);
        TypeCombo.setValue(types.get(0));
    }

    /**
     *  sets the tables and combo box values on when the view is loaded
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){

        setMonthCombo();
        setTypeCombo();
        setContactCombo();
        setTable(AppointmentDB.getFilteredAppointmentsByContact(contactCombo.getValue()));



    }
}
