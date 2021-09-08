package org.fly.base.exts

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/7 9:17 PM
 * @description: EditText的扩展
 * @since: 1.0.0
 */

/**
 * 文字输入监听
 */
inline fun EditText.addTextChanged(
    crossinline afterChanged: (s: Editable?) -> Unit = {},
    crossinline beforeChanged: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { s, start, couunt, after -> },
    crossinline onChanged: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit = { s, start, before, count -> }
) {
    val listener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = afterChanged(s)

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
            beforeChanged(s, start, count, after)

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
            onChanged(s, start, before, count)
    }
    addTextChangedListener(listener)
}

inline fun EditText.afterTextChanged(crossinline afterChanged: (s: Editable?) -> Unit = {}) {
    addTextChanged(afterChanged = afterChanged)
}

inline fun EditText.beforeTextChanged(
    crossinline beforeChanged: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _ -> }
) {
    addTextChanged(beforeChanged = beforeChanged)
}

inline fun EditText.onTextChanged(
    crossinline onChanged: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit = { _, _, _, _ -> }
) {
    addTextChanged(onChanged = onChanged)
}


/**
 * 显示软键盘
 */
fun EditText.showSoftInput(context: Context) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInputFromInputMethod(this.getWindowToken(), 0)
}

/**
 * 隐藏软键盘
 */
fun EditText.hideSoftInput(context: Context) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

/**
 * 切换软键盘的显示与隐藏
 */
fun EditText.triggleSoftInput(context: Context) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInputFromWindow(this.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS)
}

/**
 * 移动光标到文字指定位置
 */
fun EditText.setSelectionSafely(index: Int) {
    try {
        this.setSelection(index)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 将光标移动到文字最后面
 */
fun EditText.moveSelectionToEnd() {
    text.length.let { if (it > 0) setSelection(it) }
}