public class ServerLogin extends ServerPOST {
    public ServerLogin(String user, String pass, int year, int month, int day) {
        super("auth.php", "login", "", "pennkey", user, "password", pass, "birthday", String.format("%d-%d-%d", year, month, day));
    }
}
