package clavardage;

import java.sql.*;
import java.time.LocalDateTime;
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

        //test
        this.storeMessage(new Message("salut !", MessageWay.SENT), new User("perlotte", "255.255.255.255", "ff:ff:ff:ff:ff:ff"));

        //plan database shutdown when the user leaves the application
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                disconnectAndShutDown();
            }
        });
    }

    public void storeMessage(Message message, User distantUser) {
        String content = "'"+message.getContent()+"'";
        String distantUserMacAddress = "'"+distantUser.getMacAddress()+"'";
        String isSent = message.isSent() ? "true" : "false";
        String date = "'"+message.getDateTime().getYear()+"-"+message.getDateTime().getMonthValue()+"-"+message.getDateTime().getDayOfMonth()+"'";
        String time = "'"+message.getDateTime().getHour()+":"+message.getDateTime().getMinute()+":"+message.getDateTime().getSecond()+"'";

        String sqlRequest = "INSERT INTO messages VALUES ("+content+", "+distantUserMacAddress+", "+isSent+", "+date+", "+time+")";
        System.out.println(sqlRequest);
        try {
            //System.out.println(sqlRequest);
            this.connection.createStatement().execute(sqlRequest);
            System.out.println("Message stored correctly");
        } catch (SQLException e) {
            System.out.println("Statement error : message could not be stored : ");
            System.out.println(e);
        }
    }

    public ArrayList<Message> getMessages(User user) {
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
                String date = resultSet.getDate("date").
                LocalDateTime currentDateTime = LocalDateTime.of
                //resultSet.getTime("time")
            }

            ArrayList<Message> messages = new ArrayList<Message>();
            System.out.println("Successfully retrieved messages exchanged with " + user + " : ");
        } catch (SQLException e) {
            System.out.println("Statement error : could not get messages exchanged with user " + user + " : ");
            System.out.println(e);
        } finally {
            return messages;
        }
    }

    /*
    public void getUsers(ArrayList<User> connectedUsers) {
    }
    */

    public void storeUser(User user) {

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
                System.out.println("Connected established");
            } catch(SQLException noDataBaseFound) {
                try {
                    //creation of and connection to the local database
                    System.out.println("No database found. Trying to create a local database and connect to it");
                    this.connection = DriverManager.getConnection("jdbc:derby:" + this.dataBaseName + ";create=true");
                    System.out.println("Database created and connection established");

                    //initialization statements (tables and data to start)
                    try {
                        /*
                        connection.createStatement().execute("" +
                                "CREATE TABLE users (" +
                                "   macAddress VARCHAR(17) NOT NULL CONSTRAINT usersPK PRIMARY KEY" +
                                ")"
                        );

                        connection.createStatement().execute("" +
                                "INSERT INTO users VALUES ('f8:28:19:73:f2:1f')"
                        );
                        */
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
