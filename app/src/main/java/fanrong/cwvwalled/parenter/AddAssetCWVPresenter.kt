package fanrong.cwvwalled.parenter

import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.litepal.GreNodeOperator
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.litepal.LiteCoinBeanOperator
import fanrong.cwvwalled.litepal.TokenInfo
import net.sourceforge.http.model.CWVCoinType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAssetCWVPresenter : AddAssetPresenter() {


    override fun requestAsset(inputStr: String, valueCallBack: ValueCallBack<List<TokenInfo>>) {

        val using = GreNodeOperator.queryCWVnode()
        // val req = QueryCoinTypeReq(using.node_url)
        val req = CWVCoinType.CVWType("0");
        req.limit = "100"
        //   req.coin_name = inputStr

        RetrofitClient.getFBCNetWorkApi()
                .CWVCoinType(ConvertToBody.ConvertToBody(req))
                .enqueue(object : Callback<CWVCoinType> {
                    override fun onFailure(call: Call<CWVCoinType>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<CWVCoinType>, response: Response<CWVCoinType>) {
                        if ("1".equals(response.body()?.err_code)) {
                            var tokenInfo = response.body()?.tokenInfo



                            if (tokenInfo != null) {
                                var allCWV = LiteCoinBeanOperator.findAllCWVs()

                                for (token in tokenInfo) {
                                    token.tokenType = "CWV"
                                    for (lifedatabean in allCWV) {
                                        if (token.tokenAddress.equals(lifedatabean.contract_addr)) {
                                            token.isOpen = true;
                                         //   token.save()
                                        }
                                    }
                                }


                                valueCallBack.valueBack(tokenInfo)
                            }

                        }
                    }
                })

    }

    override fun changeAssetStatus(coinBean:TokenInfo, isOpen: Boolean) {

        if (hasCoins == null) {
            hasCoins = mutableListOf()
        }

        if (isOpen) {
            //TODO()存储当前的对象 coinBean
            LiteCoinBeanOperator.copyTokenInfor(coinBean).save()
          //  coinBean.save()
        } else {
            //TODO 遍历当前存储的数据 删除当前对象
            var allCWV = LiteCoinBeanOperator.findAllCWVs()
            for (allCWV in hasCoins) {
                if (allCWV.tokenAddress.equals(coinBean.tokenAddress)) {
                    allCWV.delete()
                }
            }
        }
//        if (isOpen) {
//        LiteCoinBeanOperator.copyCoinBean(coinBean).save()
//        } else {
//            for (allETH in hasCoins) {
//                if (allETH.coin_name.equals(coinBean.coin_name)) {
//                    allETH.delete()
//                }
//            }
//        }
    }
}