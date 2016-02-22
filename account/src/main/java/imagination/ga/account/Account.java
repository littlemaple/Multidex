package imagination.ga.account;

/**
 * Created by 44260 on 2016/2/2.
 */
public class Account {
    private int id;
    private String accountName;

    public int getId() {
        return id;
    }

    public Account setId(int id) {
        this.id = id;
        return this;
    }

    public String getAccountName() {
        return accountName;
    }

    public Account setAccountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    @Override
    public String toString() {
        return "[ id:" + id + " , accountName:" + accountName + " ]";
    }
}
