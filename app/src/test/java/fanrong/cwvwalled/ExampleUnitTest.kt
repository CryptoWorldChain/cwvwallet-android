package fanrong.cwvwalled

import com.google.gson.Gson
import fanrong.cwvwalled.http.model.UpdateResp
import org.junit.Test

import org.junit.Assert.*
import java.util.*
import java.util.zip.DataFormatException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    var chartXText = arrayOfNulls<String>(6)
    @Test
    fun addition_isCorrect() {

        val aaaa = Aaaa()
        aaaa.change(aaaa.good,aaaa.ch)

        println(aaaa.good + "and" + aaaa.ch.toString())

    }


}
