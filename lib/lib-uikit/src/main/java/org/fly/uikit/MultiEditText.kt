package org.fly.uikit

import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.*
import android.text.InputFilter.LengthFilter
import android.text.InputType.TYPE_CLASS_NUMBER
import android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/4/21 11:52 AM
 * @description: 多功能输入框
 * @since: 1.0.0
 */
class MultiEditText : LinearLayout {

    /**
     * 输入类型
     */
    private val INPUT_TYPE_NONE = 0
    private val INPUT_TYPE_TEXT = 1
    private val INPUT_TYPE_TEXT_PASSWORD = 2
    private val INPUT_TYPE_NUMBER = 3
    private val INPUT_TYPE_NUMBER_DECIMAL = 4
    private val INPUT_TYPE_NUMBER_PASSWORD = 5
    private val INPUT_TYPE_NUMBER_SIGNED = 6
    private val INPUT_TYPE_TEXT_MULTI_LINE = 7

    /**
     * ellipsize
     */
    private val INPUT_ELLIPSIZE_START = 1
    private val INPUT_ELLIPSIZE_END = 2
    private val INPUT_ELLIPSIZE_MIDDLE = 3
    private val INPUT_ELLIPSIZE_MARQUEE = 4


    private lateinit var inputContainer: LinearLayout // 输入框所在容器
    private lateinit var inputView: EditText // 输入框
    private lateinit var leftIconView: ImageView // 输入框左侧icon
    private lateinit var pwdEyeView: ImageView // 密码可见icon
    private lateinit var clearView: ImageView // 文本清除icon
    private lateinit var errorView: TextView // 错误提示


    /**
     * 属性
     */
    // 输入框
    private var input = "" // 输入内容
    private var inputHint = ""// 输入提示
    private var inputHintColor = Color.GRAY // 输入提示的文字颜色
    private var inputTextSize = 14.dpToPxF // 输入文字大小
    private var inputTextColor = Color.BLACK // 输入文字颜色
    private var inputPaddingLeft = 0f // 输入框的左内边距
    private var inputPaddingRight = 0f // 输入框的右内边距
    private var inputPaddingTop = 0f // 输入框的上内边距
    private var inputPaddingBottom = 0f // 输入框的下内边距
    private var inputMarginRight = 0f // 输入框的右外边距
    private var inputHeight = LayoutParams.WRAP_CONTENT // 输入框的高度
    private var inputFocusBg: Drawable = ColorDrawable(Color.TRANSPARENT) // 输入框背景
    private var inputUnFocusBg: Drawable = ColorDrawable(Color.TRANSPARENT) // 输入框背景
    private var inputType = INPUT_TYPE_TEXT // 输入类型
    private var inputMaxLength = -1 // 输入的最大长度
    private var inputMaxLines = -1 // 输入显示的最大行数，当 inputType=INPUT_TYPE_TEXT_MULTI_LINE 有效
    private var inputEllipsize = -1

    // 密码是否可见icon
    private var showEyeIcon = false // 是否显示密码可见icon
    private var openEyeIcon: Drawable? = null // 密码可见icon
    private var closeEyeIcon: Drawable? = null // 密码不可见icon
    private var pwdEyeWidth = LayoutParams.WRAP_CONTENT // 密码可见icon的宽度
    private var pwdEyeHeight = LayoutParams.WRAP_CONTENT // 密码可见icon的高度
    private var isPwdVisible = false // 密码是否可见

    // 文本清除icon
    private var showClearIcon = false // 是否显示文本清除icon
    private var clearIcon: Drawable? = null // 文本清除icon
    private var clearWidth = LayoutParams.WRAP_CONTENT // 文本清除icon的宽度
    private var clearHeight = LayoutParams.WRAP_CONTENT // 文本清除icon的高度

    // 输入框左侧icon
    private var showLeftIcon = false // 是否显示左侧icon
    private var leftIcon: Drawable? = null // 输入框左侧icon
    private var leftIconWidth = LayoutParams.WRAP_CONTENT // 左侧icon宽度
    private var leftIconHeight = LayoutParams.WRAP_CONTENT // 左侧icon高度
    private var leftIconMarginRight = 5.dpToPx // 左侧icon与输入框之间的间距

    // 错误提示
    private var reserveErrorHeight = false // 是否保留错误的高度占位空间
    private var error = "Error" // 错误文本
    private var errorTextColor = Color.parseColor("#E0294A") // 错误文字颜色
    private var errorTextSize = 8.dpToPxF // 错误文字大小
    private var errorMarginTop = 2.dpToPx
    private var errorMarginLeft = 0
    private var errorMarginRight = 0
    private var errorHeight = LayoutParams.WRAP_CONTENT

    // 输入监听
    private var beforeInputChanged: ((et: EditText, s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? =
        null
    private var onInputChanged: ((et: EditText, s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? =
        null
    private var afterInputChanged: ((et: EditText, s: Editable?) -> Unit)? = null
    private var inputFocused: ((et: EditText, hasFocus: Boolean) -> Unit)? = null

    // 密码可见切换监听
    private var passwordVisibleClick: ((et: EditText, isVisible: Boolean) -> Unit)? = null

    // 清除文本监听
    private var clearClick: ((et: EditText) -> Unit)? = null


    constructor(context: Context?) : super(context) {
        init(context!!, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context!!, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context!!, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        initDefaultValue(context)
        initAttr(attrs)
        initView()
        setInputType()
        setInputMonitor()
        setInputEllipsize()
    }

    private fun initDefaultValue(context: Context) {
        clearIcon = R.drawable.fly_uikit_ic_edittext_clear.toDrawable(context)!!
        openEyeIcon = R.drawable.fly_uikit_ic_edittext_eye_close.toDrawable(context)!!
        closeEyeIcon = R.drawable.fly_uikit_ic_edittext_eye_close.toDrawable(context)!!
    }

    private fun initAttr(attrs: AttributeSet?) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.fly_uikit_MultiEditText)
            // 输入框
            input = ta.getString(R.styleable.fly_uikit_MultiEditText_fmet_input) ?: ""
            inputHint = ta.getString(R.styleable.fly_uikit_MultiEditText_fmet_input_hint) ?: ""
            inputHintColor = ta.getColor(
                R.styleable.fly_uikit_MultiEditText_fmet_input_hintColor,
                inputHintColor
            )
            inputMaxLength =
                ta.getInt(R.styleable.fly_uikit_MultiEditText_fmet_input_maxLength, inputMaxLength)
            inputMaxLines =
                ta.getInt(R.styleable.fly_uikit_MultiEditText_fmet_input_maxLines, inputMaxLines)
            inputEllipsize =
                ta.getInt(R.styleable.fly_uikit_MultiEditText_fmet_input_ellipsize, inputEllipsize)
            inputTextSize = ta.getDimension(
                R.styleable.fly_uikit_MultiEditText_fmet_input_textSize,
                inputTextSize
            )
            inputTextColor = ta.getColor(
                R.styleable.fly_uikit_MultiEditText_fmet_input_textColor,
                inputTextColor
            )
            inputPaddingLeft = ta.getDimension(
                R.styleable.fly_uikit_MultiEditText_fmet_input_paddingLeft,
                inputPaddingLeft
            )
            inputPaddingTop = ta.getDimension(
                R.styleable.fly_uikit_MultiEditText_fmet_input_paddingTop,
                inputPaddingTop
            )
            inputPaddingRight = ta.getDimension(
                R.styleable.fly_uikit_MultiEditText_fmet_input_paddingRight,
                inputPaddingRight
            )
            inputPaddingBottom = ta.getDimension(
                R.styleable.fly_uikit_MultiEditText_fmet_input_paddingBottom,
                inputPaddingBottom
            )
            inputMarginRight = ta.getDimension(
                R.styleable.fly_uikit_MultiEditText_fmet_input_marginRight,
                inputMarginRight
            )
            inputHeight = ta.getDimension(
                R.styleable.fly_uikit_MultiEditText_fmet_input_height,
                inputHeight.toFloat()
            ).toInt()
            inputFocusBg =
                ta.getDrawable(R.styleable.fly_uikit_MultiEditText_fmet_input_focusBg)
                    ?: inputFocusBg
            inputUnFocusBg =
                ta.getDrawable(R.styleable.fly_uikit_MultiEditText_fmet_input_unFocusBg)
                    ?: inputUnFocusBg
            inputType = ta.getInt(R.styleable.fly_uikit_MultiEditText_fmet_input_type, inputType)

            // 密码可见icon
            showEyeIcon =
                ta.getBoolean(R.styleable.fly_uikit_MultiEditText_fmet_show_eye, showEyeIcon)
            openEyeIcon = ta.getDrawable(R.styleable.fly_uikit_MultiEditText_fmet_eye_openIcon)
                ?: openEyeIcon
            closeEyeIcon = ta.getDrawable(R.styleable.fly_uikit_MultiEditText_fmet_eye_closeIcon)
                ?: closeEyeIcon
            pwdEyeWidth = ta.getDimension(
                R.styleable.fly_uikit_MultiEditText_fmet_eye_width,
                pwdEyeWidth.toFloat()
            ).toInt()
            pwdEyeHeight = ta.getDimension(
                R.styleable.fly_uikit_MultiEditText_fmet_eye_height,
                pwdEyeHeight.toFloat()
            ).toInt()
            isPwdVisible = ta.getBoolean(
                R.styleable.fly_uikit_MultiEditText_fmet_password_visible,
                isPwdVisible
            )

            // 清除icon
            showClearIcon =
                ta.getBoolean(R.styleable.fly_uikit_MultiEditText_fmet_show_clear, showClearIcon)
            clearIcon =
                ta.getDrawable(R.styleable.fly_uikit_MultiEditText_fmet_clear_icon) ?: clearIcon
            clearWidth = ta.getDimension(
                R.styleable.fly_uikit_MultiEditText_fmet_clear_width,
                clearWidth.toFloat()
            ).toInt()
            clearHeight = ta.getDimension(
                R.styleable.fly_uikit_MultiEditText_fmet_clear_height,
                clearHeight.toFloat()
            ).toInt()

            // 左侧icon
            showLeftIcon =
                ta.getBoolean(R.styleable.fly_uikit_MultiEditText_fmet_show_leftIcon, showLeftIcon)
            leftIcon = ta.getDrawable(R.styleable.fly_uikit_MultiEditText_fmet_leftIcon)
            leftIconWidth = ta.getDimension(
                R.styleable.fly_uikit_MultiEditText_fmet_leftIcon_width,
                leftIconWidth.toFloat()
            ).toInt()
            leftIconHeight = ta.getDimension(
                R.styleable.fly_uikit_MultiEditText_fmet_leftIcon_height,
                leftIconHeight.toFloat()
            ).toInt()
            leftIconMarginRight = ta.getDimension(
                R.styleable.fly_uikit_MultiEditText_fmet_leftIcon_marginRight,
                leftIconMarginRight.toFloat()
            ).toInt()

            // 错误提示
            reserveErrorHeight =
                ta.getBoolean(
                    R.styleable.fly_uikit_MultiEditText_fmet_reserve_errorHeight,
                    reserveErrorHeight
                )
            error = ta.getString(R.styleable.fly_uikit_MultiEditText_fmet_error) ?: "Error"
            errorTextSize =
                ta.getDimension(
                    R.styleable.fly_uikit_MultiEditText_fmet_error_textSize,
                    errorTextSize
                )
            errorTextColor =
                ta.getColor(R.styleable.fly_uikit_MultiEditText_fmet_error_textColor, errorTextColor)
            errorMarginTop =
                ta.getDimension(
                    R.styleable.fly_uikit_MultiEditText_fmet_error_marginTop,
                    errorMarginTop.toFloat()
                )
                    .toInt()
            errorMarginLeft =
                ta.getDimension(
                    R.styleable.fly_uikit_MultiEditText_fmet_error_marginLeft,
                    errorMarginLeft.toFloat()
                ).toInt()
            errorMarginRight =
                ta.getDimension(
                    R.styleable.fly_uikit_MultiEditText_fmet_error_marginRight,
                    errorMarginRight.toFloat()
                ).toInt()
            errorHeight =
                ta.getDimension(
                    R.styleable.fly_uikit_MultiEditText_fmet_error_height,
                    errorHeight.toFloat()
                ).toInt()
            ta.recycle()
        }
    }

    private fun initView() {
        orientation = VERTICAL

        // 输入框
        initInputView()

        // 输入错误提示
        initErrorView()
    }

    private fun initInputView() {
        inputContainer = LinearLayout(context).also {
            val lp = LayoutParams(0, LayoutParams.WRAP_CONTENT)
            lp.weight = 1f
            it.layoutParams = lp
            it.orientation = HORIZONTAL
            it.background = inputUnFocusBg
            it.setPadding(
                inputPaddingLeft.toInt(),
                inputPaddingTop.toInt(),
                inputPaddingRight.toInt(),
                inputPaddingBottom.toInt()
            )
        }
        addView(inputContainer)

        inputContainer.run {
            // 左侧icon
            leftIconView = ImageView(context).also {
                val leftIconLp = LayoutParams(leftIconWidth, leftIconHeight)
                leftIconLp.gravity = Gravity.CENTER_VERTICAL
                leftIconLp.rightMargin = leftIconMarginRight
                it.layoutParams = leftIconLp
                it.adjustViewBounds = true
                it.setImageDrawable(leftIcon)
            }
            inputContainer.addView(leftIconView)

            leftIconView.visibility = if (showLeftIcon) {
                View.VISIBLE
            } else {
                View.GONE
            }

            // 输入框
            inputView = EditText(context).also {
                val etLp = LayoutParams(0, inputHeight)
                etLp.gravity = Gravity.CENTER_VERTICAL
                etLp.weight = 1f
                etLp.rightMargin = inputMarginRight.toInt()
                it.layoutParams = etLp

                it.setBackgroundColor(Color.TRANSPARENT)
                it.setText(input)
                it.setHint(inputHint)
                it.setHintTextColor(inputHintColor)
                it.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputTextSize)
                it.setTextColor(inputTextColor)
                it.gravity = Gravity.CENTER_VERTICAL
                it.setPadding(0, 0, 0, 0)
                if (inputMaxLength >= 0) {
                    it.setFilters(arrayOf<InputFilter>(LengthFilter(inputMaxLength)))
                }
                if (inputMaxLines >= 0) {
                    it.maxLines = inputMaxLines
                }
            }
            addView(inputView)
            // 设置光标为null，即为字体颜色
            try {
                val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
                f.isAccessible = true
                f[inputView] = 0
            } catch (ignored: Exception) {
                //
            }

            // 密码可见icon
            pwdEyeView = ImageView(context).also {
                val eyeLp = LayoutParams(pwdEyeWidth, pwdEyeHeight)
                eyeLp.gravity = (Gravity.CENTER_VERTICAL)
                it.layoutParams = eyeLp
                it.adjustViewBounds = true
                if (isPwdVisible) {
                    it.setImageDrawable(openEyeIcon)
                } else {
                    it.setImageDrawable(closeEyeIcon)
                }
            }
            addView(pwdEyeView)
            pwdEyeView.visibility = if (showEyeIcon) {
                View.VISIBLE
            } else {
                View.GONE
            }

            //  清除文本icon
            clearView = ImageView(context).also {
                val clearLp = LayoutParams(clearWidth, clearHeight)
                clearLp.gravity = (Gravity.CENTER_VERTICAL)
                it.layoutParams = clearLp
                it.adjustViewBounds = true
                it.setImageDrawable(clearIcon)
            }
            addView(clearView)
            clearView.visibility = if (showClearIcon && !input.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun initErrorView() {
        errorView = TextView(context).also {
            val lp = LayoutParams(LayoutParams.WRAP_CONTENT, errorHeight)
            lp.topMargin = errorMarginTop
            lp.leftMargin = errorMarginLeft
            lp.rightMargin = errorMarginRight
            it.layoutParams = lp
            it.text = error
            it.setTextSize(TypedValue.COMPLEX_UNIT_PX, errorTextSize)
            it.setTextColor(errorTextColor)
        }
        addView(errorView)
        errorView.visibility = if (reserveErrorHeight) {
            View.INVISIBLE
        } else {
            View.GONE
        }
    }

    /**
     * 设置输入类型
     */
    private fun setInputType() {
        when (inputType) {
            // 不设置
            INPUT_TYPE_NONE -> {
                inputView.inputType = InputType.TYPE_NULL
            }
            // 纯文本
            INPUT_TYPE_TEXT -> {
                inputView.inputType = InputType.TYPE_CLASS_TEXT
            }
            // 文本密码
            INPUT_TYPE_TEXT_PASSWORD -> {
                inputView.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            // 纯数字
            INPUT_TYPE_NUMBER -> {
                inputView.inputType = InputType.TYPE_CLASS_NUMBER
            }
            // 带小数点的数字
            INPUT_TYPE_NUMBER_DECIMAL -> {
                inputView.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            }
            // 数字密码
            INPUT_TYPE_NUMBER_PASSWORD -> {
                inputView.inputType = TYPE_CLASS_NUMBER or TYPE_NUMBER_VARIATION_PASSWORD
            }
            // 带正负符号的数字
            INPUT_TYPE_NUMBER_SIGNED -> {
                inputView.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED
            }
            // 多行输入
            INPUT_TYPE_TEXT_MULTI_LINE -> {
                inputView.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
            }
        }
        if (inputType == INPUT_TYPE_TEXT_PASSWORD || inputType == INPUT_TYPE_NUMBER_PASSWORD) {
            if (isPwdVisible) {
                setPasswordVisibale()
            } else {
                setPasswordNotVisibale()
            }
        }
    }

    /**
     * 设置输入监听
     */
    private fun setInputMonitor() {
        inputView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                beforeInputChanged?.invoke(inputView, s, start, count, after)
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                onInputChanged?.invoke(inputView, s, start, before, count)
            }

            override fun afterTextChanged(s: Editable?) {
                if (showClearIcon) {
                    if (inputView.text.toString().isNotEmpty()) {
                        clearView.visibility = View.VISIBLE
                    } else {
                        clearView.visibility = View.GONE
                    }
                } else {
                    if (clearView.visibility != View.GONE) {
                        clearView.visibility = View.GONE
                    }
                }
                afterInputChanged?.invoke(inputView, s)
            }
        })
        inputView.setOnFocusChangeListener { v, hasFocus ->
            inputContainer.background = if (hasFocus) {
                inputFocusBg
            } else {
                inputUnFocusBg
            }
        }
        pwdEyeView.setOnClickListener {
            togglePasswordVisible()
            passwordVisibleClick?.invoke(inputView, isPwdVisible)
        }
        clearView.setOnClickListener {
            clearText()
            clearClick?.invoke(inputView)
        }
    }


    private fun setInputEllipsize() {
        if (inputEllipsize > 0) {
            when (inputEllipsize) {
                INPUT_ELLIPSIZE_START -> {
                    inputView.setEllipsize(TextUtils.TruncateAt.START)
                }
                INPUT_ELLIPSIZE_END -> {
                    inputView.setEllipsize(TextUtils.TruncateAt.END)
                }
                INPUT_ELLIPSIZE_MIDDLE -> {
                    inputView.setEllipsize(TextUtils.TruncateAt.MIDDLE)
                }
                INPUT_ELLIPSIZE_MARQUEE -> {
                    inputView.setEllipsize(TextUtils.TruncateAt.MARQUEE)
                }
            }
        }
    }

    fun getInputView(): EditText {
        return inputView
    }

    fun getLeftIconView(): ImageView {
        return leftIconView
    }

    fun getPasswordVisibleView(): ImageView {
        return pwdEyeView
    }

    fun getClearView(): ImageView {
        return clearView
    }

    fun getErrorView(): TextView {
        return errorView
    }

    /**
     * 控制密码是否可见
     */
    fun togglePasswordVisible() {
        if (inputType == INPUT_TYPE_TEXT_PASSWORD || inputType == INPUT_TYPE_NUMBER_PASSWORD) {
            if (isPwdVisible) {
                setPasswordNotVisibale()
            } else {
                setPasswordVisibale()
            }
            isPwdVisible = !isPwdVisible
        }
    }

    private fun setPasswordVisibale() {
        inputView.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
        inputView.setSelection(inputView.getText().length)
        pwdEyeView.setImageDrawable(openEyeIcon)
    }

    private fun setPasswordNotVisibale() {
        inputView.setTransformationMethod(PasswordTransformationMethod.getInstance())
        inputView.setSelection(inputView.getText().length)
        pwdEyeView.setImageDrawable(closeEyeIcon)
    }

    /**
     * 密码是否可见
     */
    fun isPasswordVisible(): Boolean {
        return isPwdVisible
    }

    /**
     * 清除文本
     */
    fun clearText() {
        inputView.setText("")
    }

    /**
     * 设置输入内容
     */
    fun setInput(content: CharSequence): MultiEditText {
        inputView.setText(content)
        return this
    }

    fun getInput(): String {
        return inputView.text.toString()
    }

    fun setError(text: String): MultiEditText {
        errorView.text = text
        return this
    }

    fun showError(isShow: Boolean) {
        errorView.visibility = if (isShow) {
            View.VISIBLE
        } else {
            if (reserveErrorHeight) {
                View.INVISIBLE
            } else {
                View.GONE
            }
        }
    }

    fun doBeforeInputChanged(block: ((et: EditText, s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)?): MultiEditText {
        this.beforeInputChanged = block
        return this
    }

    fun doOnInputChanged(block: ((et: EditText, s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)?): MultiEditText {
        this.onInputChanged = block
        return this
    }

    fun doAfterInputChanged(block: ((et: EditText, s: Editable?) -> Unit)?): MultiEditText {
        this.afterInputChanged = block
        return this
    }

    fun doInputFocused(block: ((et: EditText, hasFocus: Boolean) -> Unit)?): MultiEditText {
        this.inputFocused = block
        return this
    }

    fun doPasswordVisibleClick(block: ((et: EditText, isVisible: Boolean) -> Unit)?): MultiEditText {
        this.passwordVisibleClick = block
        return this
    }

    fun doClearClick(block: ((et: EditText) -> Unit)?): MultiEditText {
        this.clearClick = block
        return this
    }

    /**
     * 粘贴文本
     */
    fun paste(): String {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val pasteData = cm.text.toString()
        return pasteData
    }

    /**
     * 只粘贴数字
     */
    fun pasteNumber(): String {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val pasteData = cm.text.toString()
        // 过滤掉所有非数字字符
        val result = pasteData.replace("[^\\d]".toRegex(), "")
        return result
    }
}