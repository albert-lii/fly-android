package org.fly.base.utils

import android.widget.EditText
import org.fly.base.exts.multiply
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
object NumberUtils {

    /**=============================================================================================
     * 对数字的处理
     **===========================================================================================*/

    @JvmStatic
    fun formatINT(
        digit: Double?,
        maxPrecision: Int = 2,
        minPrecision: Int = 2,
        mode: RoundingMode = RoundingMode.DOWN
    ): String {
        return formatINT(
            digit = if (digit == null) {
                null
            } else {
                digit.toString()
            },
            maxPrecision = maxPrecision,
            minPrecision = minPrecision,
            mode = mode
        )
    }

    /**
     * 将数字格式化为国际数字格式
     *
     * @param digit 数字
     * @param maxPrecision 最大保留小数位
     * @param minPrecision 最小保留小数位
     * @param mode 数字截取格式，默认向下取整
     */
    @JvmStatic
    fun formatINT(
        digit: String?,
        maxPrecision: Int = 2,
        minPrecision: Int = 2,
        mode: RoundingMode = RoundingMode.DOWN
    ): String {
        return format(
            digit = if (digit == null) {
                null
            } else {
                digit.toString()
            },
            maxPrecision = maxPrecision,
            minPrecision = minPrecision,
            mode = mode
        )
    }

    @JvmStatic
    fun formatPrecision(
        digit: Double?,
        maxDecimalPlaces: Int = 2,
        minDecimalPlaces: Int = 2,
        mode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        return formatPrecision(
            digit = if (digit == null) {
                null
            } else {
                digit.toString()
            },
            maxPrecision = maxDecimalPlaces,
            minPrecision = minDecimalPlaces,
            mode = mode
        )
    }

    /**
     * 根据保留的小数位来处理数字
     *
     * @param digit 数字
     * @param maxPrecision 最大保留的小数位
     * @param minPrecision 最小保留的小数位
     * @param mode 小数部分截取规则，默认四舍五入
     */
    @JvmStatic
    fun formatPrecision(
        digit: String?,
        maxPrecision: Int = 2,
        minPrecision: Int = 2,
        mode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        return format(
            digit = digit,
            maxPrecision = maxPrecision,
            minPrecision = minPrecision,
            mode = mode,
            integerFormat = "0"
        )
    }

    /**
     * 将数字格式化为指定的数字格式
     *
     * @param digit 数字
     * @param maxPrecision 最大保留的小数位
     * @param minPrecision 最小的保留的小数位
     * @param mode 数字截取模式
     * @param minIntegerBits 最少的有效整数位，默认整数位最少有1位数字
     * @param integerPattern 整数部分规则，默认使用国际数字规则
     */
    @JvmStatic
    fun format(
        digit: String?,
        maxPrecision: Int = 2,
        minPrecision: Int = 2,
        mode: RoundingMode = RoundingMode.HALF_UP,
        minIntegerBits: Int = 1,
        integerFormat: String = ""
    ): String {
        val digitBd: BigDecimal
        if (digit.isNullOrEmpty() || digit.toDoubleOrNull() == null) {
            digitBd = BigDecimal("0")
        } else {
            digitBd = BigDecimal(digit)
        }
        // 小数部分的规则
        var fractionPattern = ""
        var minFraction = minPrecision
        if (minPrecision > maxPrecision) {
            minFraction = maxPrecision
        }
        for (i in 0 until maxPrecision) {
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
//        df.currency = Currency.getInstance(Locale.ENGLISH)
        df.roundingMode = mode
        val symbols = DecimalFormatSymbols(Locale.ENGLISH)
        // 防止因为语言环境切换，导致小数点变成其他符号，例如印尼、葡萄牙等语言环境下，小数点会变成逗号
        // 但是如果数字的小数位还有千分位，则此方法也会无效
        // 所以此处推荐使用Locale来固定数字的显示为英文环境下的显示
//        symbols.decimalSeparator = '.'
        df.decimalFormatSymbols = symbols
        return df.format(digitBd)
    }

    /**
     * 保留指定的有效小数位
     *
     * @param digit 数字
     * @param precision 有效小数位
     * @param mode 有效小数位的舍入规则，默认四舍五入
     */
    @JvmStatic
    fun formatValidPrecision(
        digit: String?,
        precision: Int,
        precisionMode: RoundingMode = RoundingMode.HALF_UP
    ): BigDecimal {
        val d = digit ?: "0"
        val number = BigDecimal(d)
        val divisor: BigDecimal = BigDecimal.ONE
        val mc = MathContext(precision, precisionMode)
        return number.divide(divisor, mc)
    }

    /**
     * 将EditText中输入的数字转为指定格式，此方法建议放在onTextChanged方法中
     *
     * @param et 输入框控件
     * @param precision 数字的最大小数位
     * @param negative 是否允许输入负数
     */
    @JvmStatic
    fun formatInput(
        et: EditText,
        precision: Int? = null,
        negative: Boolean = true
    ) {
        val text = et.text.toString()
        if (text.startsWith(".")) {
            // 输入的的一个字符是"."，自动补全
            val s = if (precision == 0) {
                "0"
            } else {
                "0" + text
            }
            et.setText(s)
            et.setSelection(s.length)
        } else if (text.trim().startsWith("0") && text.trim().length > 1) {
            // 起始字符是0，并且后面字符不是"."，则后续无法输入
            if (!text.substring(1, 2).equals(".")) {
                et.setText(text.substring(0, 1))
                et.setSelection(1)
            }
        } else if (text.startsWith("-")) {
            if (!negative) {
                // 不允许输入负数
                et.setText("")
            }
        }
        if (precision != null) {
            if (text.contains(".")) {
                // 超过最大小数位
                if (precision <= 0) {
                    val s = text.substring(0, text.indexOf("."))
                    et.setText(s)
                    et.setSelection(s.length)
                } else if (text.length - text.indexOf(".") > (precision + 1)) {
                    val s = text.substring(0, text.indexOf(".") + (precision + 1))
                    et.setText(s)
                    et.setSelection(s.length)
                }
            }
        }
    }

    /**=============================================================================================
     * 在使用Double和Float进行计算时，尤其是减法运算，可能出现精度丢失现象，
     * 所以进行Double和Float计算时，建议使用BigDecimal进行计算
     **===========================================================================================*/

    /**
     * 加法运算
     *
     * @param precision 小数精度
     * @param mode 小数舍入模式
     */
    @JvmStatic
    fun plus(
        v1: String?,
        v2: String?,
        precision: Int? = null,
        mode: RoundingMode = RoundingMode.FLOOR
    ): BigDecimal {
        val t1 = if (v1.isNullOrEmpty()) "0" else v1
        val t2 = if (v2.isNullOrEmpty()) "0" else v2
        val b1 = BigDecimal(t1)
        val b2 = BigDecimal(t2)
        val result = b1.add(b2)
        if (precision != null) {
            return result.setScale(precision, mode)
        }
        return result
    }

    /**
     * 减法运算
     *
     * @param precision 小数精度
     * @param mode 小数舍入模式
     */
    @JvmStatic
    fun minus(
        v1: String?,
        v2: String?,
        precision: Int? = null,
        mode: RoundingMode = RoundingMode.FLOOR
    ): BigDecimal {
        val t1 = if (v1.isNullOrEmpty()) "0" else v1
        val t2 = if (v2.isNullOrEmpty()) "0" else v2
        val b1 = BigDecimal(t1)
        val b2 = BigDecimal(t2)
        val result = b1.subtract(b2)
        if (precision != null) {
            return result.setScale(precision, mode)
        }
        return result
    }

    /**
     * 乘法运算
     *
     * @param precision 小数精度
     * @param mode 小数舍入模式
     */
    @JvmStatic
    fun multiply(
        v1: String?,
        v2: String?,
        precision: Int? = null,
        mode: RoundingMode = RoundingMode.FLOOR
    ): BigDecimal {
        if (v1.isNullOrEmpty() || v2.isNullOrEmpty()) {
            return BigDecimal("0")
        }
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        val result = b1.multiply(b2)
        if (precision != null) {
            return result.setScale(precision, mode)
        }
        return result
    }

    /**
     * 除法运算
     */
    @JvmStatic
    fun divide(
        v1: String?,
        v2: String?,
        precision: Int? = null, // 小数精度
        mode: RoundingMode = RoundingMode.HALF_EVEN // 小数舍入模式
    ): BigDecimal {
        if (v1.isNullOrEmpty() || v2.isNullOrEmpty() || v2.toDoubleOrNull() == null || v2.toDouble() == 0.0) {
            return BigDecimal("0")
        }
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        if (precision == null) {
            return b1.divide(b2)
        } else {
            return b1.divide(b2, precision, mode)
        }
    }
}

