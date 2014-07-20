import exceptions.AccountLoginLimitReachedException;
import exceptions.AccountRevokedException;

public class AwaitingFirstLoginAttempt extends LoginServiceState {

    @Override
    public void handleIncorrectPassword(LoginService context, IAccount account, String password) {
        context.setState(new AfterFirstFailedLoginAttempt(account.getId()));
    }
}
