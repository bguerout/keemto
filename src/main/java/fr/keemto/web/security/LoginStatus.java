package fr.keemto.web.security;

public class LoginStatus {

    private final boolean loggedIn;
    private final String username;

    public LoginStatus(boolean loggedIn, String username) {
        this.loggedIn = loggedIn;
        this.username = username;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getUsername() {
        return username;
    }
}