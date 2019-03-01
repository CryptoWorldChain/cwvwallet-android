package fanrong.cwvwalled.database;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import org.greenrobot.greendao.AbstractDaoSession;

import fanrong.cwvwalled.base.AppApplication;

public class GreendaoUtils {

    private static DaoSession daoSession;

    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            root = "";
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(AppApplication.Companion.getInstance()
                    , root + "fanrong-db");
            SQLiteDatabase database = helper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(database);
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

}
