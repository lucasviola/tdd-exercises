import exceptions.AccountLoginLimitReachedException;
import exceptions.AccountNotFoundInRepositoryException;
import exceptions.AccountRevokedException;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;


public class LoginServiceTest {

    private IAccount account;
    private IAccountRepository accountRepository;
    private LoginService service;

    @Before
    public void setUp() throws Exception {
        account = mock(IAccount.class);
        accountRepository = mock(IAccountRepository.class);
        service = new LoginService(accountRepository);
        when(accountRepository.find(anyString())).thenReturn(account);
        when(account.getId()).thenReturn("lucas");

    }

    @Test
    public void shouldSetAccountToLoggedInWhenPasswordMatches() throws Exception {
        //arrange
        when(account.passwordMatches(anyString())).thenReturn(true);

        //act
        service.login("lucas", "pass");

        //assert
        verify(account, times(1)).setLoggedIn(true);
    }

    @Test
    public void shouldSetAccountToRevokedAfterThreeFailedAttempts() throws Exception {
        when(account.passwordMatches(anyString())).thenReturn(false);

        for (int i = 0; i < 3; i++)
            service.login("lucas", "pass");

        verify(account, times(1)).setRevoked(true);
    }

    @Test
    public void shouldNotSetAccountLoggedInIfPasswordDoesNotMatch() throws Exception {
       when(account.passwordMatches(anyString())).thenReturn(false);

       service.login("lucas", "pass");

       verify(account, never()).setLoggedIn(true);

    }

    @Test
    public void shouldNotRevokeSecondAccountAfterTwoFailedAttemptsOnFirstAccount() throws Exception {
        IAccount secondAccount = mock(IAccount.class);
        when(secondAccount.passwordMatches(anyString())).thenReturn(false);
        when(accountRepository.find("viola")).thenReturn(secondAccount);

        service.login("lucas", "password");
        service.login("lucas", "password");
        service.login("viola", "password");

        verify(secondAccount, never()).setRevoked(true);

    }

    @Test(expected = AccountLoginLimitReachedException.class)
    public void shouldNowAllowConcurrentLogins() throws Exception {
        when(account.passwordMatches(anyString())).thenReturn(true);
        when(account.isLoggedIn()).thenReturn(true);

        service.login("lucas", "password");
    }

    @Test(expected = AccountNotFoundInRepositoryException.class)
    public void shouldThrowExceptionIfAccountNotFound() throws Exception {
        when(accountRepository.find("viola")).thenReturn(null);

        service.login("viola", "password");
    }

    @Test(expected = AccountRevokedException.class)
    public void shouldNotBePossibleToLogIntoRevokedAccount() throws Exception {
        when(account.passwordMatches(anyString())).thenReturn(true);
        when(account.isRevoked()).thenReturn(true);

        service.login("lucas", "pass");
    }

    @Test
    public void shouldResetBackToInitialStateAfterSuccessfulLogin() throws Exception {
        when(account.passwordMatches(anyString())).thenReturn(false);
        service.login("lucas", "pass");
        service.login("lucas", "pass");
        when(account.passwordMatches(anyString())).thenReturn(true);
        service.login("lucas", "pass");
        when(account.passwordMatches(anyString())).thenReturn(false);
        service.login("lucas", "pass");

        verify(account, never()).setRevoked(true);

    }
}
