package Controller;

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

    @FXML
    private TableView<Appointment> ApptmtsView;

    @FXML
    private Button BackBtn;

    @FXML
    private TableColumn<?, ?> ContactCol;

    @FXML
    private TableColumn<?, ?> CustIDCol;

    @FXML
    private TableColumn<?, ?> DescriptionCol;

    @FXML
    private TableColumn<?, ?> EndCol;

    @FXML
    private Button GenerateBtn;

    @FXML
    private TableColumn<?, ?> IDCol;

    @FXML
    private TableColumn<?, ?> LocationCol;


    @FXML
    private ComboBox<String> MonthCombo;

    @FXML
    private TableColumn<?, ?> StartCol;

    @FXML
    private TableColumn<?, ?> TitleCol;



    @FXML
    private TableColumn<?, ?> TypeCol;

    @FXML
    private Button UpdateBtn;

    @FXML
    private TableColumn<?, ?> UserIDCol;

    @FXML
    private ComboBox<String> contactCombo;

    @FXML
    private ComboBox<String> TypeCombo;

    @FXML
    private Button UserLogButton;

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
            //System.out.println(item);
            if(item.contains("Successful")&&item.contains("at "+thisYear)){
                //System.out.println("found");
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



    @FXML
    void OnActionBack(ActionEvent event) throws IOException {
        LoginController lg = new LoginController();
        lg.setStage("/View/ViewAppointments.fxml",event,"Appointments");
    }

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




    @FXML
    void OnActionUpdateCombo(ActionEvent event) {
        setTable(AppointmentDB.getFilteredAppointmentsByContact(contactCombo.getValue()));

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

    public void setContactCombo(){
        ObservableList<String> contacts = FXCollections.observableArrayList();
        for(Integer i : AppointmentDB.getIdsForComboBox("contacts")){
            contacts.add(AppointmentDB.contactIdToContact(i));
        }

        contactCombo.setItems(contacts);
        contactCombo.setValue(contacts.get(0));

    }

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

    public void setTypeCombo(){
        ObservableList<String> types = FXCollections.observableArrayList();
        HashSet<String> set = new HashSet<>();
        for(Appointment i :AppointmentDB.getAllAppointments()){
            set.add(i.getType());

        }
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            types.add(String.valueOf(it.next()));
        }


        TypeCombo.setItems(types);
        TypeCombo.setValue(types.get(0));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){

        //setTable(AppointmentDB.getFilteredAppointmentsByContact());
        setMonthCombo();
        setTypeCombo();
        setContactCombo();
        setTable(AppointmentDB.getFilteredAppointmentsByContact(contactCombo.getValue()));



    }
}
