package edu.upenn.cis350.shs_mobile;

public class ServerLogout extends ServerPOST {
    public ServerLogout(String user, String token) {
        super("auth.php", "logout", "", "pennkey", user, "auth_token", token);
    }
}
