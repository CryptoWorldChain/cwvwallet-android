package fanrong.cwvwalled.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity()
public class GreWallet {

    @Id
    private Long id;

    private String address;

    private String pubKey;

    private String privateKey;

    private String walletName;

    @Generated(hash = 1888347029)
    public GreWallet(Long id, String address, String pubKey, String privateKey,
            String walletName) {
        this.id = id;
        this.address = address;
        this.pubKey = pubKey;
        this.privateKey = privateKey;
        this.walletName = walletName;
    }

    @Generated(hash = 342575845)
    public GreWallet() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPubKey() {
        return this.pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getWalletName() {
        return this.walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }
}
