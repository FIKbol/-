package auth;

public class Account {
    private String login;
    private String password;
    private int role; // 1 – admin, 0 – user

    public Account(String login, String password, int role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public int getRole() { return role; }

    public void setPassword(String password) { this.password = password; }
    public void setRole(int role) { this.role = role; }

    @Override
    public String toString() {
        return login + ";" + password + ";" + role;
    }

    public static Account fromString(String line) {
        String[] parts = line.split(";");
        return new Account(parts[0], parts[1], Integer.parseInt(parts[2]));
    }
}
