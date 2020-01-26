package network;

public class ResponseLoginPacket extends LoginPacket {
    private String respondingUserLogin;
    private String respondingUserMacAddress;

    protected ResponseLoginPacket(boolean isLoginGranted) {
        if(isLoginGranted) {
            System.out.println("Bad constructor used : if login is granted, a login is required");
            System.exit(1);
        } else {
            this.respondingUserLogin = null;
            this.respondingUserMacAddress = null;
        }
    }

    protected ResponseLoginPacket(boolean isLoginGranted, String respondingUserLogin, String respondingUserMacAddress) {
        if(isLoginGranted) {
            this.respondingUserLogin = respondingUserLogin;
            this.respondingUserMacAddress = respondingUserMacAddress;
        } else {
            this.respondingUserLogin = null;
            this.respondingUserMacAddress = null;
        }
    }

    protected boolean isLoginGranted() {
        return this.respondingUserLogin != null && this.respondingUserMacAddress != null;
    }

    protected String getRespondingUserLogin() {
        return this.respondingUserLogin;
    }

    protected String getRespondingUserMacAddress() {
        return this.respondingUserMacAddress;
    }

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
