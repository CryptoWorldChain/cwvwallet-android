package fanrong.cwvwalled.http.model;

import java.io.Serializable;

public class WalletNonceReq extends BaseResponse implements Serializable {
    public String dapp_id;
    public String node_url;
    public String address;
}
