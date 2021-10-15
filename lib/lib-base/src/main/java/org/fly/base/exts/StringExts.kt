package org.fly.base.exts

import org.fly.base.utils.NumberUtils
import java.math.RoundingMode

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/24 8:05 下午
 * @description: String相关扩展
 * @since: 1.0.0
 */

fun String.plus(
    value: String?,
    precision: Int? = null,
    mode: RoundingMode = RoundingMode.FLOOR
): String {
    return NumberUtils.plus(v1 = this, v2 = value, precision = precision, mode = mode).toString()
}

fun String.minus(
    value: String?,
    precision: Int? = null,
    mode: RoundingMode = RoundingMode.FLOOR
): String {
    return NumberUtils.minus(v1 = this, v2 = value, precision = precision, mode = mode).toString()
}

fun String.multiply(
    value: String?,
    precision: Int? = null,
    mode: RoundingMode = RoundingMode.FLOOR
): String {
    return NumberUtils.multiply(v1 = this, v2 = value, precision = precision, mode = mode)
        .toString()
}

fun String.divide(
    value: String?,
    precision: Int? = null,
    mode: RoundingMode = RoundingMode.FLOOR
): String {
    return NumberUtils.divide(v1 = this, v2 = value, precision = precision, mode = mode).toString()
}

