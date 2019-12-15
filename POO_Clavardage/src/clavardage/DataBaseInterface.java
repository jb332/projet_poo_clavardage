package clavardage;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

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
        //this.storeMessage(new Message("salut !", MessageWay.SENT), new User("perlotte"));
    }

    public void storeMessage(Message message, User sender) {
        try {
            this.connection.createStatement().execute("" +
                    "INSERT INTO messages VALUES ('Salut !', 'f8:28:19:73:f2:1f', 'f8:28:19:73:f2:1f', '2019-12-15', '3:29:58')"
            );
        } catch(SQLException e) {
            System.out.println("Statement error : ");
            System.out.println(e);
        }
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
}
