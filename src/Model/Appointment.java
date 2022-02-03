package Model;

import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Appointment {

    private int apptmtId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerId;
    private int userId;
    private String startDateAttr;
    private String endDateAttr;

    /**constructor for the appointment class*/
    public Appointment(int apptmtId, String title, String description, String location, String contact, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm ");
        this.startDateAttr = start.format(formatter);
        this.endDateAttr = end.format(formatter);
        this.apptmtId = apptmtId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
    }
    /**@return endDateAtrr*/
    public String getEndDateAttr() {
        return endDateAttr;
    }
    /**set endDateAtrr*/
    public void setEndDateAttr(String endDateAttr) {
        this.endDateAttr = endDateAttr;
    }
    /**@return startDateAtrr*/
    public String getStartDateAttr(){
        return startDateAttr;
    }
    /**set startDateAtrr*/
    public void setStartDateAttr(String startDateAttr) {
        this.startDateAttr = startDateAttr;
    }
    /**@return apptmtId*/
    public int getApptmtId() {
        return apptmtId;
    }
    /**set apptmtId*/
    public void setApptmtId(int apptmtId) {
        this.apptmtId = apptmtId;
    }
    /**@return title*/
    public String getTitle() {
        return title;
    }
    /**set title*/
    public void setTitle(String title) {
        this.title = title;
    }
    /**@return description*/
    public String getDescription() {
        return description;
    }
    /**set description*/
    public void setDescription(String description) {
        this.description = description;
    }
    /**@return location*/
    public String getLocation() {
        return location;
    }
    /**set location*/
    public void setLocation(String location) {
        this.location = location;
    }
    /**@return contact*/
    public String getContact() {
        return contact;
    }
    /**setcontact*/
    public void setContact(String contact) {
        this.contact = contact;
    }
    /**@return type*/
    public String getType() {
        return type;
    }
    /**set type*/
    public void setType(String type) {
        this.type = type;
    }
    /**@return start*/
    public LocalDateTime getStart() {
        return start;
    }
    /**set start*/
    public void setStart(LocalDateTime start) {
        this.start = start;
    }
    /**@return end*/
    public LocalDateTime getEnd() {
        return end;
    }
    /**set end*/
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
    /**@return customerId*/
    public int getCustomerId() {
        return customerId;
    }
    /**setcustomerId*/
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    /**@return userId*/
    public int getUserId() {
        return userId;
    }
    /**set userId*/
    public void setUserId(int userId) {
        this.userId = userId;
    }

}
