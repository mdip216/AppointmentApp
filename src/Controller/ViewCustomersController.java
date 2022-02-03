package Controller;
/**
 *
 * @author Matt DiPerna
 */
import Model.Appointment;
import Model.AppointmentDB;
import Model.Customer;
import Model.CustomerDB;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewCustomersController implements Initializable {
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
    private Button AddBtn;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> AddressCol;
    /**
     * an element of the gui
     */
    @FXML
    private TableView<Customer> CustomerView;
    /**
     * an element of the gui
     */
    @FXML
    private Button BackBtn;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> CountryCol;
    /**
     * an element of the gui
     */
    @FXML
    private Button DeleteBtn;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> DivisionCol;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> IDCol;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> NameCol;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> PhoneNumberCol;
    /**
     * an element of the gui
     */
    @FXML
    private TableColumn<?, ?> PostalCol;
    /**
     * an element of the gui
     */
    @FXML
    private Button UpdateBtn;
    /**
     * @param event the event triggers function to switch to the customer view
     */
    @FXML
    void OnActionAdd(ActionEvent event) throws IOException {
        LoginController lg = new LoginController();
        lg.setStage("/View/AddCustomer.fxml",event,"Add Customer");
    }

    /**
     * @param event the event triggers function to switch to the appointments view
     */

    @FXML
    void OnActionBack(ActionEvent event) throws IOException {
        LoginController lg = new LoginController();
        lg.setStage("/View/ViewAppointments.fxml",event,"Appointments");
    }
    /**
     * @param event the event triggers function to delete an appointment
     */
    @FXML
    void OnActionDelete(ActionEvent event) {
        try{
            int id = CustomerView.getSelectionModel().getSelectedItem().getCustomerID();

            for(Customer i : CustomerDB.getAllCustomers()){
                if(i.getCustomerID()==id){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Deletion");
                    alert.setContentText("Are you sure you wish to delete the selected customer?");
                    alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");

                    //we need to check if they press cancel if they do we do not delete
                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.isPresent() && result.get()==ButtonType.CANCEL){
                        break;
                    }
                    //need an sql query for delete
                    CustomerDB.deleteCustomer(id);
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Customer Deleted");
                    alert2.setContentText("The customer was deleted\n");
                    alert2.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
                    alert2.show();
                    setTable(CustomerDB.getAllCustomers());
                    break;

                }
            }
        }catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: No Customer Selected");
            alert.setContentText("Please select the customer you wish to delete");
            alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
            alert.show();
        }
    }
    /**
     * @param event the event triggers function to switch to the update customer view
     */
    @FXML
    void OnActionUpdate(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/UpdateCustomer.fxml"));
            loader.load();
            UpdateCustomerController controller = loader.getController();
            //send selected customer
            controller.sendCustomer(CustomerView.getSelectionModel().getSelectedItem());
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
            alert.setContentText("Please select a customer");
            alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
            alert.show();
        }
    }
    /**
     * @param customers and ObservableList for setting the customer table with the function
     */
    public void setTable(ObservableList<Customer> customers){

        CustomerView.setItems(customers);
        //automatically calls get methods

        IDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        NameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        AddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        PostalCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        DivisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        CountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        PhoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

    }

    /**
     * sets the customers table
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){

        setTable(CustomerDB.getAllCustomers());

    }

}
