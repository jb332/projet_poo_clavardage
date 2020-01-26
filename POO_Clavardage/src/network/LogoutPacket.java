package network;

public class LogoutPacket extends LoginPacket {
    private String requestingUserMacAddress;

    protected LogoutPacket(String macAddress) {
        this.requestingUserMacAddress = macAddress;
    }

    protected String getRequestingUserMacAddress() {
        return this.requestingUserMacAddress;
    }

    @Override
    protected String getDataToSend() {
        return
            super.getFlag() + ";" +
            this.getRequestingUserMacAddress() + ";";
    }
}
