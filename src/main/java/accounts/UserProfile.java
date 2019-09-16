package accounts;

import java.util.Random;

public class UserProfile {
    private final String login;
    private final String password;
    private final String email;
    private boolean isCertified;
    private String key;
    private String echo;

    public UserProfile(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        key = email.substring(0, 2) + new Random(email.length()).nextInt();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setCertified(boolean isCertified) {
        this.isCertified = isCertified;
    }

    public boolean isCertified() {
        return isCertified;
    }

    public String getKey() {
        return this.key;
    }

    public String getEcho() {
        return echo;
    }

    public void setEcho(String echo) {
        this.echo = echo;
    }
}
