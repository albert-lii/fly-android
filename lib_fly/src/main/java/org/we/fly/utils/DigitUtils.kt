package org.we.fly.utils

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
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

    /**
     * 将数字格式化为国际化数字格式
     *
     * @param digit 数字
     * @param fractionDigits 有效小数位
     * @param mode 数字截取格式，默认向下取整
     */
    fun formatToIN(
        digit: String,
        fractionDigits: Int = 2,
        mode: RoundingMode = RoundingMode.DOWN
    ): String {
        return format(
            digit = digit,
            maxFractionDigits = fractionDigits,
            minFractionDigits = fractionDigits,
            mode = mode
        )
    }

    /**
     * 将数字格式化为国际化数字格式
     *
     * @param digit 数字
     * @param maxFractionDigits 有效小数位
     * @param minFractionDigits 最小的小数有效位
     * @param mode 数字截取格式，默认向下取整
     */
    fun formatToIN(
        digit: String,
        maxFractionDigits: Int = 2,
        minFractionDigits: Int = 2,
        mode: RoundingMode = RoundingMode.DOWN
    ): String {
        return format(
            digit = digit,
            maxFractionDigits = maxFractionDigits,
            minFractionDigits = minFractionDigits,
            mode = mode
        )
    }

    /**
     * 根据保留的小数位来处理数字
     *
     * @param digit 数字
     * @param fractionDigits 有效小数位
     * @param mode 小数部分截取规则，默认四舍五入
     * @param minIntegerDigits 最少的有效整数位，默认整数位最少有1位数字
     */
    fun formatByFractionDigits(
        digit: String,
        fractionDigits: Int = 2,
        mode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        return format(
            digit = digit,
            maxFractionDigits = fractionDigits,
            minFractionDigits = fractionDigits,
            mode = mode,
            integerFormat = "0"
        )
    }

    /**
     * 根据保留的小数位来处理数字
     *
     * @param digit 数字
     * @param maxFractionDigits 有效小数位
     * @param minFractionDigits 最小的小数有效位
     * @param mode 小数部分截取规则，默认四舍五入
     * @param minIntegerDigits 最少的有效整数位，默认整数位最少有1位数字
     */
    fun formatByFractionDigits(
        digit: String,
        maxFractionDigits: Int = 2,
        minFractionDigits: Int = 2,
        mode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        return format(
            digit = digit,
            maxFractionDigits = maxFractionDigits,
            minFractionDigits = minFractionDigits,
            mode = mode,
            integerFormat = "0"
        )
    }


    /**
     * 将数字格式化为指定的数字格式
     *
     * @param digit 数字
     * @param maxFractionDigits 有效小数位
     * @param minFractionDigits 最小的小数有效位
     * @param mode 数字截取模式
     * @param minIntegerDigits 最少的有效整数位，默认整数位最少有1位数字
     * @param integerPattern 整数部分规则
     */
    fun format(
        digit: String,
        maxFractionDigits: Int = 2,
        minFractionDigits: Int = 2,
        mode: RoundingMode = RoundingMode.HALF_UP,
        minIntegerDigits: Int = 1,
        integerFormat: String = ""
    ): String {
        var digitDouble = digit.toDoubleOrNull()
        if (digitDouble == null) {
            digitDouble = 0.0
        }
        // 小数部分的规则
        var fractionPattern = ""
        var minFraction = minFractionDigits
        if (minFractionDigits > maxFractionDigits) {
            minFraction = maxFractionDigits
        }
        for (i in 0 until maxFractionDigits) {
            if (i < minFraction) {
                fractionPattern += "0"
            } else {
                fractionPattern += "#"
            }
        }
        // 整数部分规则
        var integerPattern = integerFormat
        if (integerPattern.isEmpty()) {
            if (minIntegerDigits == 0) {
                integerPattern = "#,###"
            } else {
                for (i in 1..minIntegerDigits) {
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

    /**
     * 将EditText中输入的数字转为指定格式，此方法建议放在onTextChanged方法中
     *
     * @param et 输入框控件
     * @param fractionDigits 数字的最大小数位
     */
    fun formatInput(et: EditText, fractionDigits: Int = 2) {
        val text = et.text.toString()
        if (text.contains(".")) {
            // 超过最大小数位
            if (text.length - text.indexOf(".") > (fractionDigits + 1)) {
                val s = text.substring(0, text.indexOf(".") + (fractionDigits + 1))
                et.setText(s)
                et.setSelection(s.length)
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