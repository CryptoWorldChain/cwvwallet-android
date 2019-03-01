package fanrong.cwvwalled.http.engine;

import com.google.gson.Gson;

import okhttp3.RequestBody;

public class ConvertToBody {
    public static RequestBody ConvertToBody(Object paremter){
        String jsonParemter = new Gson().toJson(paremter);
        return RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"), jsonParemter);
    }
}
