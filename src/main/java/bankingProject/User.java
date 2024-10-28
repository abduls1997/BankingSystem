package bankingProject;

public class User {
    private String username;
    private String password;
    private boolean isLoggedIn;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isLoggedIn = false;
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public void login() {
        this.isLoggedIn = true;
    }

    public void logout() {
        this.isLoggedIn = false;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String toCSV() {
        return String.format("%s,%s", username, password);
    }

    public static User fromCSV(String line) {
        String[] parts = line.split(",");
        return new User(parts[0], parts[1]);
    }
}