package Model;

import Database.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class AppointmentDB {
    /**string for fifteen minute alert*/
    private static String fifteenMinStr;
    /**string for delete alert*/
    private static String onDeleteStr;

    /**method for getting all appointments in the database*/
    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Appointments.Appointment_ID,Appointments.title,Appointments.description,Appointments.location,Appointments.type,Appointments.start,Appointments.end,Appointments.customer_id,Appointments.user_id,Contacts.Contact_name from Appointments\n" +
                    "INNER JOIN Contacts \n" +
                    "on Appointments.Contact_ID = Contacts.Contact_ID;";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int apptmtId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String contact = rs.getString("Contact_Name");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");

                Appointment appt = new Appointment(apptmtId, title, description, location, contact, type, start, end, customerId, userId);
                appointments.add(appt);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointments;
    }
    /**method for getting all filtered appointments in the database by the time and date*/
    public static ObservableList<Appointment> getFilteredAppointments(ZonedDateTime beg, ZonedDateTime stop) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {


            String begFormat = beg.format(formatter);
            String stopFormat = stop.format(formatter);


            PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT Appointments.Appointment_ID,Appointments.title,Appointments.description,Appointments.location,Appointments.type,Appointments.start,Appointments.end,Appointments.customer_id,Appointments.user_id,Contacts.Contact_name from Appointments\n" +
                    "INNER JOIN Contacts\n" +
                    "on Appointments.Contact_ID = Contacts.Contact_ID" +
                    " WHERE Start BETWEEN ? and ?;");
            ps.setString(1, begFormat);
            ps.setString(2, stopFormat);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
                int apptmtId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String contact = rs.getString("Contact_Name");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                ;//.toLocalDateTime().atZone(ZoneId.systemDefault());
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");

                Appointment appt = new Appointment(apptmtId, title, description, location, contact, type, start, end, customerId, userId);
                appointments.add(appt);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointments;
    }
    /**method for saving appointments in the database*/
    public static void saveAddedAppointment(String title, String description, String location, String contact, String type, ZonedDateTime newStart, ZonedDateTime newEnd, int custId, int userId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement("INSERT INTO appointments VALUES(NULL,?, \n" +
                    "?, ?, ?, \n" +
                    "?, ?, NOW(), 'script', NOW(), 'script', ?, ?, ?);");
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            String start = newStart.withZoneSameInstant(ZoneOffset.UTC).format(formatter);
            String end = newEnd.withZoneSameInstant(ZoneOffset.UTC).format(formatter);
            ps.setString(5, start);
            ps.setString(6, end);
            ps.setInt(7, custId);
            ps.setInt(8, userId);
            ps.setInt(9, contactToContactId(contact));
            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    /**method for saving updated appointments in the database*/
    public static void saveUpdatedAppointment(int appmtId, String title, String description, String location, String contact, String type, ZonedDateTime newStart, ZonedDateTime newEnd, int custId, int userId) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement("UPDATE appointments\n" +
                    "SET title = ?, description = ?, location = ?, contact_id = ?, type = ?, start = ?, end = ?, customer_id = ?, user_id = ?\n" +
                    "WHERE Appointment_ID = ?;");
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);

            // contact needs to be converted back to contact_id
            ps.setInt(4, contactToContactId(contact));



            ps.setString(5, type);

            //convert zonedatetimes to correct format in UTC
            String start = newStart.withZoneSameInstant(ZoneOffset.UTC).format(formatter);
            String end = newEnd.withZoneSameInstant(ZoneOffset.UTC).format(formatter);
            ps.setString(6, start);
            ps.setString(7, end);

            ps.setInt(8, custId);
            ps.setInt(9, userId);
            ps.setInt(10, appmtId);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
    /**method for deleting appointments in the database*/
    public static String deleteAppointment(int apptmtId, String type) {
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement("delete from appointments \n" +
                    "where Appointment_ID = ?;");
            ps.setInt(1, apptmtId);
            ps.executeUpdate();
            //need to set the string
            onDeleteStr = "Appointment ID: " + String.valueOf(apptmtId) + "\n Type: " + type;
            return onDeleteStr;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        throw new IllegalArgumentException("Not found");
    }

    /**method for checking EST business hours*/
    public static boolean estCheck(ZonedDateTime start, ZonedDateTime end, LocalDate date) {
        // Business hours are in EST
        ZoneId zoneId = ZoneId.of("America/New_York");
        LocalTime am = LocalTime.of(8, 00);
        LocalTime pm = LocalTime.of(22, 00);

        ZonedDateTime morning = ZonedDateTime.of(date, am, zoneId);
        ZonedDateTime night = ZonedDateTime.of(date, pm, zoneId);

        if (start.isBefore(morning) || end.isAfter(night)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Time Format");
            alert.setContentText("Business hours are closed during this time");
            alert.getDialogPane().setStyle("-fx-font-family: 'Times New Roman';");
            alert.show();
            return false;
        }
        return true;
    }
    /**method for time combo box*/
    public static ObservableList<String> createTimes() {
        ObservableList<String> times = FXCollections.observableArrayList();

        for (int i = 0; i < 24; i++) {
            String time;
            String timePlusFifteen;
            String timePlusThirty;
            String timePlusFortyFive;
            if (i < 10) {
                time = "0" + i + ":" + "00";
                timePlusFifteen = "0" + i + ":" + "15";
                timePlusThirty = "0" + i + ":" + "30";
                timePlusFortyFive = "0" + i + ":" + "45";
            } else {
                time = i + ":" + "00";
                timePlusFifteen = i + ":" + "15";
                timePlusThirty = i + ":" + "30";
                timePlusFortyFive = i + ":" + "45";
            }
            times.add(time);
            times.add(timePlusFifteen);
            times.add(timePlusThirty);
            times.add(timePlusFortyFive);
        }

        return times;
    }

    /**method for id combo box*/
    public static ObservableList<Integer> getIdsForComboBox(String idName) {
        //return the observable array list of int ids
        ObservableList<Integer> ids = FXCollections.observableArrayList();
        //customer
        if (idName.equals("customer")) {
            try {
                PreparedStatement ps = JDBC.getConnection().prepareStatement("select customer_id\n" +
                        "from customers;");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    ids.add(rs.getInt("Customer_ID"));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //user
        } else if (idName.equals("user")) {
            try {
                PreparedStatement ps = JDBC.getConnection().prepareStatement("select user_id\n" +
                        "from users;");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    ids.add(rs.getInt("User_ID"));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //contacts
        } else if (idName.equals("contacts")) {
            try {
                PreparedStatement ps = JDBC.getConnection().prepareStatement("select Contact_id\n" +
                        "from Contacts;");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    ids.add(rs.getInt("Contact_ID"));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return ids.sorted();
    }

    /**method for converting contact id to contact name string*/
    public static String contactIdToContact(int id) {
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement("select contact_name \n" +
                    "from contacts\n" +
                    "where contact_id = ?; ");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("Contact_name");
                return name;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        throw new IllegalArgumentException("Not found");
    }
    /**method for converting contact string to contact id int*/
    public static int contactToContactId(String name) throws SQLException {

        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement("select contact_id\n" +
                    "from contacts\n" +
                    "where contact_name = ?; ");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("Contact_ID");
                return id;

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }
    /**method for checking for appointments within fifteen minutes of login*/
    public static boolean fifteenMinCheck() {
        for (Appointment i : getAllAppointments()) {
            // these are in local time convert them to zone
            ZonedDateTime zdt = i.getStart().atZone(ZoneId.systemDefault());

            //get now in zone time
            ZonedDateTime now = ZonedDateTime.now();

            if (now.isBefore(zdt) && now.isAfter(zdt.minusMinutes(15))) {
                //get id date and time of i
                fifteenMinStr = i.getApptmtId() + " at " + i.getStartDateAttr();

                return true;
            }
        }

        return false;
    }
    /**@return fifteenMinStr*/
    public static String getFifteenMinStr() {
        return fifteenMinStr;
    }
    /**method for checking for repeating customer appointments*/
    public static boolean repeatCustomerAppointment(int apptmtId, int custId, ZonedDateTime start, ZonedDateTime end) { //function isnt finished
        for (Appointment i : getAllAppointments()) {
            if (i.getCustomerId() == custId && i.getApptmtId() != apptmtId) {

                //check times
                ZonedDateTime existingStart = i.getStart().atZone(ZoneId.systemDefault());
                ZonedDateTime existingEnd = i.getEnd().atZone(ZoneId.systemDefault());


                //with start before existing start
                if (start.isBefore(existingStart) && end.isAfter(existingStart)) {
                    return true;
                }
                if (start.isBefore(existingStart) && end.equals(existingEnd)) {
                    return true;
                }
                if (start.isBefore(existingStart) && end.isAfter(existingEnd)) {
                    return true;
                }

                //with starts equal
                if (start.isEqual(existingStart) && end.isAfter(existingStart)) {
                    return true;
                }
                if (start.isEqual(existingStart) && end.equals(existingEnd)) {
                    return true;
                }
                if (start.isEqual(existingStart) && end.isAfter(existingEnd)) {
                    return true;
                }

                //start after existing start
                if (start.isAfter(existingStart) && end.isBefore(existingEnd)) {
                    return true;
                }
                if (start.isAfter(existingStart) && end.equals(existingEnd)) {
                    return true;
                }
                if (start.isAfter(existingStart) && start.isBefore(existingEnd) && end.isAfter(existingEnd)) {
                    return true;
                }
            } else if (i.getCustomerId() == custId && apptmtId == Integer.MIN_VALUE) {
                ZonedDateTime existingStart = i.getStart().atZone(ZoneId.systemDefault());
                ZonedDateTime existingEnd = i.getEnd().atZone(ZoneId.systemDefault());


                //with start before existing start
                if (start.isBefore(existingStart) && end.isAfter(existingStart)) {
                    return true;
                }
                if (start.isBefore(existingStart) && end.equals(existingEnd)) {
                    return true;
                }
                if (start.isBefore(existingStart) && end.isAfter(existingEnd)) {
                    return true;
                }

                //with starts equal
                if (start.isEqual(existingStart) && end.isAfter(existingStart)) {
                    return true;
                }
                if (start.isEqual(existingStart) && end.equals(existingEnd)) {
                    return true;
                }
                if (start.isEqual(existingStart) && end.isAfter(existingEnd)) {
                    return true;
                }

                //start after existing start
                if (start.isAfter(existingStart) && end.isBefore(existingEnd)) {
                    return true;
                }
                if (start.isAfter(existingStart) && end.equals(existingEnd)) {
                    return true;
                }
                if (start.isAfter(existingStart) && start.isBefore(existingEnd) && end.isAfter(existingEnd)) {
                    return true;
                }
            }

        }

        return false;
    }
    /**method for checking for filtering appointments by contact string*/
    public static ObservableList<Appointment> getFilteredAppointmentsByContact(String contactName) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {

            PreparedStatement ps = JDBC.getConnection().prepareStatement("Select Appointments.Appointment_ID,Appointments.title,Appointments.description,Appointments.location,Appointments.type,Appointments.start,Appointments.end,Appointments.customer_id,Appointments.user_id,Contacts.Contact_name from Appointments\n" +
                    "                    INNER JOIN Contacts \n" +
                    "                    on Appointments.Contact_ID = Contacts.Contact_ID\n" +
                    "                     WHERE Contacts.Contact_ID =?");
            //System.out.println(contactToContactId(contactName));
            ps.setInt(1, contactToContactId(contactName));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
                int apptmtId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String contact = rs.getString("Contact_Name");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                ;//.toLocalDateTime().atZone(ZoneId.systemDefault());
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");

                Appointment appt = new Appointment(apptmtId, title, description, location, contact, type, start, end, customerId, userId);
                appointments.add(appt);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointments;
    }

    /**method for getting the number of appointments given a month and type*/
    public static int getCount(String type, int month ) {
        int count = 0;
        try {

            PreparedStatement ps = JDBC.getConnection().prepareStatement("select count(*) as count from appointments where type = ? and MONTH(Start) = ?;");
            ps.setString(1, type);
            ps.setInt(2,month);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){count = rs.getInt("count");}
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }


}