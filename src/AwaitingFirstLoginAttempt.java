import exceptions.AccountLoginLimitReachedException;
import exceptions.AccountRevokedException;

public class AwaitingFirstLoginAttempt extends LoginServiceState {

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
                context.setState(new AfterFirstFailedLoginAttempt(account.getId()));
        }

        if (failedAttempts == 3)
            account.setRevoked(true);

    }
}
