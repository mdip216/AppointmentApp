package Controller;

import Model.CustomerDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    /**
     * an element of the gui
     */
    @FXML
    private TextField AddressTxt;
    /**
     * an element of the gui
     */
    @FXML
    private Button CancelBtn;
    /**
     * an element of the gui
     */
    @FXML
    private ComboBox<String> CountryCombo;
    /**
     * an element of the gui
     */
    @FXML
    private TextField CustomerIDTxt;
    /**
     * an element of the gui
     */
    @FXML
    private ComboBox<String> DivisionCombo;
    /**
     * an element of the gui
     */
    @FXML
    private TextField NameTxt;
    /**
     * an element of the gui
     */
    @FXML
    private TextField PhoneTxt;
    /**
     * an element of the gui
     */
    @FXML
    private TextField PostalCodeTxt;
    /**
     * an element of the gui
     */
    @FXML
    private Button SaveBtn;
    /**
     * @param event the event triggers function to switch back to the view customers page
     */
    @FXML
    void OnActionCancel(ActionEvent event) throws IOException {
        LoginController lg = new LoginController();
        lg.setStage("/View/ViewCustomers.fxml",event,"Customers");
    }
    /**
     * @param event the event triggers function to update the division combo box based on country
     */
    @FXML
    void OnActionCountryChange(ActionEvent event) {
        String country = CountryCombo.getValue();
        DivisionCombo.setItems(CustomerDB.getDivisions(country));
        DivisionCombo.setValue(CustomerDB.getDivisions(country).get(0));
    }

    /**
     * @param event the event triggers function to save the new customer and checks for errors
     */
    @FXML
    void OnActionSave(ActionEvent event) throws IOException {
       try {
           String Name = NameTxt.getText();
           String address = AddressTxt.getText();
           String postalCode = PostalCodeTxt.getText();
           String phoneNumber = PhoneTxt.getText();
           String division = DivisionCombo.getValue();
           String country = CountryCombo.getValue();
           if(Name.isEmpty()||address.isEmpty()||postalCode.isEmpty()||phoneNumber.isEmpty()||division.isEmpty()||country.isEmpty()){
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Incomplete Customer");
               alert.setContentText("Please fill out all required items!");
               alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
               alert.show();
               return;
           }

           CustomerDB.saveAddedCustomer(Name, address, postalCode, phoneNumber, division, country);
           //switch back to view screen
           LoginController lg = new LoginController();
           lg.setStage("/View/ViewCustomers.fxml", event, "Customers");
       }catch(NumberFormatException e){
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Incomplete Appointment");
           alert.setContentText("Please fill out all required items!");
           alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
           alert.show();
       }

    }

    /**
     * sets the gui elements on initialization */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        CountryCombo.setItems(CustomerDB.getCountries());
        CountryCombo.setValue(CustomerDB.getCountries().get(0));
        DivisionCombo.setItems(CustomerDB.getDivisions(CustomerDB.getCountries().get(0)));
        DivisionCombo.setValue(CustomerDB.getDivisions(CustomerDB.getCountries().get(0)).get(0));
    }

}
