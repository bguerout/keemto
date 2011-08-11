package fr.keemto.web.security;


import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class LoginStatus {

    private final boolean loggedIn;
    private final String username;

    @JsonCreator
    public LoginStatus(@JsonProperty("loggedIn") boolean loggedIn, @JsonProperty("username") String username) {
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