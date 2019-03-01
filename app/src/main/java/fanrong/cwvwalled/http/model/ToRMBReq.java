package fanrong.cwvwalled.http.model;

import java.io.Serializable;

/**
 * 获取余额
 */
public class ToRMBReq implements Serializable {
    public String dapp_id;
    public String coin;
    public String period;
    public String size;
}
