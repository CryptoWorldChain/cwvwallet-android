package fanrong.cwvwalled.http.engine;

import net.sourceforge.http.model.QueryCoinTypeResp;
import net.sourceforge.http.model.spdt.HomeAdvertResp;
import net.sourceforge.http.model.spdt.QueryOtherServiceResp;
import net.sourceforge.http.model.spdt.TransactionRecordResp;

import fanrong.cwvwalled.http.model.NodeListResp;
import fanrong.cwvwalled.http.model.WalletBalanceModel;
import fanrong.cwvwalled.http.model.WalletNonceModel;
import fanrong.cwvwalled.http.model.WalletTransferModel;
import fanrong.cwvwalled.http.model.response.QueryNodeGasResp;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FBCNetWorkApi {


    String SPDT_ROOT = "https://dev.wallet.icwv.co/";


    @POST(SPDT_ROOT + "fbc/pbqnl.do")
    Call<NodeListResp> requestNodeList(@Body RequestBody map);

    @POST(SPDT_ROOT + "fbc/pbqbe.do")
    Call<WalletBalanceModel> fbcGetBalance(@Body RequestBody map);

    @POST(SPDT_ROOT + "fbc/pbqne.do")
    Call<WalletNonceModel> fbcWalletNonce(@Body RequestBody map);

    /**
     * 查询节点当前GAS
     *
     * @param map
     * @return
     */
    @POST(SPDT_ROOT + "fbc/pbqgs.do")
    Call<QueryNodeGasResp> fbcQueryNodeGas(@Body RequestBody map);

    /**
     * 代币-转账
     *
     * @param map
     * @return
     */
    @POST(SPDT_ROOT + "fbc/pbtxt.do")
    Call<WalletTransferModel> fbcTransfer(@Body RequestBody map);

    /**
     * 查询第三方服务
     */
    @POST(SPDT_ROOT + "spdt/pbapl.do")
    Call<QueryOtherServiceResp> queryOtherService(@Body RequestBody map);

    /**
     * 查询首页广告图
     */
    @POST(SPDT_ROOT + "spdt/pbbal.do")
    Call<HomeAdvertResp> queryHomeAdvert(@Body RequestBody map);

    /**
     * 查询交易记录
     */
    @POST(SPDT_ROOT + "eth/pbqta.do")
    Call<TransactionRecordResp> queryTransactionRecord(@Body RequestBody map);
    /**
     * 查询交易记录
     */
    @POST(SPDT_ROOT + "fbc/pbqti.do")
    Call<QueryCoinTypeResp> queryCoinType(@Body RequestBody map);
}
