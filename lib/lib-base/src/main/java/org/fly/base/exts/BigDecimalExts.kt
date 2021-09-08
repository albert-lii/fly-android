package org.fly.base.exts

import org.fly.base.utils.NumberUtils
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/10/27 3:26 PM
 * @description: -
 * @since: 1.0.0
 */

/**
 * 加法运算
 */
fun BigDecimal.plus(
    v: String?,
    precision: Int? = null,
    mode: RoundingMode = RoundingMode.FLOOR
): BigDecimal {
    return NumberUtils.plus(this.toString(), v, precision, mode)
}

/**
 * 减法运算
 */
fun BigDecimal.minus(
    v: String?,
    precision: Int? = null,
    mode: RoundingMode = RoundingMode.FLOOR
): BigDecimal {
    return NumberUtils.minus(this.toString(), v, precision, mode)
}

/**
 * 乘法运算
 */
fun BigDecimal.multiply(
    v: String?,
    precision: Int? = null,
    mode: RoundingMode = RoundingMode.FLOOR
): BigDecimal {
    return NumberUtils.multiply(this.toString(), v, precision, mode)
}

/**
 * 除法运算
 */
fun BigDecimal.divide(
    v: String?,
    precision: Int? = null,
    mode: RoundingMode = RoundingMode.HALF_EVEN
): BigDecimal {
    return NumberUtils.divide(this.toString(), v, precision, mode)
}

/**
 * 是否为0
 */
fun BigDecimal.isZero(): Boolean {
    return this.compareTo(BigDecimal.ZERO) == 0
}