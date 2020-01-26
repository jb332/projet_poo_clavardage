package network;

public class RequestLoginPacket extends LoginPacket {
    private String requestingUserLogin;
    private String requestingUserMacAddress;

    protected RequestLoginPacket(String login, String macAddress) {
        this.requestingUserLogin = login;
        this.requestingUserMacAddress = macAddress;
    }

    protected String getRequestingUserLogin() {
        return this.requestingUserLogin;
    }

    protected String getRequestingUserMacAddress() {
        return this.requestingUserMacAddress;
    }

    @Override
    protected String getDataToSend() {
        return
            super.getFlag() + ";" +
            this.getRequestingUserLogin() + ";" +
            this.getRequestingUserMacAddress() + ";";
    }
}
