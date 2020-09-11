package org.we.fly.utils

import android.widget.EditText
import java.lang.NumberFormatException
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/30 3:41 PM
 * @description: 数字相关工具类
 * @since: 1.0.0
 */
object DigitUtils {

    fun formatToIN(
        digit: String?,
        decimalPlaces: Int = 2,
        mode: RoundingMode = RoundingMode.DOWN
    ): String {
        return formatToIN(
            digit = digit?.toDoubleOrNull(),
            maxDecimalPlaces = decimalPlaces,
            minDecimalPlaces = decimalPlaces,
            mode = mode
        )
    }

    /**
     * 将数字格式化为国际化数字格式
     *
     * @param digit 数字
     * @param maxDecimalPlaces 最大保留小数位
     * @param minDecimalPlaces 最小保留小数位
     * @param mode 数字截取格式，默认向下取整
     */
    fun formatToIN(
        digit: Double?,
        maxDecimalPlaces: Int = 2,
        minDecimalPlaces: Int = 2,
        mode: RoundingMode = RoundingMode.DOWN
    ): String {
        return format(
            digit = digit,
            maxDecimalPlaces = maxDecimalPlaces,
            minDecimalPlaces = minDecimalPlaces,
            mode = mode
        )
    }

    fun formatByDecimalPlaces(
        digit: String?,
        decimalPlaces: Int = 2,
        mode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        return formatByDecimalPlaces(
            digit = digit?.toDoubleOrNull(),
            decimalPlaces = decimalPlaces,
            mode = mode
        )
    }

    /**
     * 根据保留的小数位来处理数字
     *
     * @param digit 数字
     * @param decimalPlaces 保留的小数位
     * @param mode 小数部分截取规则，默认四舍五入
     * @param minIntegerDigits 最少的有效整数位，默认整数位最少有1位数字
     */
    fun formatByDecimalPlaces(
        digit: Double?,
        decimalPlaces: Int = 2,
        mode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        return format(
            digit = digit,
            maxDecimalPlaces = decimalPlaces,
            minDecimalPlaces = decimalPlaces,
            mode = mode,
            integerFormat = "0"
        )
    }

    fun formatByDecimalPlaces(
        digit: String?,
        maxDecimalPlaces: Int = 2,
        minDecimalPlaces: Int = 2,
        mode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        return formatByDecimalPlaces(
            digit = digit?.toDoubleOrNull(),
            maxDecimalPlaces = maxDecimalPlaces,
            minDecimalPlaces = minDecimalPlaces,
            mode = mode
        )
    }

    /**
     * 根据保留的小数位来处理数字
     *
     * @param digit 数字
     * @param maxFractionDigits 最大保留的小数位
     * @param minFractionDigits 最小保留的小数位
     * @param mode 小数部分截取规则，默认四舍五入
     * @param minIntegerDigits 最少的有效整数位，默认整数位最少有1位数字
     */
    fun formatByDecimalPlaces(
        digit: Double?,
        maxDecimalPlaces: Int = 2,
        minDecimalPlaces: Int = 2,
        mode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        return format(
            digit = digit,
            maxDecimalPlaces = maxDecimalPlaces,
            minDecimalPlaces = minDecimalPlaces,
            mode = mode,
            integerFormat = "0"
        )
    }

    /**
     * 将数字格式化为指定的数字格式
     *
     * @param digit 数字
     * @param maxDecimalPlaces 最大保留的小数位
     * @param minDecimalPlaces 最小的保留的小数位
     * @param mode 数字截取模式
     * @param minIntegerBits 最少的有效整数位，默认整数位最少有1位数字
     * @param integerPattern 整数部分规则
     */
    fun format(
        digit: Double?,
        maxDecimalPlaces: Int = 2,
        minDecimalPlaces: Int = 2,
        mode: RoundingMode = RoundingMode.HALF_UP,
        minIntegerBits: Int = 1,
        integerFormat: String = ""
    ): String {
        val digitDouble = digit ?: 0.0
        // 小数部分的规则
        var fractionPattern = ""
        var minFraction = minDecimalPlaces
        if (minDecimalPlaces > maxDecimalPlaces) {
            minFraction = maxDecimalPlaces
        }
        for (i in 0 until maxDecimalPlaces) {
            if (i < minFraction) {
                fractionPattern += "0"
            } else {
                fractionPattern += "#"
            }
        }
        // 整数部分规则
        var integerPattern = integerFormat
        if (integerPattern.isEmpty()) {
            if (minIntegerBits == 0) {
                integerPattern = "#,###"
            } else {
                for (i in 1..minIntegerBits) {
                    if (i / 4 == 1) {
                        integerPattern = "0," + integerPattern
                    } else {
                        integerPattern = "0" + integerPattern
                    }
                }
            }
            if (integerPattern.length < 4) {
                val diff = 4 - integerPattern.length
                for (i in 0 until diff) {
                    if (integerPattern.length == 3) {
                        integerPattern = "#," + integerPattern
                    } else {
                        integerPattern = "#" + integerPattern
                    }
                }
            }
        }
        var pattern = integerPattern
        if (fractionPattern.isNotEmpty()) {
            pattern += ".${fractionPattern}"
        }
        val df = DecimalFormat(pattern)
        df.currency = Currency.getInstance(Locale.CHINA)
        df.roundingMode = mode
        val symbols = DecimalFormatSymbols(Locale.ENGLISH)
        // 防止因为语言环境切换，导致小数点变成其他符号，例如印尼、葡萄牙等语言环境下，小数点会变成逗号
        // 但是如果数字的小数位还有千分位，则此方法也会无效
        // 所以此处推荐使用Locale来固定数字的显示为英文环境下的显示
//        symbols.decimalSeparator = '.'
        df.decimalFormatSymbols = symbols
        return df.format(digitDouble)
    }

    fun formatByDecimalPercision(
        digit: String?,
        precision: Int,
        precisionMode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        return formatByDecimalPercision(
            digit = digit?.toDoubleOrNull(),
            precision = precision,
            precisionMode = precisionMode
        )
    }

    /**
     * 保留指定的有效小数位
     *
     * @param digit 数字
     * @param precision 有效小数位
     * @param mode 有效小数位的舍入规则，默认四舍五入
     */
    fun formatByDecimalPercision(
        digit: Double?,
        precision: Int,
        precisionMode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        val digitDouble = digit ?: 0.0
        val number = BigDecimal(digitDouble)
        val divisor: BigDecimal = BigDecimal.ONE
        val mc = MathContext(precision, precisionMode)
        return number.divide(divisor, mc).toString()
    }

    /**
     * 将EditText中输入的数字转为指定格式，此方法建议放在onTextChanged方法中
     *
     * @param et 输入框控件
     * @param limitDecimalPlaces 是否限制小数的位数
     * @param decimalPlaces 数字的最大小数位，仅当 limitDecimalPlaces=true 时有效
     * @param negative 是否允许输入负数
     */
    fun formatInput(
        et: EditText,
        limitDecimalPlaces: Boolean = false,
        decimalPlaces: Int = 2,
        negative: Boolean = true
    ) {
        val text = et.text.toString()
        if (limitDecimalPlaces) {
            if (text.contains(".")) {
                // 超过最大小数位
                if (text.length - text.indexOf(".") > (decimalPlaces + 1)) {
                    val s = text.substring(0, text.indexOf(".") + (decimalPlaces + 1))
                    et.setText(s)
                    et.setSelection(s.length)
                }
            }
        }
        // 输入的的一个字符是"."，自动补全
        if (text.startsWith(".")) {
            val s = "0" + text
            et.setText(s)
            et.setSelection(s.length)
        }
        // 起始字符是0，并且后面字符不是"."，则后续无法输入
        if (text.trim().startsWith("0") && text.trim().length > 1) {
            if (!text.substring(1, 2).equals(".")) {
                et.setText(text.substring(0, 1))
                et.setSelection(1)
            }
        }
        // 输入的的一个字符是"-"，则清空，后续无法输入
        if (!negative) {
            if (text.startsWith("-")) {
                et.setText("")
            }
        }
    }

    /**===========================================================================
     * 在使用Double和Float进行计算时，尤其是减法运算，可能出现精度丢失现象，
     * 所以进行Double和Float计算时，建议使用BigDecimal进行计算
     **==========================================================================*/

    /**
     * 加法运算
     */
    fun sum(v1: String?, v2: String?): BigDecimal {
        val t1 = if (v1.isNullOrEmpty()) "0" else v1
        val t2 = if (v2.isNullOrEmpty()) "0" else v2
        val b1 = BigDecimal(t1)
        val b2 = BigDecimal(t2)
        return b1.plus(b2)
    }

    /**
     * 减法运算
     */
    fun imsub(v1: String?, v2: String?): BigDecimal {
        val t1 = if (v1.isNullOrEmpty()) "0" else v1
        val t2 = if (v2.isNullOrEmpty()) "0" else v2
        val b1 = BigDecimal(t1)
        val b2 = BigDecimal(t2)
        return b1.minus(b2)
    }

    /**
     * 乘法运算
     */
    fun product(v1: String?, v2: String?): BigDecimal {
        if (v1.isNullOrEmpty() || v2.isNullOrEmpty()) {
            return BigDecimal("0")
        }
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        return b1.times(b2)
    }

    /**
     * 除法运算
     */
    fun quotient(v1: String?, v2: String?): BigDecimal {
        if (v1.isNullOrEmpty() || v2.isNullOrEmpty()) {
            return BigDecimal("0")
        }
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        return b1.div(b2)
    }
}