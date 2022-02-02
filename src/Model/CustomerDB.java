package Model;

import Database.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDB {

    public static ObservableList<Customer> getAllCustomers(){
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try{

            PreparedStatement ps = JDBC.getConnection().prepareStatement(" SELECT c.Customer_ID, c.Customer_Name, c.Address, c.Postal_Code, c.Phone, c.Division_ID, \n" +
                    "f.Division, f.Country_ID, countries.Country \n" +
                    "FROM customers as c INNER JOIN first_level_divisions \n" +
                    "as f on c.Division_ID = f.Division_ID INNER JOIN countries ON f.Country_ID = countries.Country_ID;");

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int customerID = rs.getInt("Customer_ID");
                String Name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                String division = rs.getString("Division");
                String country = rs.getString("Country");

                Customer cust = new Customer(customerID,Name,address,postalCode,phoneNumber,division,country);
                customers.add(cust);
            }




        }catch(SQLException throwables){
            throwables.printStackTrace();
        }

        return customers;
    }

    public static ObservableList<String> getCountries(){
        ObservableList<String> countries = FXCollections.observableArrayList();
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement("select country from countries;");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                countries.add(rs.getString("country"));
            }
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }

        return countries;
    }

    public static ObservableList<String> getDivisions(String country){
        ObservableList<String> divisions = FXCollections.observableArrayList();

        //search for these division where country_id = country
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement("select * \n" +
                    "from countries\n" +
                    "Inner join first_level_divisions as division on division.country_id = countries.country_id\n" +
                    "where country = ?;");
            ps.setString(1,country);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String division = rs.getString("division");
                divisions.add(division);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return divisions;
    }

    public static void saveUpdatedCustomer(int CustomerId,String Name,String address,String postalCode,String phoneNumber,String division,String country){
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement("update customers \n" +
                    "set customer_name = ?, address = ?, postal_code = ?, phone = ?, division_id = ?\n" +
                    "where customer_id = ?;");
            ps.setString(1,Name);
            ps.setString(2,address);
            ps.setString(3,postalCode);
            ps.setString(4,phoneNumber);
            //convert division to division_id
            ps.setInt(5,getDivisionId(division));
            ps.setInt(6,CustomerId);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static int getDivisionId(String name){
        try{PreparedStatement ps = JDBC.getConnection().prepareStatement("Select division_id from first_level_divisions where division = ?");
            ps.setString(1,name);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("division_ID");
                return id;

            }
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return 0;
    }

    public static void deleteCustomer(int customerId){
        try{

            PreparedStatement ps = JDBC.getConnection().prepareStatement(
                    "delete from customers where customer_id = ?");
            ps.setInt(1,customerId);
            ps.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public static void saveAddedCustomer(String Name,String address,String postalCode,String phoneNumber,String division,String country){
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement("INSERT INTO customers VALUES(NULL, ?, ?, ?, " +
                    "?, NOW(), 'user', NOW(), 'user', ?);");
            ps.setString(1,Name);
            ps.setString(2,address);
            ps.setString(3,postalCode);
            ps.setString(4,phoneNumber);
            //convert division to division_id
            ps.setInt(5,getDivisionId(division));
            ps.executeUpdate();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
