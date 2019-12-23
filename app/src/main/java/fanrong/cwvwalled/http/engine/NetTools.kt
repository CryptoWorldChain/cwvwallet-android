package fanrong.cwvwalled.http.engine

import xianchao.com.basiclib.XcSingletons
import java.lang.Exception

object NetTools {
    fun <T> formatJson(json: String, clazz: Class<T>): T? {
        try {
            return XcSingletons.obtainGson().fromJson(json, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}