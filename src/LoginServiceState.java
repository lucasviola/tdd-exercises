import exceptions.AccountLoginLimitReachedException;
import exceptions.AccountNotFoundInRepositoryException;
import exceptions.AccountRevokedException;

public abstract class LoginServiceState {

    protected String previousAccountId = "";
    protected int failedAttempts;

    public abstract void login(IAccount account, String password);


}
