package fanrong.cwvwalled.parenter

import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.litepal.*
import net.sourceforge.http.model.CWVCoinType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAssetCWVPresenter : AddAssetPresenter() {


    override fun requestAsset(inputStr: String, valueCallBack: ValueCallBack<List<TokenInfo>> ,wallet: GreWalletModel) {

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
                                var allCWV = LiteCoinBeanOperator.findAllFromParent(wallet.address)

                                for (token in tokenInfo) {
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


        if (isOpen) {
            //TODO()存储当前的对象 coinBean
            LiteCoinBeanOperator.copyTokenInfor(coinBean).save()
          //  coinBean.save()
        } else {
            //TODO 遍历当前存储的数据 删除当前对象
            var allCWVs = LiteCoinBeanOperator.findAllFromParent(coinBean.sourceAddr?:"")
            for (allCWV in allCWVs) {
                if (allCWV.contract_addr.equals(coinBean.tokenAddress)) {
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