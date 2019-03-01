package fanrong.cwvwalled.http.engine;

import com.facebook.stetho.okhttp3.StethoInterceptor;


import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import fanrong.cwvwalled.http.url.URLBuilder;
import fanrong.cwvwalled.utils.FanRongTextUtils;
import fanrong.cwvwalled.utils.SWLog;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by terry.c on 06/03/2018.
 */

public class RetrofitClient {

    private static final RetrofitClient ourInstance = new RetrofitClient();

    public static RetrofitClient getInstance() {
        return ourInstance;
    }

    private RetrofitClient() {
        createRetrofit();
    }

    private Retrofit mRetrofit;

    private static final int mTimeOut = 20;

    public Retrofit createRetrofit() {
        if (mRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    //打印retrofit日志
                    try {
                        SWLog.d("RetrofitClient retrofitBack = ", FanRongTextUtils.unicodeToUtf8(message));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
//                    .cache(cache)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request spdt = chain.request().newBuilder()
                                    .addHeader("dapp_id", "CWV")
                                    .addHeader("body-sign", "CWV!@#4")
                                    .addHeader("Content-Type","application/x-www-form-urlencoded")
                                    .build();
                            return chain.proceed(spdt);
                        }
                    })
                    .addNetworkInterceptor(new StethoInterceptor())
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(mTimeOut, TimeUnit.SECONDS)
                    .readTimeout(mTimeOut, TimeUnit.SECONDS)
                    .writeTimeout(mTimeOut, TimeUnit.SECONDS)
                    .build();


            mRetrofit = new Retrofit.Builder()
                    .baseUrl(URLBuilder.ins().getHostUrl())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public Retrofit createRetrofit(String baseUrl) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                try {
                    SWLog.d("RetrofitClient retrofitBack = ", FanRongTextUtils.unicodeToUtf8(message));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
//                    .cache(cache)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(mTimeOut, TimeUnit.SECONDS)
                .readTimeout(mTimeOut, TimeUnit.SECONDS)
                .writeTimeout(mTimeOut, TimeUnit.SECONDS)
                .build();


        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return mRetrofit;
    }


    private static HashMap<Class, Object> hashMap = new HashMap();

    public static FBCNetWorkApi getFBCNetWorkApi() {
        FBCNetWorkApi netWorkApi = (FBCNetWorkApi) hashMap.get(FBCNetWorkApi.class);
        if (netWorkApi == null) {
            synchronized (FBCNetWorkApi.class) {
                netWorkApi = getInstance().mRetrofit.create(FBCNetWorkApi.class);
                hashMap.put(FBCNetWorkApi.class,netWorkApi);

            }
        }
        return netWorkApi;
    }


    public static ETHNetWorkApi getETHNetWorkApi() {
        ETHNetWorkApi netWorkApi = (ETHNetWorkApi) hashMap.get(ETHNetWorkApi.class);
        if (netWorkApi == null) {
            synchronized (FBCNetWorkApi.class) {
                netWorkApi = getInstance().mRetrofit.create(ETHNetWorkApi.class);
                hashMap.put(ETHNetWorkApi.class,netWorkApi);

            }
        }
        return netWorkApi;
    }

    public static NetWorkApi getNetWorkApi() {
        NetWorkApi netWorkApi = (NetWorkApi) hashMap.get(NetWorkApi.class);
        if (netWorkApi == null) {
            synchronized (FBCNetWorkApi.class) {
                netWorkApi = getInstance().mRetrofit.create(NetWorkApi.class);
                hashMap.put(netWorkApi.getClass(),netWorkApi);

            }
        }
        return netWorkApi;
    }
    public static OTCNetWorkApi getOTCNetWorkApi() {
        OTCNetWorkApi netWorkApi = (OTCNetWorkApi) hashMap.get(NetWorkApi.class);
        if (netWorkApi == null) {
            synchronized (FBCNetWorkApi.class) {
                netWorkApi = getInstance().mRetrofit.create(OTCNetWorkApi.class);
                hashMap.put(OTCNetWorkApi.class,netWorkApi);
            }
        }
        return netWorkApi;
    }

}
