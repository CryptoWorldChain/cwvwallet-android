package fanrong.cwvwalled.utils;

import android.os.Build;
import android.util.JsonReader;
import android.util.JsonToken;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.StringReader;

import fanrong.cwvwalled.base.AppApplication;

public class CallJsCodeUtils {

    private static WebView webView;

    public static void init() {
        webView = getWebView();
    }


    private static WebView getWebView() {
        final WebView webView = new WebView(AppApplication.Companion.getInstance());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient());
        // Enable remote debugging via chrome://inspect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                SWLog.e("onConsoleMessage：：" + consoleMessage.message() +
                        "----" + consoleMessage.lineNumber() +
                        "====" + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        webView.loadUrl("file:///android_asset/www/index.html");
        return webView;
    }


    public static String readStringJsValue(String jsValue) {
        JsonReader reader = new JsonReader(new StringReader(jsValue));
        // Must set lenient to parse single values
        reader.setLenient(true);
        try {
            if (reader.peek() != JsonToken.NULL) {
                if (reader.peek() == JsonToken.STRING) {
                    String msg = reader.nextString();
                    return msg;
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                // NOOP
            }
        }
        return null;
    }


    public static void generateMnemonic(ValueCallback<String> valueCallback) {
        final String english = String.format("bip44.generateMnemonic(null,null, bip44.wordlists.EN);");
        webView.evaluateJavascript(english, valueCallback);

    }

    public static void mnemonicToHDPrivateKey(String mnemonicStr, ValueCallback<String> valueCallback) {

        final String english = String.format("var base58 = bip44.mnemonicToHDPrivateKey('%s','');bip44.getPrivateKey(base58, 0).toString('hex');", mnemonicStr);
        webView.evaluateJavascript(english, valueCallback);

    }

    public static void getAddress(String dPrivateKey, ValueCallback<String> valueCallback) {

        final String english = String.format("bip44.getAddress('%s',0);", dPrivateKey);
        webView.evaluateJavascript(english, valueCallback);

    }

    public static void getPrivateKey(String dPrivateKey, ValueCallback<String> valueCallback) {

        final String english = String.format("bip44.getPrivateKey('%s',0).toString('hex');", dPrivateKey);
        webView.evaluateJavascript(english, valueCallback);

    }


    public static void privateToAddress(String privateKey, ValueCallback<String> valueCallback) {
        // ETH 通过私钥获取 地址，CWV 私钥和ETH 生成出来是一样的
        final String english = String.format("bip44.privateToAddress('%s');", privateKey);
        webView.evaluateJavascript(english, valueCallback);

    }
    public static void cwv_GenFromPrikey(String dPrivateKey, ValueCallback<String> valueCallback) {

        final String english = String.format("android_funcCreateWallet('%s');", dPrivateKey);
        webView.evaluateJavascript(english, valueCallback);

    }


    public static void cwv_ecHexSign(String dPrivateKey, String hexStr, ValueCallback<String> valueCallback) {

        final String english = String.format("cwv.ecHexSign('%s','%s');", dPrivateKey, hexStr);
        webView.evaluateJavascript(english, valueCallback);

    }

    //
    public static class EthParamter {
        public String nonce;
        public String gasPrice;
        public String to;
        public String value;
        public String data;
        public String gasLimit = "0x61A80";
        public String chainId = "1337";
    }

    public static void eth_ecHexSign(EthParamter paramter, String privateKey, ValueCallback<String> valueCallback) {
//
//
//        EthParamter ethParamter = paramter = new EthParamter();
//        ethParamter.value = "0xDE0B6B3A7640000";
//        ethParamter.nonce = "0x19";
//        ethParamter.gasPrice = "0x1";
//        ethParamter.gasLimit = "0x61A80";
//        ethParamter.to = "0x0dd69df4cbe26a089133411ae3dc1a059c513961";
//        ethParamter.data = "0x0";
//        ethParamter.chainId = "1";
//        privateKey = "0fb54c7973d3609528ca6853b3926bf29fdb96f06a29013e083727896732048b";

//        StringBuilder builder = new StringBuilder();
//        builder.append("var tx = new ethsign(");
//        builder.append(new Gson().toJson(paramter) + ");");
//        builder.append("tx.sign('");
//        builder.append(privateKey);
//        builder.append("');");
//        builder.append("tx.serialize().toString('hex')");

        String singe = "var tx = new ethsign({" +
                "nonce:'%s'," +
                "gasPrice:'%s'," +
                "gasLimit:'%s'," +
                "to:'%s'," +
                "value:'%s'," +
                "data:'%s'," +
                "chainId:%d" +
                "});tx.sign('%s');tx.serialize().toString('hex')";


        String format = String.format(singe,
                paramter.nonce,
                paramter.gasPrice,
                paramter.gasLimit,
                paramter.to,
                paramter.value,
                paramter.data,
                Integer.valueOf(paramter.chainId),
                privateKey);

        SWLog.e(format);
        webView.evaluateJavascript(format, valueCallback);

    }


}
