package xianchao.com.basiclib.utils

import android.os.BaseBundle
import android.os.Bundle
import java.io.Serializable

object BundleUtils {

    fun createWith(key: String, value: Serializable): Bundle {
        val bundle = Bundle()
        bundle.putSerializable(key, value)
        return bundle
    }

    fun createWith(key: String, value: String): Bundle {
        val bundle = Bundle()
        bundle.putString(key, value)
        return bundle
    }

    fun createWith(key: String, value: Boolean): Bundle {
        val bundle = Bundle()
        bundle.putBoolean(key, value)
        return bundle
    }

    fun createWith(key: String, value: Int): Bundle {
        val bundle = Bundle()
        bundle.putInt(key, value)
        return bundle
    }

}