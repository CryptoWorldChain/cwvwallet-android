package fanrong.cwvwalled.utils

import java.math.BigDecimal
import java.math.BigInteger

import xianchao.com.basiclib.utils.CheckedUtils
import java.math.RoundingMode

object DecimalUtils {

    fun scale2Down(string: String): String {
        if (CheckedUtils.isEmpty(string)) {
            return ""
        }

        return BigDecimal(string).setScale(2, RoundingMode.DOWN).toString()
    }
}
