package fanrong.cwvwalled.http.model.response;

import java.io.Serializable;

/**
 * 查询当前节点 gas 结果
 */
public class QueryNodeGasResp implements Serializable {
    public String gas_price;
    public String err_code;
    public String msg;
}
