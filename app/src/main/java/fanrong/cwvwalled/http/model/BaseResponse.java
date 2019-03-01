package fanrong.cwvwalled.http.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by terry.c on 06/03/2018.
 */

public class BaseResponse<T> {

    @SerializedName("err_code")
    public String code;

    public String msg;

    public T data;

    public Status status;

    public BaseResponse() {

    }

    private BaseResponse(@NonNull Status status, @Nullable String message, @Nullable T data) {
        this.status = status;
        this.msg = message;
        this.data = data;
    }

    public static <T> BaseResponse<T> success(@NonNull T data) {
        return new BaseResponse<>(Status.SUCCESS, null, data);
    }

    public static <T> BaseResponse<T> error(String msg, @Nullable T data) {
        return new BaseResponse<>(Status.ERROR, msg, data);
    }

    public static <T> BaseResponse<T> loading(@Nullable T data) {
        return new BaseResponse<>(Status.LOADING, null, data);
    }

    public enum Status {SUCCESS, ERROR, LOADING}

}
