package imagination.ga.account;

/**
 * Created by 44260 on 2016/2/2.
 */
public class AccountController {
    private static AccountController controller;
    private static Account account;

    private AccountController() {
    }

    public static AccountController getInstance() {
        if (controller == null)
            controller = new AccountController();
        return controller;
    }

    public Account getCurrentAccount() {
        if (account == null) {
            account = new Account();
        }
        return account;
    }
}
