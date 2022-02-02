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


    @FXML
    private TextField AddressTxt;

    @FXML
    private Button CancelBtn;

    @FXML
    private ComboBox<String> CountryCombo;

    @FXML
    private TextField CustomerIDTxt;

    @FXML
    private ComboBox<String> DivisionCombo;

    @FXML
    private TextField NameTxt;

    @FXML
    private TextField PhoneTxt;

    @FXML
    private TextField PostalCodeTxt;

    @FXML
    private Button SaveBtn;

    @FXML
    void OnActionCancel(ActionEvent event) throws IOException {
        LoginController lg = new LoginController();
        lg.setStage("/View/ViewCustomers.fxml",event,"Customers");
    }

    @FXML
    void OnActionCountryChange(ActionEvent event) {
        String country = CountryCombo.getValue();
        DivisionCombo.setItems(CustomerDB.getDivisions(country));
        DivisionCombo.setValue(CustomerDB.getDivisions(country).get(0));
    }

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

    @Override
    public void initialize(URL url, ResourceBundle rb){
        CountryCombo.setItems(CustomerDB.getCountries());
        CountryCombo.setValue(CustomerDB.getCountries().get(0));
        DivisionCombo.setItems(CustomerDB.getDivisions(CustomerDB.getCountries().get(0)));
        DivisionCombo.setValue(CustomerDB.getDivisions(CustomerDB.getCountries().get(0)).get(0));
    }

}
