package fanrong.cwvwalled.http.model;

import java.io.Serializable;

public class RequestBuyListReq implements Serializable {
    public String pageNum;
    public String pageSize;
    /**
     * 1.买2.卖
     */
    public String type;

}
