package fanrong.cwvwalled.http.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WalletTransferModel implements Serializable {
    public String tx_id;
    public String err_code;

    public String msg;
    public BaseResponse.Status status;
}
