import exceptions.AccountLoginLimitReachedException;
import exceptions.AccountNotFoundInRepositoryException;
import exceptions.AccountRevokedException;

public class LoginService {

    private final IAccountRepository accountRepository;
    private int failedAttempts = 0;
    private String previousAccountId = "";

    public LoginService(IAccountRepository accountRepository) {

        this.accountRepository = accountRepository;
    }

    public void login(String accountId, String password) {
        IAccount account = accountRepository.find(accountId);

        if(account == null)
            throw new AccountNotFoundInRepositoryException();

        if (account.passwordMatches(password)){

            if(account.isLoggedIn())
                throw new AccountLoginLimitReachedException();

            if(account.isRevoked())
                throw new AccountRevokedException();

            account.setLoggedIn(true);
        }
        else {

            if(previousAccountId.equals(accountId))
                ++failedAttempts;
            else{
                failedAttempts = 1;
                previousAccountId = accountId;
            }
        }

        if (failedAttempts == 3)
            account.setRevoked(true);
    }
}
