package clavardage;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import javax.xml.crypto.Data;
import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.lang.Runtime;

public class DataBaseInterface {

    private Clavardage chat;
    private Connection connection;

    private final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private final String dataBaseName = "clavarbase";


    public DataBaseInterface(Clavardage chat) {
        this.chat = chat;

        //connect to the database and create one if
        connectToTheDataBaseAndCreateOneIfNecessary();

        //create messages for testing
        try {
            this.connection.createStatement().execute("DELETE FROM messages");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LocalDateTime dateTime = LocalDateTime.of(2019, 12, 15, 3, 27, 35);

        storeMessage(new Message("Salut ! Comment ça va Jake ?", MessageWay.RECEIVED, dateTime), this.chat.getUsers().getUserFromLogin("John"));
        storeMessage(new Message("Salut John, ça va super bien et toi ?!", MessageWay.SENT, dateTime.plusDays(1)), this.chat.getUsers().getUserFromLogin("John"));
        storeMessage(new Message("ça va, ça va. Immotep.", MessageWay.RECEIVED, dateTime.plusDays(2)), this.chat.getUsers().getUserFromLogin("John"));

        storeMessage(new Message("Salut ! Comment ça va Jake ?", MessageWay.RECEIVED, dateTime), this.chat.getUsers().getUserFromLogin("James"));
        storeMessage(new Message("Salut James, ça va super bien et toi ?!", MessageWay.SENT, dateTime.plusDays(1)), this.chat.getUsers().getUserFromLogin("James"));
        storeMessage(new Message("ça va, ça va. Immotep.", MessageWay.RECEIVED, dateTime.plusDays(2)), this.chat.getUsers().getUserFromLogin("James"));

        //ATTENTION AUX APOSTROPHES
        storeMessage(new Message("Salut ! Comment ça va Jake ?", MessageWay.RECEIVED, dateTime), this.chat.getUsers().getUserFromLogin("Jack"));
        storeMessage(new Message("T es mauvais Jack.", MessageWay.SENT, dateTime.plusDays(1)), this.chat.getUsers().getUserFromLogin("Jack"));

        storeMessage(new Message("Salut ! Comment ça va Jake ?", MessageWay.RECEIVED, dateTime), this.chat.getUsers().getUserFromLogin("Johnson"));
        storeMessage(new Message("Salut Johnson, ça va super bien et toi ?!", MessageWay.SENT, dateTime.plusDays(1)), this.chat.getUsers().getUserFromLogin("Johnson"));
        storeMessage(new Message("ça va, ça va. Immotep.", MessageWay.RECEIVED, dateTime.plusDays(2)), this.chat.getUsers().getUserFromLogin("Johnson"));

        storeMessage(new Message("Salut ! Comment ça va Jake ?", MessageWay.RECEIVED, dateTime), this.chat.getUsers().getUserFromLogin("Jackson"));
        storeMessage(new Message("Salut Jackson, ça va super bien et toi ?!", MessageWay.SENT, dateTime.plusDays(1)), this.chat.getUsers().getUserFromLogin("Jackson"));
        storeMessage(new Message("ça va, ça va. Immotep.", MessageWay.RECEIVED, dateTime.plusDays(2)), this.chat.getUsers().getUserFromLogin("Jackson"));

        //plan database shutdown when the user leaves the application
        DataBaseInterface thisBis = this;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                thisBis.disconnectAndShutDown();
            }
        });
    }

    public void storeMessage(Message message, User distantUser) {
        String content = message.getContent();
        String distantUserMacAddress = distantUser.getMacAddress();
        Boolean isSent = message.isSent();
        Date date = Date.valueOf(message.getDateTime().toLocalDate());
        Time time = Time.valueOf(message.getDateTime().toLocalTime());

        try {
            String sqlQuery = "INSERT INTO messages VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, content);
            preparedStatement.setString(2, distantUserMacAddress);
            preparedStatement.setBoolean(3, isSent);
            preparedStatement.setDate(4, date);
            preparedStatement.setTime(5, time);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Statement error : message could not be stored : ");
            System.out.println(e);
        }
    }

    public ArrayList<Message> getMessages(User user) {
        ArrayList<Message> messages = new ArrayList<Message>();

        try {
            String query = "" +
                    "SELECT content, isSent, date, time " +
                    "FROM messages " +
                    "WHERE distantUserMacAddress = ?" +
                    "ORDER BY date, time";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, user.getMacAddress());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String currentContent = resultSet.getString("content");
                MessageWay currentMessageWay = resultSet.getBoolean("isSent") ? MessageWay.SENT : MessageWay.RECEIVED;
                LocalDate currentDate = resultSet.getDate("date").toLocalDate();
                LocalTime currentTime = resultSet.getTime("time").toLocalTime();
                LocalDateTime currentDateTime = LocalDateTime.of(currentDate, currentTime);
                messages.add(new Message(currentContent, currentMessageWay, currentDateTime));
            }
            //System.out.println("Successfully retrieved messages exchanged with " + user + " : ");
        } catch (SQLException e) {
            System.out.println("Statement error : could not get messages exchanged with user " + user + " : ");
            System.out.println(e);
        } finally {
            return messages;
        }
    }

    public void connectToTheDataBaseAndCreateOneIfNecessary() {
        try {
            //database driver initialization
            Class.forName(this.driver);
            System.out.println("Apache Derby driver loaded.");

            //if the database already exists then the connection succeeds else we create the database
            try {
                //connection to the local database
                System.out.println("Trying to connect to the local database");
                this.connection = DriverManager.getConnection("jdbc:derby:" + this.dataBaseName);
                System.out.println("Connection established");
            } catch(SQLException noDataBaseFound) {
                try {
                    //creation of and connection to the local database
                    System.out.println("No database found. Trying to create a local database and connect to it");
                    this.connection = DriverManager.getConnection("jdbc:derby:" + this.dataBaseName + ";create=true");
                    System.out.println("Database created and connection established");

                    //initialization statements (tables and data to start)
                    try {
                        this.connection.createStatement().execute("" +
                                "CREATE TABLE messages (" +
                                "   content VARCHAR(500)," +
                                "   distantUserMacAddress VARCHAR(17)," +
                                "   isSent BOOLEAN," +
                                "   date DATE," +
                                "   time TIME" +
                                ")"
                        );
                    } catch (SQLException statementError) {
                        System.out.println("Fatal Error : Table creation statement error : ");
                        System.out.println(statementError);
                        System.exit(1);
                    }
                } catch(SQLException dataBaseCreationError) {
                    System.out.println("Fatal Error : Could not create a database : ");
                    System.out.println(dataBaseCreationError);
                    System.exit(1);
                }
            }
        } catch(ClassNotFoundException driverClassNotFound) {
            System.out.println("Fatal Error : Apache Derby driver class not found : ");
            System.out.println(driverClassNotFound);
            System.exit(1);
        }
    }

    public void disconnectAndShutDown() {
        //closing the connection and shuting down the database
        try {
            //disconnection from the database
            this.connection.close();
            System.out.println("Disconnected from the database");

            //shuting down the database
            boolean gotSQLExc = false;
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException shutDownError) {
                if (shutDownError.getSQLState().equals("XJ015")) {
                    gotSQLExc = true;
                }
            }
            if (!gotSQLExc) {
                System.out.println("Database did not shut down normally");
            } else {
                System.out.println("Database shut down normally");
            }

            //unloading the database driver
            System.gc();

        } catch (SQLException disconnectionError) {
            System.out.println("Could not disconnect from the database : ");
            System.out.println(disconnectionError);
        }
    }
}
