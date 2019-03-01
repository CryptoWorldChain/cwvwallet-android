package fanrong.cwvwalled.http.model;

import java.io.Serializable;
import java.util.List;

public class QueryTokensModel implements Serializable {

    public List<TokenModel> token;

    public String err_code;

    public String msg;

    public static class TokenModel implements Serializable {

        public String channel_name;
        public String coin_name;
        public String coin_symbol;
        public String coin_decimals;
        public String coin_total_supply;
        public String single_max_amt;
        public String single_min_amt;
        public double yuE;
        public String count;
        public String countCNY;

    }


}
