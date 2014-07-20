import exceptions.AccountLoginLimitReachedException;
import exceptions.AccountRevokedException;

public class AfterSecondFailedLoginAttempt extends LoginServiceState {
    private String previousAccountId;

    public AfterSecondFailedLoginAttempt(String previousAccountId) {

        this.previousAccountId = previousAccountId;
        failedAttempts = 2;
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
            if(previousAccountId.equals(account.getId())) {
                account.setRevoked(true);
                context.setState(new AwaitingFirstLoginAttempt());
            }
            else{
                context.setState(new AfterFirstFailedLoginAttempt(account.getId()));
            }
        }
    }
}
