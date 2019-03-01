package fanrong.cwvwalled.http.model;

import java.io.Serializable;
import java.util.ArrayList;

public class BussnessModel implements Serializable{

    public String errcode;
    public String msg;
    public Page page;
    public ArrayList<Maiadvert> maiadvert;

    public static class Maiadvert implements Serializable{
        public String ugOtcAdvertId;
        public String userId;
        public String orderAllNumber;
        public String orderTotle;
        public String successRate;
        public String number;
        public String amountType;
        public String limitMaxAmount;
        public String limitMinAmount;
        public String price;
        public String createdtime;
        public String accountaddress;
        public String paymentway;
    }


    public static class Page implements Serializable{
        public String pageno;
        public String pagesize;
        public String sum;
    }

}
