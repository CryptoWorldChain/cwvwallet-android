package fanrong.cwvwalled

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
        assertEquals(4, 2 + 2)
        var format = "HH:00"
        val timeMillis = System.currentTimeMillis()


        chartXText.forEachIndexed { index, s ->
            val instance = Calendar.getInstance()
            instance.set(Calendar.DAY_OF_YEAR, instance.get(Calendar.DAY_OF_YEAR) - index)
            chartXText[chartXText.size - index - 1] = "${instance.get(Calendar.MONTH) + 1}:${instance.get(Calendar.DAY_OF_MONTH)}"
        }


        println(Arrays.toString(chartXText))
        Collections.reverse(Arrays.asList(chartXText))

    }
}
