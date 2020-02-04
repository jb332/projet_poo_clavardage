package network;

/**
 * Connection / login change request packet.
 */
public class RequestLoginPacket extends LoginPacket {
    /**
     * The requesting user login.
     */
    private String requestingUserLogin;
    /**
     * The requesting user MAC address.
     */
    private String requestingUserMacAddress;

    /**
     * Constructor.
     * @param login the login picked by the requesting user
     * @param macAddress the requesting user MAC address
     */
    protected RequestLoginPacket(String login, String macAddress) {
        this.requestingUserLogin = login;
        this.requestingUserMacAddress = macAddress;
    }

    /**
     * Get the login the requesting user picked.
     * @return the login the requesting user picked
     */
    protected String getRequestingUserLogin() {
        return this.requestingUserLogin;
    }

    /**
     * Get the requesting user MAC address.
     * @return the requesting user MAC address
     */
    protected String getRequestingUserMacAddress() {
        return this.requestingUserMacAddress;
    }

    /**
     * Serialization method converting this packet to a string to be sent over the network. It implements the abstract method from the mother class.
     * @return the serialized packet
     */
    @Override
    protected String getDataToSend() {
        return
            super.getFlag() + ";" +
            this.getRequestingUserLogin() + ";" +
            this.getRequestingUserMacAddress() + ";";
    }
}
