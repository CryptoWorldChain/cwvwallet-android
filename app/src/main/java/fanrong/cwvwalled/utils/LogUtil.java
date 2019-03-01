package fanrong.cwvwalled.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import fanrong.cwvwalled.BuildConfig;

public class LogUtil {

    private static boolean isShowLog = BuildConfig.DEBUG;
    private static final int MAXLENGTH = 1024;


    /* ========================下面的是本地存储相关的========================== */

    /**
     * 写日志对象
     */
    private static LogWriter logWriter = null;

    /**
     * 写入本地日志线程
     */
    private static class LogWriter extends Thread {
        /**
         * 文件路径
         */
        private String mFilePath;
        /**
         * 调用这个类的线程
         */
        private int mPid;
        /**
         * 线程运行标志
         */
        private boolean isRunning = true;

        /**
         * @param filePath 文件路径
         * @param pid
         */
        public LogWriter(String filePath, int pid) {
            this.mPid = pid;
            this.mFilePath = filePath;
        }

        @Override
        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);//日期格式化对象
            Process process = null;//进程
            BufferedReader reader = null;
            FileWriter writer = null;
            try {

                /**
                 *
                 * log level：*:v , *:d , *:w , *:e , *:f , *:s
                 *
                 * Show the current mPID process level of E and W log.
                 *
                 * */
                //执行命令行
                String cmd = "logcat *:v | grep";
                process = Runtime.getRuntime().exec(cmd);
                //得到输入流
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()), 1024);
                //创建文件
                File file = new File(mFilePath);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                writer = new FileWriter(file, true);
                //循环写入文件
                String line = null;
                while (isRunning) {
                    line = reader.readLine();
                    if (line != null && line.length() > 0) {
                        writer.append("PID:" + this.mPid + "\t"
                                + sdf.format(new Date(System.currentTimeMillis())) + "\t" + line
                                + "\n");
                        writer.flush();
                    } else {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (process != null) {
                    process.destroy();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (writer != null) {
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                process = null;
                reader = null;
                writer = null;
            }
        }

        public void end() {
            isRunning = false;
        }
    }

    /**
     * 整个应用只需要调用一次即可:开始本地记录
     *
     * @param filePath 要写入的目的文件路径
     */
    @SuppressLint("DefaultLocale")
    public static void startWriteLogToSdcard(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            Calendar calendar = Calendar.getInstance();
            filePath = String.format("/sdcard/MH_Log_%04d%02d%02d_%02d%02d%02d.txt",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND));
        }

        if (logWriter == null) {
            try {
                /** LogUtil这个类的pid,必须在类外面得到 */
                logWriter = new LogWriter(filePath, android.os.Process.myPid());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (logWriter != null) {
            logWriter.start();
        }
    }

    /**
     * 整个应用只需要调用一次即可:结束本地记录
     */
    public static void endWriteLogToSdcard() {
        if (logWriter != null) {
            logWriter.end();
        }
    }


    /* ========================下面的是直接使用的========================== */

    /**
     * verbose详细日志
     *
     * @param tag     日志标记
     * @param message 日志信息
     */
    public static void v(String tag, String message) {
        if (isShowLog) {
            if (message.length() > MAXLENGTH) {
                for (int i = 0; i < message.length(); i += MAXLENGTH) {
                    if (i + MAXLENGTH < message.length()) {
                        Log.v(tag, getDetailMessage(message.substring(i, i + MAXLENGTH)));
                    } else {
                        Log.v(tag, getDetailMessage(message.substring(i)));
                    }
                }
            } else {
                Log.v(tag, getDetailMessage(message));
            }
        }
    }

    /**
     * verbose详细日志
     *
     * @param message 日志信息
     */
    public static void v(String message) {
        if (isShowLog) {
            v(getTag(), message);
        }
    }

    /**
     * error错误日志
     *
     * @param tag     日志标记
     * @param message 日志信息
     */
    public static void e(String tag, String message) {
        if (isShowLog) {
            if (message.length() > MAXLENGTH) {
                for (int i = 0; i < message.length(); i += MAXLENGTH) {
                    if (i + MAXLENGTH < message.length()) {
                        Log.e(tag, getDetailMessage(message.substring(i, i + MAXLENGTH)));
                    } else {
                        Log.e(tag, getDetailMessage(message.substring(i)));
                    }
                }
            } else {
                Log.e(tag, getDetailMessage(message));
            }
        }
    }

    /**
     * error错误日志
     *
     * @param message 日志信息
     */
    public static void e(String message) {
        if (isShowLog) {
            e(getTag(), message);
        }
    }

    /**
     * info信息日志
     *
     * @param tag     日志标记
     * @param message 日志信息
     */
    public static void i(String tag, String message) {
        if (isShowLog) {
            if (message.length() > MAXLENGTH) {
                for (int i = 0; i < message.length(); i += MAXLENGTH) {
                    if (i + MAXLENGTH < message.length()) {
                        Log.i(tag, getDetailMessage(message.substring(i, i + MAXLENGTH)));
                    } else {
                        Log.i(tag, getDetailMessage(message.substring(i)));
                    }
                }
            } else {
                Log.i(tag, getDetailMessage(message));
            }
        }
    }

    /**
     * info信息日志
     *
     * @param message 日志信息
     */
    public static void i(String message) {

        if (isShowLog) {
            i(getTag(), message);
        }
    }

    /**
     * debug调试日志
     *
     * @param tag     日志标记
     * @param message 日志信息
     */
    public static void d(String tag, String message) {

        if (isShowLog) {
            if (message.length() > MAXLENGTH) {
                for (int i = 0; i < message.length(); i += MAXLENGTH) {
                    if (i + MAXLENGTH < message.length()) {
                        Log.d(tag, getDetailMessage(message.substring(i, i + MAXLENGTH)));
                    } else {
                        Log.d(tag, getDetailMessage(message.substring(i)));
                    }
                }
            } else {
                Log.d(tag, getDetailMessage(message));
            }
        }
    }

    /**
     * debug调试日志
     *
     * @param message 日志信息
     */
    public static void d(String message) {
        if (isShowLog) {
            d(getTag(), message);
        }
    }

    /**
     * warn警告日志
     *
     * @param tag     日志标记
     * @param message 日志信息
     */
    public static void w(String tag, String message) {
        if (isShowLog) {
            int maxLength = 4000;
            if (message.length() > maxLength) {
                for (int i = 0; i < message.length(); i += maxLength) {
                    if (i + maxLength < message.length()) {
                        Log.w(tag, getDetailMessage(message.substring(i, i + maxLength)));
                    } else {
                        Log.w(tag, getDetailMessage(message.substring(i)));
                    }
                }
            } else {
                Log.w(tag, getDetailMessage(message));
            }
        }
    }

    /**
     * warn警告日志
     *
     * @param message 日志信息
     */
    public static void w(String message) {
        if (isShowLog) {
            w(getTag(), message);
        }
    }

    /**
     * 得到默认tag【类名】以及信息详情
     *
     * @return 默认tag【类名】以及信息详情,默认信息详情【类名+方法名+行号+message】
     */
    private static String getTag() {
        for (StackTraceElement ste : (new Throwable()).getStackTrace()) {
            //栈顶肯定是LogUtil这个类自己
            if (!LogUtil.class.getName().equals(ste.getClassName())) {
                int b = ste.getClassName().lastIndexOf(".") + 1;
                return ste.getClassName().substring(b);
            }
        }
        return Thread.currentThread().getStackTrace()[4].getFileName();
    }

    /**
     * 得到一个信息的详细的情况【类名+方法名+行号】
     *
     * @param message 要显示的信息
     * @return 一个信息的详细的情况【类名+方法名+行号+message】
     */
    private static String getDetailMessage(String message) {
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[4];
        if (LogUtil.class.getName().equals(stackTrace.getClassName())) {
            stackTrace = Thread.currentThread().getStackTrace()[5];
        }
        return "(" +
                stackTrace.getFileName() +
                ":" +
                stackTrace.getLineNumber() +
                ")" +
//                "." +
//                stackTrace.getMethodName() +
                "-->" +
                message;
    }

}
