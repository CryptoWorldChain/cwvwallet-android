package fanrong.cwvwalled.http.model;

import java.io.Serializable;

public class WalletModel implements Serializable{

    public String walletId;

    public String walletPassowrd;

    public String address;

    public String pubKey;

    public String privateKey;

    public String bcuId;

    public String mnemonicStr;

    public String keystoreJson;

    /**
     * 从哪里导入的钱包
     * 0、新创建
     * 1、助记词导入
     * 2、keystore导入
     * 3、私钥导入
     */
    public int importFrom = 0;

    public float balance;

    public float balance_cny;

    public String payPassword;

    //0未认证
    public int auth;

    public String walletName;


    public enum WalletType {
        FBC, ETH
    }

}
