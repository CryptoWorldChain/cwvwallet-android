package fanrong.cwvwalled.http.engine;

import net.sourceforge.http.model.QueryCoinTypeResp;
import net.sourceforge.http.model.spdt.TransactionRecordResp;

import fanrong.cwvwalled.http.model.NodeListResp;
import fanrong.cwvwalled.http.model.NodeModel;
import fanrong.cwvwalled.http.model.WalletBalanceModel;
import fanrong.cwvwalled.http.model.WalletNonceModel;
import fanrong.cwvwalled.http.model.WalletTransferModel;
import fanrong.cwvwalled.http.model.response.QueryNodeGasResp;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ETHNetWorkApi {

    String ETH_ROOT = FBCNetWorkApi.SPDT_ROOT;


    @POST(ETH_ROOT + "eth/pbqnl.do")
    Call<NodeListResp> requestNodeList(@Body RequestBody map);


    @POST(ETH_ROOT + "eth/pbqbe.do")
    Call<WalletBalanceModel> ethGetBalance(@Body RequestBody map);


    /**
     * 查询币种信息
     *
     * @param map
     * @return
     */
    @POST(ETH_ROOT + "eth/pbqti.do")
    Call<QueryCoinTypeResp> queryCoinType(@Body RequestBody map);

    /**
     * 查询交易记录
     *
     * @param map
     * @return
     */
    @POST(ETH_ROOT + "eth/pbqta.do")
    Call<TransactionRecordResp> queryTransactionRecord(@Body RequestBody map);


    @POST(ETH_ROOT + "eth/pbqne.do")
    Call<WalletNonceModel> walletNonce(@Body RequestBody map);

    /**
     * 查询节点当前GAS
     *
     * @param map
     * @return
     */
    @POST(ETH_ROOT + "eth/pbqgs.do")
    Call<QueryNodeGasResp> queryNodeGas(@Body RequestBody map);

    /**
     * 代币-转账
     *
     * @param map
     * @return
     */
    @POST(ETH_ROOT + "eth/pbtxt.do")
    Call<WalletTransferModel> transfer(@Body RequestBody map);

    /**
     * 主币-转账
     *
     * @param map
     * @return
     */
    @POST(ETH_ROOT + "eth/pbtxe.do")
    Call<WalletTransferModel> mainTransfer(@Body RequestBody map);

}
