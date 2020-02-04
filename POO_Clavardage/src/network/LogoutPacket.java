package network;

/**
 * Disconnection request packet.
 */
public class LogoutPacket extends LoginPacket {
    /**
     * The MAC address of the requesting user.
     */
    private String requestingUserMacAddress;

    /**
     * Constructor.
     * @param macAddress the requesting user MAC address
     */
    protected LogoutPacket(String macAddress) {
        this.requestingUserMacAddress = macAddress;
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
            this.getRequestingUserMacAddress() + ";";
    }
}
