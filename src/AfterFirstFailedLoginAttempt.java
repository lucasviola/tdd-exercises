import exceptions.AccountLoginLimitReachedException;
import exceptions.AccountRevokedException;

public class AfterFirstFailedLoginAttempt extends LoginServiceState {
    private String previousAccountId;

    public AfterFirstFailedLoginAttempt(String previousAccountId) {
        this.previousAccountId = previousAccountId;
        failedAttempts = 1;
    }

    @Override
    public void login(LoginService context, IAccount account, String password) {

        if (account.passwordMatches(password)){

            if(account.isLoggedIn())
                throw new AccountLoginLimitReachedException();

            if(account.isRevoked())
                throw new AccountRevokedException();

            account.setLoggedIn(true);
        }
        else {

            if(previousAccountId.equals(account.getId()))
                ++failedAttempts;
            else{
                failedAttempts = 1;
                previousAccountId = account.getId();
            }
        }

        if (failedAttempts == 3)
            account.setRevoked(true);

    }
}
