package fanrong.cwvwalled.litepal;

import android.content.ContentValues;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.List;

import fanrong.cwvwalled.eventbus.WalletChangeEvent;
import fanrong.cwvwalled.litepal.GreWalletModel;
import xianchao.com.basiclib.utils.CheckedUtils;

public class GreWalletOperator {

    public static List<GreWalletModel> queryImport() {
        return LitePal.where("isImport like ?", "1")
                .find(GreWalletModel.class);
    }

    public static GreWalletModel queryAddress(String addr) {
        List<GreWalletModel> walletModels = LitePal.where("address like ?", addr).find(GreWalletModel.class);
        if (CheckedUtils.INSTANCE.isEmpty(walletModels)) {
            return null;
        } else {
            return walletModels.get(0);
        }
    }

    public static void insert(GreWalletModel greWallet) {
        greWallet.setShowRmb(true);
        greWallet.save();
        EventBus.getDefault().post(new WalletChangeEvent());
    }

    public static void updateWalletName(GreWalletModel greWallet) {
        ContentValues values = new ContentValues();
        values.put("walletName", greWallet.getWalletName());
        LitePal.update(GreWalletModel.class, values, greWallet.getId());

        LiteCoinBeanOperator.INSTANCE.updateAllWalletName(greWallet);

        EventBus.getDefault().post(new WalletChangeEvent());
    }

    public static List<GreWalletModel> queryAll() {
        return LitePal.findAll(GreWalletModel.class);
    }

    public static GreWalletModel queryMainCWV() {
        List<GreWalletModel> all = LitePal.where("isImport is null and walletType like ?", "CWV")
                .find(GreWalletModel.class);
        if (CheckedUtils.INSTANCE.nonEmpty(all)) {
            return all.get(0);
        }
        return null;
    }

    public static GreWalletModel queryMainETH() {

        List<GreWalletModel> all = LitePal.where("isImport is null and walletType like ?", "ETH")
                .find(GreWalletModel.class);
        if (CheckedUtils.INSTANCE.nonEmpty(all)) {
            return all.get(0);
        }
        return null;
    }

    public static GreWalletModel queryWalletWithAddress(String address) {

        List<GreWalletModel> all = LitePal.where("address like ?", address)
                .find(GreWalletModel.class);
        if (CheckedUtils.INSTANCE.nonEmpty(all)) {
            return all.get(0);
        }
        return null;

    }

    public static GreWalletModel updateIsShowRmb(GreWalletModel walletModel, boolean isShowRmb) {

        ContentValues values = new ContentValues();
        values.put("isShowRmb", walletModel.isShowRmb());
        LitePal.update(GreWalletModel.class, values, walletModel.getId());
        return null;

    }

}
