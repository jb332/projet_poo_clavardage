package network;

/**
 * Connection / change login response packet. (It is created to respond to a change login request only to deny a login change. When the login change is accepted, no packet if sent over the network.)
 */
public class ResponseLoginPacket extends LoginPacket {
    /**
     * Passed as the constructor first argument to deny a connection or a login change.
     */
    protected static final boolean DENIED = false;
    /**
     * Passed as the constructor first argument to grant a connection.
     */
    protected static final boolean GRANTED = true;

    /**
     * The responding user login. When it is null, it means the request was denied.
     */
    private String respondingUserLogin;
    /**
     * The responding user MAC address. When it is null, it means the request was denied.
     */
    private String respondingUserMacAddress;

    /**
     * Constructor to create response packets that deny the request.
     * @param isLoginGranted tell if the request is granted, must be set to false for this constructor
     */
    protected ResponseLoginPacket(boolean isLoginGranted) {
        if(isLoginGranted) {
            System.out.println("Bad constructor used : if login is granted then you should provide a login and say if this packet is connection request or a login change request. If this packet is a connection request you must also provide a login");
            System.exit(1);
        } else {
            this.respondingUserLogin = null;
            this.respondingUserMacAddress = null;
        }
    }

    /**
     * Constructor.
     * @param isLoginGranted tell if the request is granted
     * @param respondingUserLogin the responding user login
     * @param respondingUserMacAddress the responding user MAC address
     */
    protected ResponseLoginPacket(boolean isLoginGranted, String respondingUserLogin, String respondingUserMacAddress) {
        if(isLoginGranted) {
            this.respondingUserLogin = respondingUserLogin;
            this.respondingUserMacAddress = respondingUserMacAddress;
        } else {
            this.respondingUserLogin = null;
            this.respondingUserMacAddress = null;
        }
    }

    /**
     * Tell if the request is granted.
     * @return true if the request is granted, false if it is denied
     */
    protected boolean isLoginGranted() {
        return this.respondingUserLogin != null && this.respondingUserMacAddress != null;
    }

    /**
     * Get the responding user login.
     * @return the responding user login
     */
    protected String getRespondingUserLogin() {
        return this.respondingUserLogin;
    }

    /**
     * Get the responding user MAC address.
     * @return the resonding user MAC address
     */
    protected String getRespondingUserMacAddress() {
        return this.respondingUserMacAddress;
    }

    /**
     * Serialization method converting this packet to a string to be sent over the network. It implements the abstract method from the mother class.
     * @return the serialized packet
     */
    @Override
    protected String getDataToSend() {
        return
            super.getFlag() + ";" +
            (
                this.isLoginGranted() ?
                (
                    this.getRespondingUserLogin() + ";" +
                    this.getRespondingUserMacAddress()
                ) :
                "no"
            ) + ";";
    }
}
