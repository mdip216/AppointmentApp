package Model;

import Database.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.transform.Result;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDB {


   public static ObservableList<User> getAllUsers() {
      ObservableList<User> users = FXCollections.observableArrayList();

      try{
         String sql = "SELECT User_ID, User_Name, Password from users";

         PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

         ResultSet rs = ps.executeQuery();

         while(rs.next()){
            int userID = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");
            User user = new User(userID,userName,password);
            users.add(user);

         }

      } catch(SQLException throwables){
         throwables.printStackTrace();
      }

      return users;
   }





}
