package clavardage;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;

public class DataBaseInterface {

    private Clavardage chat;
    private Connection connection;

    private final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private final String dataBaseName = "clavarbase";


    public DataBaseInterface(Clavardage chat) {
        this.chat = chat;

        //meant to be used only once
        //createDataBase();

        String connectionURL = "jdbc:derby:" + this.dataBaseName;
        try {
            //database driver initialization
            Class.forName(this.driver).newInstance();
            System.out.println(this.driver + " loaded.");

            try {
                //connection to the local database
                System.out.println("Trying to connect to " + connectionURL);
                this.connection = DriverManager.getConnection(connectionURL);
                System.out.println("Connected to database " + connectionURL);
            } catch (SQLException e) {
                System.out.println("Database connection problem : ");
                System.out.println(e);
                System.exit(1);
            }
        } catch(Exception e) {
            System.out.println("Driver initialization problem : ");
            System.out.println(e);
            System.exit(1);
        }

        //test
        this.storeMessage(new Message("salut !", MessageWay.SENT), new User("perlotte", "255.255.255.255", "ff:ff:ff:ff:ff:ff"));
    }

    public void storeMessage(Message message, User distantUser) {
        String content = "'"+message.getContent()+"'";
        String sender = null;
        String receiver = null;
        if(message.isSent()) {
            sender = "'f8:28:19:73:f2:1f'";
            receiver = "'"+distantUser.getMacAddress()+"'";
        } else {
            sender = "'"+distantUser.getMacAddress()+"'";
            receiver = "'f8:28:19:73:f2:1f'";
        }
        String date = "'"+message.getDateTime().getYear()+"-"+message.getDateTime().getMonthValue()+"-"+message.getDateTime().getDayOfMonth()+"'";
        String time = "'"+message.getDateTime().getHour()+":"+message.getDateTime().getMinute()+":"+message.getDateTime().getSecond()+"'";

        try {
            String sqlRequest = "INSERT INTO messages VALUES ("+content+", "+sender+", "+receiver+", "+date+", "+time+")";
            //System.out.println(sqlRequest);
            this.connection.createStatement().execute(sqlRequest);
            System.out.println("Message stored correctly");
        } catch (SQLException e) {
            System.out.println("Statement error : message could not be stored : ");
            System.out.println(e);
        }
    }

    public ArrayList<Message> getMessages(User user) {
        return null;
    }

    /*
    public void getUsers(ArrayList<User> connectedUsers) {
    }
    */

    public void createDataBase() {
        String connectionURL = "jdbc:derby:" + this.dataBaseName + ";create=true";
        Connection connection = null;

        try {
            //database driver initialization
            Class.forName(this.driver);
            System.out.println(this.driver + " loaded.");

            try {
                //connection to the local database
                System.out.println("Trying to connect to " + connectionURL);
                connection = DriverManager.getConnection(connectionURL);
                System.out.println("Connected to database " + connectionURL);

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

                    connection.createStatement().execute("" +
                            "CREATE TABLE messages (" +
                            "   content VARCHAR(500)," +
                            "   sender VARCHAR(17)," +
                            "   receiver VARCHAR(17)," +
                            "   date DATE," +
                            "   time TIME" +
                            ")"
                    );
                } catch(SQLException e) {
                    System.out.println("Statement error : ");
                    System.out.println(e);
                }

                //disconnection from the database
                connection.close();
                System.out.println("Disconnected from the database");

                //shuting down the database
                boolean gotSQLExc = false;
                try {
                    DriverManager.getConnection("jdbc:derby:;shutdown=true");
                } catch (SQLException se) {
                    if ( se.getSQLState().equals("XJ015") ) {
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

            } catch (SQLException e) {
                System.out.println("Database connection problem : ");
                System.out.println(e);
            }


        } catch(ClassNotFoundException e) {
            System.out.println("Driver class not found : ");
            System.out.println(e);
        }




    }

    public void storeUser(User user) {
    }
}
