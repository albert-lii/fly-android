package org.we.fly.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/7 9:17 PM
 * @description: EditText的扩展
 * @since: 1.0.0
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

inline fun EditText.beforeChanged(
    crossinline beforeChanged: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _ -> }
) {
    addTextChanged(beforeChanged = beforeChanged)
}

inline fun EditText.onChanged(
    crossinline onChanged: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit = { _, _, _, _ -> }
) {
    addTextChanged(onChanged = onChanged)
}