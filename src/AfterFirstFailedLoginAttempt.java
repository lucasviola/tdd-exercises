import exceptions.AccountLoginLimitReachedException;
import exceptions.AccountRevokedException;

public class AfterFirstFailedLoginAttempt extends LoginServiceState {
    private String previousAccountId;

    public AfterFirstFailedLoginAttempt(String previousAccountId) {
        this.previousAccountId = previousAccountId;
        failedAttempts = 1;
    }

    @Override
    public void handleIncorrectPassword(LoginService context, IAccount account, String password) {
        if(previousAccountId.equals(account.getId()))
            context.setState(new AfterSecondFailedLoginAttempt(account.getId()));
        else
            previousAccountId = account.getId();
    }
}
