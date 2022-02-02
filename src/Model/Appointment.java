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

    public String getEndDateAttr() {
        return endDateAttr;
    }

    public void setEndDateAttr(String endDateAttr) {
        this.endDateAttr = endDateAttr;
    }

    public String getStartDateAttr(){
        return startDateAttr;
    }

    public void setStartDateAttr(String startDateAttr) {
        this.startDateAttr = startDateAttr;
    }

    public int getApptmtId() {
        return apptmtId;
    }

    public void setApptmtId(int apptmtId) {
        this.apptmtId = apptmtId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
