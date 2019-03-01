package fanrong.cwvwalled.http.model;

import java.io.Serializable;

/**
 * 查询当前节点 gas
 */
public class QueryNodeGasReq implements Serializable {
    public String dapp_id;
    public String node_url;
}
