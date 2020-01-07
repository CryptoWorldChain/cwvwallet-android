package fanrong.cwvwalled.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import fanrong.cwvwalled.ui.activity.SplashActivity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
//自定义异常管理类  暂不使用
public class CarshHandler implements UncaughtExceptionHandler {

  private final String TAG = "CarshHandler";
//  private final String PATH = Environment.getExternalStorageDirectory().getPath()+"/crash/";
  private final String PATH ="/sdcard/CrashInfo/";
  private Map<String, String> exInfor = new HashMap<String, String>();
  private Context mContext;
  String crashHead;
  private String nameString = "cwv";
  private SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH-mm-ss");
  /*
   * 是否打开日志输出 在debug状态下
   * 在Release状态下关闭以提示程序性能
   * */
  public static final boolean DEBUG = true;
  /*系统默认的UncaughtException类
   * */
  private Thread.UncaughtExceptionHandler mDefaultHandler;

  /*CarshHandler 实例
   * */
  private static CarshHandler mCarshHandler;

  /*CarshHandler单例
   * */
  public static CarshHandler getInstance() {
    if (mCarshHandler == null) {
      synchronized (CarshHandler.class) {
        if (mCarshHandler == null) {
          mCarshHandler = new CarshHandler();
        }
      }
    }
    return mCarshHandler;
  }

  /**
   * @param context
   * 初始化 注册Cintext对象
   * 获取系统默认的UncaughtException处理器
   * 设置该UncaughtException为程序的默认处理器
   * */
  public void init(Context context) {
    mContext =context;
    //获取系统默认的UncaughtExceptionHandler处理器
    mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    //设置该CrashHandler为程序的默认处理器
    Thread.setDefaultUncaughtExceptionHandler(this);
  }

  /**
   * @param thread
   * @param ex
   * 当UncaughtException发生时被调
   * */
  @Override
  public void uncaughtException(Thread thread, Throwable ex) {
    if (!handlerException(ex) && mDefaultHandler !=null) {
            //如果用户没有处理则让系统让系统默认的异常处理器来处理
      mDefaultHandler.uncaughtException(thread,ex);
    }else {
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
          Log.e(TAG,"Error",e);
      }
      //退出程序
//      android.os.Process.killProcess(android.os.Process.myPid());
      AppManager.INSTANCE.popAllActivity();
    }
  }

  /**自定义错误处理 手机错误信息 发送错误报告等操作
   * @param ex
   * @return 返回ture表示处理了改异常  返回false表示未处理该异常
   */
  private boolean handlerException(Throwable ex) {
    if (ex == null) {
      return false;
    }
    new Thread() {
      @Override
      public void run() {
        Looper.prepare();
        //TODO 显示dialog 信息
        Toast.makeText(mContext, "很抱歉，程序出现异常，正在收集日志，即将退出", Toast.LENGTH_SHORT).show();
        Log.e(TAG+"PATH",PATH);
        Looper.loop();
      }
    }.start();
    //收集参数设备信息
    collectDevicInfor(mContext);
    //保存日志到文件
    saveCrashInforToFile(ex);
    //app重启
    //restartApp();

    return true;
  }


  /**
   * 保存错误信息到文件中
   * @param ex
   * @return 文件名称
   */
  private String saveCrashInforToFile(Throwable ex) {
    Log.e(TAG+"PATH",PATH);
    StringBuffer sb = new StringBuffer();
    sb.append(crashHead);
    for (Entry<String, String> enty : exInfor.entrySet()) {
      String key = enty.getKey();
      String value = enty.getValue();
      sb.append(key + "=" + value + "\n");
    }
    //获取错误报告
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    Throwable cause = ex.getCause();
    while (cause == null) {
      cause.printStackTrace(pw);
      cause.getCause();
    }
    pw.close();
    String result = sw.toString();
    sb.append(result);
    try {
      long timesTamp = System.currentTimeMillis();
      String time = dateFormat.format(new Date());
      String fileName = nameString + "-" + time + "-" + timesTamp + ".txt";
      if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        File file = new File(PATH);
        if (file.exists()) {
          file.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(PATH + fileName);
        fos.write(sb.toString().getBytes());
        fos.close();
      }
      return fileName;
    } catch (Exception e) {
      Log.e(TAG, "an error occured while writing file...", e);

    }
    return null;
  }


  /**
   *收集设备参数
   */
  public String collectDevicInfor(Context context) {
    try {
      PackageManager packageManager = context.getPackageManager();
      PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
      if (packageInfo != null) {
        String versionName = packageInfo.versionName == null ? "null" : packageInfo.versionName;
        String versionCode = packageInfo.versionCode + "";
        exInfor.put("versionName", versionName);
        exInfor.put("versionCode", versionCode);
        crashHead = "\n********CrashHead******" +
            "\n手机品牌" + Build.MANUFACTURER +
            "\n手机型号" + Build.MODEL +
            "\nAndroid 版本" + VERSION.RELEASE +
            "\nAndrodi SDK版本" + VERSION.SDK_INT +
            "\nApp versionName" + versionName +
            "\nApp versionCode" + versionCode;
      }
    } catch (NameNotFoundException e) {
      Log.e(TAG, "an error occured when collect pakage infor" + e);
    }
   //反射获取对象成员的字段值，getFields()和getDeclaredFields()用法区别
    //getFields()只能获取public的字段，包括父类的。
    //getDeclaredFields()只能获取自己声明的各种字段，包括public，protected，private。
    Field[] declaredFields = Build.class.getDeclaredFields();
    for (Field file : declaredFields) {
      //通过反射获取到的当需要修改他的私有属性时 使用file.setAccessible(true)关闭安全检查
      //默认是false 代表开启   true代表关闭
      file.setAccessible(true);
//      try {
//      //  exInfor.put(file.getName(), file.get(null).toString());
//        Log.e(TAG, file.getName() + ":", (Throwable) file.get(null));
//      } catch (IllegalAccessException e) {
//        Log.e(TAG, "an error occured when collect pakage infor" + e);
//        e.printStackTrace();
//      }
    }
    return crashHead;
  }

  /*
  * 重启 APP
  * */
  @SuppressLint("WrongConstant")
  private void restartApp(){
    Intent intent = new Intent(mContext.getApplicationContext(), SplashActivity.class);
    AlarmManager manager= (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    PendingIntent activity = PendingIntent
        .getActivity(mContext.getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      //Android6.0以上，包含6.0
      manager.setExactAndAllowWhileIdle(AlarmManager.RTC, System.currentTimeMillis() + 1000, activity); //解决Android6.0省电机制带来的不准时问题
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      //Android4.4到Android6.0之间，包含4.4
      manager.setExact(AlarmManager.RTC, System.currentTimeMillis() + 1000, activity); // 解决set()在api19上不准时问题
    } else {
      manager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, activity);
    }

  }

}
