package org.we.fly.utils

import android.widget.EditText
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
        // 防止因为语言环境切换，导致小数点变成其他符号
        val symbols = DecimalFormatSymbols()
        symbols.decimalSeparator = '.'
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
     */
    fun formatInput(et: EditText, limitDecimalPlaces: Boolean = false, decimalPlaces: Int = 2) {
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
    }
}