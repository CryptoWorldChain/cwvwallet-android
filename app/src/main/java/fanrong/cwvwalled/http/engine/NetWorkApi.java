package fanrong.cwvwalled.http.engine;


import fanrong.cwvwalled.http.model.MarketInfoResp;
import fanrong.cwvwalled.http.model.UpdateResp;
import fanrong.cwvwalled.http.model.response.ToRMBResp;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NetWorkApi {

    @POST("http://47.244.102.197:12000/tx/mkt/pbqks.do")
    Call<ToRMBResp> toRMB(@Body RequestBody map);


    /**
     * 获取 行情列表
     *
     * @param map
     * @return
     */
    @POST("http://47.244.102.197:12000/tx/mkt/pbqms.do")
    Call<MarketInfoResp> marketInfo(@Body RequestBody map);


    /**
     * 检查更新
     *
     * @return
     */
    @GET("http://cwv-wallet.oss-cn-hongkong.aliyuncs.com/cwv_wallet_h5/cwv_apk_version.json")
    Call<UpdateResp> update();




}
