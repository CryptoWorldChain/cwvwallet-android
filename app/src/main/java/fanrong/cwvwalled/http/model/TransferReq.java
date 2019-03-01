package fanrong.cwvwalled.http.model;

import java.io.Serializable;

public class TransferReq implements Serializable {

    public String dapp_id;
    public String node_url;
    public String nonce;
    public String from_addr;
    public String to_addr;
    public String value;
    public String signed_message;
    public String gas_price;
    public String gas_limit;
    public String timestamp;
    public String ex_data;
    public String symbol;
    public String contract_addr;
    public String tx_type;
}
