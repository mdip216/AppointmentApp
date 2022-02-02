package Controller;

import Model.Customer;
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

public class UpdateCustomerController implements Initializable {



    @FXML
    private TextField AddressTxt;

    @FXML
    private TextField CustomerIDTxt;

    @FXML
    private Button CancelBtn;

    @FXML
    private ComboBox<String> CountryCombo;

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
    void OnActionCountryChange(ActionEvent event) {
        String country = CountryCombo.getValue();
        DivisionCombo.setItems(CustomerDB.getDivisions(country));
        DivisionCombo.setValue(CustomerDB.getDivisions(country).get(0));
    }

    @FXML
    void OnActionCancel(ActionEvent event) throws IOException {
        LoginController lg = new LoginController();
        lg.setStage("/View/ViewCustomers.fxml",event,"Customers");
    }

    @FXML
    void OnActionSave(ActionEvent event) throws IOException {
        //up to here
        int CustomerId = Integer.valueOf(CustomerIDTxt.getText());
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

        CustomerDB.saveUpdatedCustomer(CustomerId,Name,address,postalCode,phoneNumber,division,country);
        //switch back to view screen
        LoginController lg = new LoginController();
        lg.setStage("/View/ViewCustomers.fxml",event,"Customers");

    }

    public void sendCustomer(Customer customer){

        CustomerIDTxt.setText(String.valueOf(customer.getCustomerID()));
        NameTxt.setText(String.valueOf(customer.getName()));
        AddressTxt.setText(String.valueOf(customer.getAddress()));
        PhoneTxt.setText(String.valueOf(customer.getPhoneNumber()));
        //need a list for countries
        CountryCombo.setItems(CustomerDB.getCountries());
        CountryCombo.setValue(String.valueOf(customer.getCountry()));
        // need a list for divisions based on selected country and everytime countrycombobox changes
        DivisionCombo.setItems(CustomerDB.getDivisions(String.valueOf(customer.getCountry())));
        DivisionCombo.setValue(String.valueOf(customer.getDivision()));
        PostalCodeTxt.setText(String.valueOf(customer.getPostalCode()));

    }

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }
}
