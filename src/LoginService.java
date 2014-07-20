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

        verifyLoginAttempt(account, password);
    }

    private void verifyLoginAttempt(IAccount account, String password) {
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
