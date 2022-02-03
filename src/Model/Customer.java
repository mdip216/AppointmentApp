package Model;

public class Customer {
    private int customerID;
    private String Name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String division;
    private String country;

    /**constructor for the customer class*/
    public Customer(int customerID, String name, String address, String postalCode, String phoneNumber, String division, String country) {
        this.customerID = customerID;
        Name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.division = division;
        this.country = country;
    }
    /**@return customerId*/
    public int getCustomerID() {
        return customerID;
    }
    /**set customerId*/
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    /**@return name*/
    public String getName() {
        return Name;
    }
    /**set name*/
    public void setName(String name) {
        Name = name;
    }
    /**@return address*/
    public String getAddress() {
        return address;
    }
    /**set address*/
    public void setAddress(String address) {
        this.address = address;
    }
    /**@return postalcode*/
    public String getPostalCode() {
        return postalCode;
    }
    /**set postalcode*/
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    /**@return phonenumber*/
    public String getPhoneNumber() {
        return phoneNumber;
    }
    /**set phonenumber*/
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    /**@return division*/
    public String getDivision() {
        return division;
    }
    /**setdivision*/
    public void setDivision(String division) {
        this.division = division;
    }
    /**@return country*/
    public String getCountry() {
        return country;
    }
    /**set country*/
    public void setCountry(String country) {
        this.country = country;
    }
}
