package database;

import clavardage.Clavardage;
import clavardage.Message;
import clavardage.User;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.lang.Runtime;

/**
 * Model. Handle data storage and retrieving.
 */
public class DataBaseInterface {
    /**
     * Used to ensure single instantiation.
     */
    private static boolean instantiated = false;

    /**
     * The controller.
     */
    private Clavardage chat;
    /**
     * Represents a connection to the database.
     */
    private Connection connection;

    /**
     * The name of the driver class.
     */
    private final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    /**
     * Name of the database. The database directory will also have this name.
     */
    private final String dataBaseName = "clavarbase";

    /**
     * Constructor.
     * @param chat the controller
     */
    private DataBaseInterface(Clavardage chat) {
        this.chat = chat;

        connectToTheDataBaseAndCreateOneIfNecessary();

        //plan database shutdown when the user leaves the application
        DataBaseInterface thisBis = this;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                thisBis.disconnectAndShutDown();
            }
        });
    }

    /**
     * Create an instance of the class. It raises an error if called twice.
     * @param chat the controller
     * @return an instance of the class
     */
    public static synchronized DataBaseInterface instantiate(Clavardage chat) {
        DataBaseInterface db = null;
        if(!DataBaseInterface.instantiated) {
            DataBaseInterface.instantiated = true;
            db = new DataBaseInterface(chat);
        } else {
            System.out.println("Fatal error : DataBaseInterface can not be instantiated twice");
            System.exit(1);
        }
        return db;
    }

    /**
     * Add a user to the known users list in the database.
     * @param user the user you want to add to the known users list
     */
    public void addUser(User user) {
        String macAddress = user.getMacAddress();
        String login = user.getLogin();
        System.out.println(macAddress);
        System.out.println(macAddress.length());

        try {
            String sqlQuery = "INSERT INTO users VALUES (?, ?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, macAddress);
            preparedStatement.setString(2, login);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Statement error : message could not be stored : ");
            System.out.println(e);
        }
    }

    /**
     * Update a user's login in the database.
     * @param user the user you want to update the login of
     */
    public void updateLogin(User user) {
        String login = user.getLogin();
        String macAddress = user.getMacAddress();

        try {
            String sqlQuery = "" +
                    "UPDATE users " +
                    "SET login = ? " +
                    "WHERE macAddress = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, macAddress);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Statement error : user could not be updated : ");
            System.out.println(e);
        }
    }

    /**
     * Get the known users from the database.
     * @return the known users
     */
    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<User>();

        try {
            ResultSet resultSet = this.connection.createStatement().executeQuery("" +
                    "SELECT macAddress, login " +
                    "FROM users " +
                    "ORDER BY login"
            );
            while(resultSet.next()) {
                String macAddress = resultSet.getString("macAddress");
                String login = resultSet.getString("login");
                users.add(new User(login, macAddress));
            }
        } catch (SQLException e) {
            System.out.println("Statement error : could not get users : ");
            System.out.println(e);
        } finally {
            return users;
        }
    }

    /**
     * Store a message in the database.
     * @param message the message you want to store
     * @param distantUser the distant sender/receiver
     */
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

    /**
     * Get the messages exchanged with a user from the database.
     * @param user the user you want to get the exchanged messages of
     * @return the messages exchanged with the specified user
     */
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
                boolean currentWay = resultSet.getBoolean("isSent");
                LocalDate currentDate = resultSet.getDate("date").toLocalDate();
                LocalTime currentTime = resultSet.getTime("time").toLocalTime();
                LocalDateTime currentDateTime = LocalDateTime.of(currentDate, currentTime);
                messages.add(new Message(currentContent, currentWay, currentDateTime));
            }
            //System.out.println("Successfully retrieved messages exchanged with " + user + " : ");
        } catch (SQLException e) {
            System.out.println("Statement error : could not get messages exchanged with user " + user + " : ");
            System.out.println(e);
        } finally {
            return messages;
        }
    }

    /**
     * Connect to the database and create one if none exists. It is meant to be called in the constructor.
     */
    private void connectToTheDataBaseAndCreateOneIfNecessary() {
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
                                "   content VARCHAR(500) NOT NULL," +
                                "   distantUserMacAddress CHAR(12) NOT NULL," +
                                "   isSent BOOLEAN NOT NULL," +
                                "   date DATE NOT NULL," +
                                "   time TIME NOT NULL," +
                                "   PRIMARY KEY (distantUserMacAddress, isSent, date, time)" +
                                ")"
                        );
                        this.connection.createStatement().execute( "" +
                                "CREATE TABLE users (" +
                                "   macAddress CHAR(12) NOT NULL," +
                                "   login VARCHAR(50) NOT NULL," +
                                "   PRIMARY KEY (macAddress)" +
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

    /**
     * Disconnect from the database and shut it down.
     */
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
