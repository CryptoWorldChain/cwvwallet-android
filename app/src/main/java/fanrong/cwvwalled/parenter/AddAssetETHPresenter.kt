package fanrong.cwvwalled.parenter

import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.litepal.GreNodeOperator
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.litepal.LiteCoinBeanOperator
import net.sourceforge.http.model.CoinBean
import net.sourceforge.http.model.QueryCoinTypeReq
import net.sourceforge.http.model.QueryCoinTypeResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAssetETHPresenter : AddAssetPresenter() {


    override fun requestAsset(inputStr: String, valueCallBack: ValueCallBack<List<CoinBean>>) {
        val using = GreNodeOperator.queryETHnode()
        val req = QueryCoinTypeReq(using.node_url)
        req.coin_name = inputStr

        RetrofitClient.getETHNetWorkApi()
                .queryCoinType(ConvertToBody.ConvertToBody(req))
                .enqueue(object : Callback<QueryCoinTypeResp> {
                    override fun onFailure(call: Call<QueryCoinTypeResp>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<QueryCoinTypeResp>, response: Response<QueryCoinTypeResp>) {
                        if ("1".equals(response.body()?.err_code)) {
                            val tokens = response.body()!!.token!!

                            for (token in tokens) {
                                if (!"ETH".equals(token.coin_symbol)) {
                                    token.coin_symbol = token.coin_symbol + "(e)"
                                }
                                for (allETH in hasCoins) {
                                    if (allETH.coin_name.equals(token.coin_name)) {
                                        token.isOpen = true
                                    }
                                }
                            }

                            valueCallBack.valueBack(tokens)
                        }
                    }

                })

    }

    override fun changeAssetStatus(coinBean: CoinBean, isOpen: Boolean) {
        if (hasCoins == null) {
            hasCoins = mutableListOf()
        }

        if (isOpen) {
            LiteCoinBeanOperator.copyCoinBean(coinBean).save()
        } else {
            for (allETH in hasCoins) {
                if (allETH.coin_name.equals(coinBean.coin_name)) {
                    allETH.delete()
                }
            }
        }
    }
}