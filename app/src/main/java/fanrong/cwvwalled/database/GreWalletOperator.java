package fanrong.cwvwalled.database;

import java.util.List;

public class GreWalletOperator {

    public static void insert(GreWallet greWallet) {
        GreendaoUtils.getDaoSession().getGreWalletDao().insert(greWallet);
    }

    public static List<GreWallet> queryAll(){
        return GreendaoUtils.getDaoSession().getGreWalletDao().queryBuilder().list();
    }
}
