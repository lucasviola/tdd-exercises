
public interface IAccount {

    boolean passwordMatches(String candidate);
    void setLoggedIn(boolean value);
    void setRevoked(boolean value);
    boolean isLoggedIn();
    boolean isRevoked();
    String getId();
}
