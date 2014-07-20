import exceptions.AccountLoginLimitReachedException;
import exceptions.AccountNotFoundInRepositoryException;
import exceptions.AccountRevokedException;

public abstract class LoginServiceState {

    protected String previousAccountId = "";
    protected int failedAttempts;

    public final void login(LoginService context, IAccount account, String password){
        if (account.passwordMatches(password)){

            if(account.isLoggedIn())
                throw new AccountLoginLimitReachedException();

            if(account.isRevoked())
                throw new AccountRevokedException();

            account.setLoggedIn(true);
            context.setState(new AwaitingFirstLoginAttempt());
        }
        else {
            handleIncorrectPassword(context, account, password);
        }
    }

    public abstract void handleIncorrectPassword(LoginService context, IAccount account, String password);


}
