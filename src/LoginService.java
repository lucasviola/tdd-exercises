import exceptions.AccountLoginLimitReachedException;
import exceptions.AccountNotFoundInRepositoryException;
import exceptions.AccountRevokedException;

public class LoginService {

    private final IAccountRepository accountRepository;
    private LoginServiceState state = new AwaitingFirstLoginAttempt();

    public LoginService(IAccountRepository accountRepository) {

        this.accountRepository = accountRepository;
    }

    public void login(String accountId, String password) {
        IAccount account = accountRepository.find(accountId);

        if(account == null)
            throw new AccountNotFoundInRepositoryException();

        state.login(this, account, password);
    }


    public void setState(LoginServiceState state) {
        this.state = state;
    }
}
