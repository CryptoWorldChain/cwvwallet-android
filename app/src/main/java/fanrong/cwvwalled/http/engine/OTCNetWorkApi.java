package fanrong.cwvwalled.http.engine;

import net.sourceforge.http.model.AddPayModeResp;
import net.sourceforge.http.model.AuthRealNameResp;
import net.sourceforge.http.model.BindPhoneResp;
import net.sourceforge.http.model.CheckRealNameResp;
import net.sourceforge.http.model.ConfirmPayResp;
import net.sourceforge.http.model.ConfirmPaySPDTResp;
import net.sourceforge.http.model.OderListResp;
import net.sourceforge.http.model.OrderAppealResp;
import net.sourceforge.http.model.PayModeResp;
import net.sourceforge.http.model.QueryOrderResp;
import net.sourceforge.http.model.ReqestUserInfoResp;
import net.sourceforge.http.model.SendMsgResp;
import net.sourceforge.http.model.TransantionResp;

import fanrong.cwvwalled.http.model.BussnessModel;
import fanrong.cwvwalled.http.model.BussnessSellModel;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OTCNetWorkApi {

    @POST("http://39.96.70.16:7085/otc/mer/pbmai.do")
    Call<BussnessModel> requestSellList(@Body RequestBody map);

    /**
     * 二十、广告列表（卖） 买入功能调用 卖的广告列表
     * @param map
     * @return
     */
    @POST("http://39.96.70.16:7085/otc/mer/pbadv.do")
    Call<BussnessSellModel> requestBuyList(@Body RequestBody map);

    /**
     * 发起交易
     * @param map
     * @return
     */
    @POST("http://39.96.70.16:7085/otc/ord/pbpos.do")
    Call<TransantionResp> otcTransaction(@Body RequestBody map);

    @POST("http://39.96.70.16:7085/otc/ord/pbuos.do")
    Call<OderListResp> requestOderList(@Body RequestBody map);

    /**
     * 获取验证码
     * @param map
     * @return
     */
    @POST("http://39.96.70.16:7085/otc/sys/pbaut.do")
    Call<SendMsgResp> sendMsg(@Body RequestBody map);

    /**
     * 绑定手机号
     * @param map
     * @return
     */
    @POST("http://39.96.70.16:7085/otc/usr/pbrus.do")
    Call<BindPhoneResp> bindPhone(@Body RequestBody map);

    /**
     * 实名认证状态
     * @param map
     * @return
     */
    @POST("http://39.96.70.16:7085/otc/usr/pbshi.do")
    Call<CheckRealNameResp> checkRealName(@Body RequestBody map);
    /**
     * 用户信息查询
     * @param map
     * @return
     */
    @POST("http://39.96.70.16:7085/otc/usr/pbpis.do")
    Call<ReqestUserInfoResp> requestUserInfo(@Body RequestBody map);
    /**
     * 实名认证
     * @param map
     * @return
     */
    @POST("http://39.96.70.16:7085/otc/usr/pbvcs.do")
    Call<AuthRealNameResp> authRealName(@Body RequestBody map);

    /**
     * otc 支付方式列表
     * @param map
     * @return
     */
    @POST("http://39.96.70.16:7085/otc/mer/pbpws.do")
    Call<PayModeResp> payModeList(@Body RequestBody map);
    /**
     * otc 添加支付方式
     * @param map
     * @return
     */
    @POST("http://39.96.70.16:7085/otc/mer/pbapw.do")
    Call<AddPayModeResp> addPayMode(@Body RequestBody map);
    /**
     * otc 修改支付方式
     * @param map
     * @return
     */
    @POST("http://39.96.70.16:7085/otc/mer/pbupw.do")
    Call<AddPayModeResp> updatePayMode(@Body RequestBody map);
    /**
     * otc 删除支付方式
     * @param map
     * @return
     */
    @POST("http://39.96.70.16:7085/otc/mer/pbdpw.do")
    Call<AddPayModeResp> deletePayMode(@Body RequestBody map);
    /**
     * otc 查询
     * @param map
     * @return
     */
    @POST("http://39.96.70.16:7085/otc/ord/pbquo.do")
    Call<QueryOrderResp> queryOrder(@Body RequestBody map);
    /**
     * otc 确认支付
     * @param map
     * @return
     */
    @POST("http://39.96.70.16:7085/otc/ord/pbcfs.do")
    Call<ConfirmPayResp> confirmPay(@Body RequestBody map);
    /**
     * otc 订单申诉
     * @param map
     * @return
     */
    @POST("http://39.96.70.16:7085/otc/ord/pbaos.do")
    Call<OrderAppealResp> orderAppeal(@Body RequestBody map);
    /**
     * otc 确认转账
     * @param map
     * @return
     */
    @POST("http://39.96.70.16:7085/otc/ord/pbtra.do")
    Call<ConfirmPaySPDTResp> confirmPaySPDT(@Body RequestBody map);

}
