package fanrong.cwvwalled.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

import xianchao.com.basiclib.utils.CheckedUtils;

public class MoneyUtils {


    /**
     * 判断值是否可以参与计算
     *
     * @param moneyCount
     * @return
     */
    private static boolean isValid(String moneyCount) {
        if (moneyCount == null || moneyCount.length() == 0 || new BigDecimal(moneyCount).doubleValue() == 0) {
            return false;
        }
        return true;
    }

    public static String getRightNum(String moneyCount) {
        if (!isValid(moneyCount)) {
            return "0";
        }
        try {
            BigDecimal bigDecimal = new BigDecimal(moneyCount);
            BigDecimal divide = bigDecimal.divide(new BigDecimal(Math.pow(10, 18)));
            return divide.toString() + "";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    public static String getMultiplyEighteen(String moneycount) {
        if (!isValid(moneycount)) {
            return "";
        }

        return new BigDecimal(moneycount)
                .multiply(new BigDecimal(Math.pow(10, 18)))
                .toBigInteger().toString();
    }


    public static String getMultiplyValue(String moneyOne, String moneyTwo) {
        if (!isValid(moneyOne) || !isValid(moneyTwo)) {
            return "0";
        }
        try {
            return new BigDecimal(moneyOne).multiply(new BigDecimal(moneyTwo)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }


    public static String commonHandleDecimal(String value) {
        if (CheckedUtils.INSTANCE.isEmpty(value)) {
            return "0.0000";
        }

        String string = new BigDecimal(value).setScale(4, BigDecimal.ROUND_DOWN).doubleValue() + "";
        //整数保留两位小数
        if (new BigDecimal(string).setScale(2, BigDecimal.ROUND_DOWN).compareTo(new BigDecimal(string)) == 0) {
            return new BigDecimal(string).setScale(2, BigDecimal.ROUND_DOWN).toString();
        } else {
            return string;
        }
    }



    public static String commonRMBDecimal(String value) {
        if (CheckedUtils.INSTANCE.isEmpty(value)) {
            return "0.00";
        }
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_DOWN).toString();

    }


    /**
     * 判断是不是整数
     *
     * @param value
     * @return
     */

    public static boolean isInteger(String value) {
        if (CheckedUtils.INSTANCE.isEmpty(value)) {
            return false;
        }

        return new BigDecimal(new BigDecimal(value).toBigInteger()).compareTo(new BigDecimal(value)) == 0;
    }


    public static String toHexValue(String string) {
        if (CheckedUtils.INSTANCE.isEmpty(string)) {
            return "";
        } else {
            return new BigInteger(string).toString(16);
        }

    }

    public static String hexToTen(String value) {

        if (value.startsWith("0x")) {
            value = value.replaceFirst("0x", "");
        }

        return Long.parseLong(value, 16) + "";
    }


    public static String getDownTip(String tip) {
        if (CheckedUtils.INSTANCE.isEmpty(tip)) {
            return "";
        } else {
            return new BigDecimal(hexToTen(tip))
                    .divide(new BigDecimal(Math.pow(10, 9))).setScale(9)
                    .toPlainString();
        }

    }


}
