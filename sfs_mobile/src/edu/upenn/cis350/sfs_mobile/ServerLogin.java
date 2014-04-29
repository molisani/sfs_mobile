public class ServerLogin extends ServerPOST {
    public ServerLogin(String user, String pass, int year, int month, int day) {
        super("auth.php");
        addField("login", "");
        addField("pennkey", user);
        addField("password", pass);
        addField("birthday", String.format("%d-%d-%d", year, month, day));
    }
}
