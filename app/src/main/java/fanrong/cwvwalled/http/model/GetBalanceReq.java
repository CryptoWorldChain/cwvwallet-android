package fanrong.cwvwalled.http.model;

import java.io.Serializable;

/**
 * 获取余额
 */
public class GetBalanceReq implements Serializable {
    public String dapp_id;
    public String node_url;
    public String address;
    public String contract_addr;
}
