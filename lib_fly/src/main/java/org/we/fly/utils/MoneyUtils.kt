package org.we.fly.utils

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/30 3:41 PM
 * @description: 金额相关的工具类
 * @since: 1.0.0
 */
object MoneyUtils {

    /**
     * 将金额格式化为国际化数字格式
     *
     * @param money 金额
     * @param maxFractionDigits 有效小数位
     * @param minFractionDigits 最小的小数有效位
     * @param isHalfUp 是否四舍五入
     * @param minIntegerDigits 最少的有效整数位，默认整数位最少有1位数字
     */
    fun formatMoneyToIN(
        money: String,
        maxFractionDigits: Int = 2,
        minFractionDigits: Int = 2,
        isHalfUp: Boolean = false,
        minIntegerDigits: Int = 1
    ): String {
        var moneyDouble = money.toDoubleOrNull()
        if (moneyDouble == null) {
            moneyDouble = 0.0
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
        var integerPattern = ""
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
        var pattern = integerPattern
        if (fractionPattern.isNotEmpty()) {
            pattern += ".${fractionPattern}"
        }
        val df = DecimalFormat(pattern)
        df.currency = Currency.getInstance(Locale.CHINA)
        df.roundingMode = if (isHalfUp) RoundingMode.HALF_UP else RoundingMode.DOWN
        // 防止因为语言环境切换，导致小数点变成其他符号
        val symbols = DecimalFormatSymbols()
        symbols.decimalSeparator = '.'
        df.decimalFormatSymbols = symbols
        return df.format(moneyDouble)
    }
}