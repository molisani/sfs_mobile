public class ServerLogout extends ServerPOST {
    public ServerLogout(String user, String token) {
        super("auth.php");
        addField("logout", "");
        addFIeld("pennkey", user);
        addField("auth_token", token);
    }
}
