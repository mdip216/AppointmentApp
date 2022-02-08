Title: AppointmentApp

Purpose: A bilingual JavaFX Application used for scheduling appointments connected to a database. 

Description: Create, retrieve, modify and delete appointments from a MySql datatbase using JDBC while accounting for foreign key constraints. Login form determines and displays user location, and translates between English or French using .properties based on user's computer settings. Accepts correct user ID and password and displays a message if incorrect. Logs all user login attempts in a .txt file. Generates a message if there is an appointment within 15 minutes of login. Accounts for switching time zones between the database (UTC) and the time zone of the user's location using ZonedDateTime, LocalDateTime, Timestamp classes. Generates alerts for incorrect information entered for an appointment. Takes into consideration overlapping appointments for the same customer. Generates multiple different reports based on the entities of the database. 

JDK Version: Java-jdk-11.0.11

JavaFX Version: javafx-sdk-11.0.2

How to run: In the JDBC.java file replace all the strings in quotation marks with your own information for the Mysql database on your local machine 

MySql Version Number: mysql-connector-java-8.0.25
