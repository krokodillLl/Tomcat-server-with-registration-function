package accounts;

import java.util.HashMap;
import java.util.Map;

public class AccountService {
    private final Map<String, UserProfile> loginToProfile;

    private static AccountService accountService;
    public static AccountService getInstance() {
        if(accountService == null)
            accountService = new AccountService();
        return accountService;
    }

    private AccountService() {
        loginToProfile = new HashMap<>();
    }

    public void addNewUser(UserProfile userProfile) {
        loginToProfile.put(userProfile.getLogin(), userProfile);
    }
    public UserProfile getUserByLogin(String login) {
        return loginToProfile.get(login);
    }

    public String getSessionId() {
        return loginToProfile.size() + "";
    }

}
