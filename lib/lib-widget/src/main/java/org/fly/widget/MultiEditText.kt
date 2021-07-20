package org.fly.widget

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
import org.fly.base.extensions.dpToPx
import org.fly.base.extensions.singleClick


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


    private lateinit var tv_title: TextView // 标题
    private lateinit var ll_inputRow: LinearLayout // 输入框所在行
    private lateinit var ll_input: LinearLayout // 输入框所在容器
    private lateinit var et_input: EditText // 输入框
    private lateinit var iv_leftIcon: ImageView // 输入框左侧icon
    private lateinit var iv_pwdEye: ImageView // 密码可见icon
    private lateinit var iv_clear: ImageView // 文本清除icon
    private lateinit var tv_rightBtn: TextView // 最右边的按钮
    private lateinit var tv_error: TextView // 错误提示

    /**
     * 属性
     */
    // 标题
    private var showTitle = true
    private var title = "" // 标题
    private var titleTextSize = 14.dpToPx.toFloat() // 标题文字大小
    private var titleTextColor = Color.BLACK // 标题颜色
    private var titleFocusTextColor = Color.parseColor("#F2B90B") // 输入框获取焦点时，标题的颜色
    private var titleMarginBottom = 8.dpToPx.toFloat() // 标题与输入框之间的间距
    private var isTitleBold = false // 标题是否加粗


    // 输入框
    private var input = "" // 输入内容
    private var inputHint = ""// 输入提示
    private var inputHintColor = Color.GRAY // 输入提示的文字颜色
    private var inputTextSize = 14.dpToPx.toFloat() // 输入文字大小
    private var inputTextColor = Color.BLACK // 输入文字颜色
    private var inputPaddingLeft = 0f // 输入框的左内边距
    private var inputPaddingRight = 0f // 输入框的右内边距
    private var inputPaddingTop = 0f // 输入框的上内边距
    private var inputPaddingBottom = 0f // 输入框的下内边距
    private var inputMarginRight = 0f // 输入框的右外边距
    private var inputRowPaddingLeft = 0f // 输入框所在行的左内边距
    private var inputRowPaddingRight = 0f // 输入框所在行的右内边距
    private var inputRowPaddingTop = 0f // 输入框所在行的上内边距
    private var inputRowPaddingBottom = 0f // 输入框所在行的下内边距
    private var inputHeight = LayoutParams.WRAP_CONTENT // 输入框的高度
    private var inputFocusBg: Drawable = ColorDrawable(Color.TRANSPARENT) // 输入框背景
    private var inputUnFocusBg: Drawable = ColorDrawable(Color.TRANSPARENT) // 输入框背景
    private var inputRowFocusBg: Drawable = ColorDrawable(Color.TRANSPARENT) // 输入框所在行的背景
    private var inputRowUnFocusBg: Drawable = ColorDrawable(Color.TRANSPARENT) // 输入框所在行的背景
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
    private var leftIconMarginRight = 5.dpToPx.toInt() // 左侧icon与输入框之间的间距

    // 右侧按钮
    private var showRightBtn = false
    private var rightBtnWidth = LayoutParams.WRAP_CONTENT
    private var rightBtnHeight = LayoutParams.MATCH_PARENT
    private var rightBtnText = "" // 右侧按钮文字
    private var rightBtnTextColor = Color.BLACK // 右侧按钮文字颜色
    private var rightBtnTextSize = 12.dpToPx.toFloat() // 右侧按钮文字大小
    private var isRightTextBold = false // 右侧按钮文字是否加粗
    private var rightBtnBg: Drawable? = null // 右侧按钮背景
    private var rightBtnMarginLeft = 5.dpToPx.toInt()
    private var rightBtnPaddingLeft = 0
    private var rightBtnPaddingRight = 0
    private var rightBtnPaddingTop = 0
    private var rightBtnPaddingBottom = 0

    // 错误提示
    private var reserveErrorHeight = false // 是否保留错误的高度占位空间
    private var error = "Error" // 错误文本
    private var errorTextColor = Color.parseColor("#E0294A") // 错误文字颜色
    private var errorTextSize = 8.dpToPx.toFloat() // 错误文字大小
    private var errorMarginTop = 2.dpToPx.toInt()
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
        initAttr(attrs)
        initView(context)
        setInputType()
        setInputMonitor()
        setInputEllipsize()
    }

    private fun initAttr(attrs: AttributeSet?) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.fly_MultiEditText)
            // 标题
            showTitle = ta.getBoolean(R.styleable.fly_MultiEditText_met_show_title, showTitle)
            title = ta.getString(R.styleable.fly_MultiEditText_met_title) ?: ""
            titleTextSize = ta.getDimension(
                R.styleable.fly_MultiEditText_met_title_textSize,
                titleTextSize
            )
            titleTextColor = ta.getColor(
                R.styleable.fly_MultiEditText_met_title_textColor,
                titleTextColor
            )
            titleFocusTextColor =
                ta.getColor(
                    R.styleable.fly_MultiEditText_met_title_focusTextColor,
                    titleFocusTextColor
                )
            isTitleBold =
                ta.getBoolean(
                    R.styleable.fly_MultiEditText_met_title_bold,
                    isTitleBold
                )
            titleMarginBottom = ta.getDimension(
                R.styleable.fly_MultiEditText_met_title_marginBottom,
                titleMarginBottom
            )

            // 输入框
            input = ta.getString(R.styleable.fly_MultiEditText_met_input) ?: ""
            inputHint = ta.getString(R.styleable.fly_MultiEditText_met_input_hint) ?: ""
            inputHintColor = ta.getColor(
                R.styleable.fly_MultiEditText_met_input_hintColor,
                inputHintColor
            )
            inputMaxLength =
                ta.getInt(R.styleable.fly_MultiEditText_met_input_maxLength, inputMaxLength)
            inputMaxLines =
                ta.getInt(R.styleable.fly_MultiEditText_met_input_maxLines, inputMaxLines)
            inputEllipsize =
                ta.getInt(R.styleable.fly_MultiEditText_met_input_ellipsize, inputEllipsize)
            inputTextSize = ta.getDimension(
                R.styleable.fly_MultiEditText_met_input_textSize,
                inputTextSize
            )
            inputTextColor = ta.getColor(
                R.styleable.fly_MultiEditText_met_input_textColor,
                inputTextColor
            )
            inputPaddingLeft = ta.getDimension(
                R.styleable.fly_MultiEditText_met_input_paddingLeft,
                inputPaddingLeft
            )
            inputPaddingTop = ta.getDimension(
                R.styleable.fly_MultiEditText_met_input_paddingTop,
                inputPaddingTop
            )
            inputPaddingRight = ta.getDimension(
                R.styleable.fly_MultiEditText_met_input_paddingRight,
                inputPaddingRight
            )
            inputPaddingBottom = ta.getDimension(
                R.styleable.fly_MultiEditText_met_input_paddingBottom,
                inputPaddingBottom
            )
            inputMarginRight = ta.getDimension(
                R.styleable.fly_MultiEditText_met_input_marginRight,
                inputMarginRight
            )
            inputRowPaddingLeft = ta.getDimension(
                R.styleable.fly_MultiEditText_met_input_rowPaddingLeft,
                inputRowPaddingLeft
            )
            inputRowPaddingRight = ta.getDimension(
                R.styleable.fly_MultiEditText_met_input_rowPaddingRight,
                inputRowPaddingRight
            )
            inputRowPaddingTop = ta.getDimension(
                R.styleable.fly_MultiEditText_met_input_rowPaddingTop,
                inputRowPaddingTop
            )
            inputRowPaddingBottom = ta.getDimension(
                R.styleable.fly_MultiEditText_met_input_rowPaddingBottom,
                inputRowPaddingBottom
            )
            inputHeight = ta.getDimension(
                R.styleable.fly_MultiEditText_met_input_height,
                inputHeight.toFloat()
            ).toInt()
            inputFocusBg =
                ta.getDrawable(R.styleable.fly_MultiEditText_met_input_focusBg) ?: inputFocusBg
            inputUnFocusBg =
                ta.getDrawable(R.styleable.fly_MultiEditText_met_input_unFocusBg) ?: inputUnFocusBg
            inputRowFocusBg =
                ta.getDrawable(R.styleable.fly_MultiEditText_met_input_rowFocusBg)
                    ?: inputRowFocusBg
            inputRowUnFocusBg =
                ta.getDrawable(R.styleable.fly_MultiEditText_met_input_rowUnFocusBg)
                    ?: inputRowUnFocusBg
            inputType = ta.getInt(R.styleable.fly_MultiEditText_met_input_type, inputType)

            // 密码可见icon
            showEyeIcon = ta.getBoolean(R.styleable.fly_MultiEditText_met_show_eye, showEyeIcon)
            openEyeIcon = ta.getDrawable(R.styleable.fly_MultiEditText_met_eye_open_icon)
            closeEyeIcon = ta.getDrawable(R.styleable.fly_MultiEditText_met_eye_close_icon)
            pwdEyeWidth = ta.getDimension(
                R.styleable.fly_MultiEditText_met_eye_width,
                pwdEyeWidth.toFloat()
            ).toInt()
            pwdEyeHeight = ta.getDimension(
                R.styleable.fly_MultiEditText_met_eye_height,
                pwdEyeHeight.toFloat()
            ).toInt()
            isPwdVisible = ta.getBoolean(
                R.styleable.fly_MultiEditText_met_password_visible,
                isPwdVisible
            )

            // 清除icon
            showClearIcon =
                ta.getBoolean(R.styleable.fly_MultiEditText_met_show_clear, showClearIcon)
            clearIcon = ta.getDrawable(R.styleable.fly_MultiEditText_met_clear_icon)
            clearWidth = ta.getDimension(
                R.styleable.fly_MultiEditText_met_clear_width,
                clearWidth.toFloat()
            ).toInt()
            clearHeight = ta.getDimension(
                R.styleable.fly_MultiEditText_met_clear_height,
                clearHeight.toFloat()
            ).toInt()

            // 左侧icon
            showLeftIcon =
                ta.getBoolean(R.styleable.fly_MultiEditText_met_show_leftIcon, showLeftIcon)
            leftIcon = ta.getDrawable(R.styleable.fly_MultiEditText_met_leftIcon)
            leftIconWidth = ta.getDimension(
                R.styleable.fly_MultiEditText_met_leftIcon_width,
                leftIconWidth.toFloat()
            ).toInt()
            leftIconHeight = ta.getDimension(
                R.styleable.fly_MultiEditText_met_leftIcon_height,
                leftIconHeight.toFloat()
            ).toInt()
            leftIconMarginRight = ta.getDimension(
                R.styleable.fly_MultiEditText_met_leftIcon_marginRight,
                leftIconMarginRight.toFloat()
            ).toInt()

            // 右侧按钮
            showRightBtn = ta.getBoolean(R.styleable.fly_MultiEditText_met_show_rBtn, showRightBtn)
            rightBtnWidth = ta.getDimension(
                R.styleable.fly_MultiEditText_met_rBtn_width,
                rightBtnWidth.toFloat()
            ).toInt()
            rightBtnHeight = ta.getDimension(
                R.styleable.fly_MultiEditText_met_rBtn_height,
                rightBtnHeight.toFloat()
            ).toInt()
            rightBtnText = ta.getString(R.styleable.fly_MultiEditText_met_rBtn_text) ?: ""
            rightBtnTextColor =
                ta.getColor(R.styleable.fly_MultiEditText_met_rBtn_textColor, rightBtnTextColor)
            rightBtnTextSize =
                ta.getDimension(R.styleable.fly_MultiEditText_met_rBtn_textSize, rightBtnTextSize)
            isRightTextBold =
                ta.getBoolean(
                    R.styleable.fly_MultiEditText_met_rBtn_bold,
                    isRightTextBold
                )
            rightBtnBg = ta.getDrawable(R.styleable.fly_MultiEditText_met_rBtn_bg) ?: rightBtnBg
            rightBtnMarginLeft =
                ta.getDimension(
                    R.styleable.fly_MultiEditText_met_rBtn_marginLeft,
                    rightBtnMarginLeft.toFloat()
                ).toInt()
            rightBtnPaddingLeft =
                ta.getDimension(
                    R.styleable.fly_MultiEditText_met_rBtn_paddingLeft,
                    rightBtnPaddingLeft.toFloat()
                ).toInt()
            rightBtnPaddingRight =
                ta.getDimension(
                    R.styleable.fly_MultiEditText_met_rBtn_paddingRight,
                    rightBtnPaddingRight.toFloat()
                ).toInt()
            rightBtnPaddingTop =
                ta.getDimension(
                    R.styleable.fly_MultiEditText_met_rBtn_paddingTop,
                    rightBtnPaddingTop.toFloat()
                ).toInt()
            rightBtnPaddingBottom =
                ta.getDimension(
                    R.styleable.fly_MultiEditText_met_rBtn_paddingBottom,
                    rightBtnPaddingBottom.toFloat()
                ).toInt()
            reserveErrorHeight =
                ta.getBoolean(
                    R.styleable.fly_MultiEditText_met_reserve_errorHeight,
                    reserveErrorHeight
                )
            error = ta.getString(R.styleable.fly_MultiEditText_met_error) ?: "Error"
            errorTextSize =
                ta.getDimension(R.styleable.fly_MultiEditText_met_error_textSize, errorTextSize)
            errorTextColor =
                ta.getColor(R.styleable.fly_MultiEditText_met_error_textColor, errorTextColor)
            errorMarginTop =
                ta.getDimension(
                    R.styleable.fly_MultiEditText_met_error_marginTop,
                    errorMarginTop.toFloat()
                )
                    .toInt()
            errorMarginLeft =
                ta.getDimension(
                    R.styleable.fly_MultiEditText_met_error_marginLeft,
                    errorMarginLeft.toFloat()
                ).toInt()
            errorMarginRight =
                ta.getDimension(
                    R.styleable.fly_MultiEditText_met_error_marginRight,
                    errorMarginRight.toFloat()
                ).toInt()
            errorHeight =
                ta.getDimension(
                    R.styleable.fly_MultiEditText_met_error_height,
                    errorHeight.toFloat()
                ).toInt()
            ta.recycle()
        }
    }

    private fun initView(context: Context) {
        orientation = VERTICAL
        // 标题
        initTitleView()

        ll_inputRow = LinearLayout(context).also {
            val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            it.layoutParams = lp
            it.orientation = HORIZONTAL
            it.background = inputRowUnFocusBg
            it.setPadding(
                inputRowPaddingLeft.toInt(),
                inputRowPaddingTop.toInt(),
                inputRowPaddingRight.toInt(),
                inputRowPaddingBottom.toInt()
            )
        }
        addView(ll_inputRow)

        // 输入框
        initInputView()

        // 输入框右侧按钮
        initRightBtnView()

        // 输入错误提示
        initErrorView()
    }

    private fun initTitleView() {
        tv_title = TextView(context).also {
            val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            lp.bottomMargin = titleMarginBottom.toInt()
            it.layoutParams = lp
            it.text = title
            it.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize)
            it.setTextColor(titleTextColor)
            val tp: TextPaint = it.getPaint()
            tp.isFakeBoldText = isTitleBold
        }
        addView(tv_title)
        tv_title.visibility = if (showTitle) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun initInputView() {
        ll_input = LinearLayout(context).also {
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
        ll_inputRow.addView(ll_input)

        ll_input.run {
            // 左侧icon
            iv_leftIcon = ImageView(context).also {
                val leftIconLp = LayoutParams(leftIconWidth, leftIconHeight)
                leftIconLp.gravity = Gravity.CENTER_VERTICAL
                leftIconLp.rightMargin = leftIconMarginRight
                it.layoutParams = leftIconLp
                it.adjustViewBounds = true
                it.setImageDrawable(leftIcon)
            }
            ll_input.addView(iv_leftIcon)

            iv_leftIcon.visibility = if (showLeftIcon) {
                View.VISIBLE
            } else {
                View.GONE
            }

            // 输入框
            et_input = EditText(context).also {
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
            addView(et_input)
            // 设置光标为null，即为字体颜色
            try {
                val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
                f.isAccessible = true
                f[et_input] = 0
            } catch (ignored: Exception) {
                //
            }

            // 密码可见icon
            iv_pwdEye = ImageView(context).also {
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
            addView(iv_pwdEye)
            iv_pwdEye.visibility = if (showEyeIcon) {
                View.VISIBLE
            } else {
                View.GONE
            }

            //  清除文本icon
            iv_clear = ImageView(context).also {
                val clearLp = LayoutParams(clearWidth, clearHeight)
                clearLp.gravity = (Gravity.CENTER_VERTICAL)
                it.layoutParams = clearLp
                it.adjustViewBounds = true
                it.setImageDrawable(clearIcon)
            }
            addView(iv_clear)
            iv_clear.visibility = if (showClearIcon && !input.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun initRightBtnView() {
        tv_rightBtn = TextView(context).also {
            val rightBtnLp = LayoutParams(rightBtnWidth, rightBtnHeight)
            rightBtnLp.gravity = Gravity.CENTER_VERTICAL
            rightBtnLp.leftMargin = rightBtnMarginLeft
            it.layoutParams = rightBtnLp
            it.gravity = Gravity.CENTER
            it.text = rightBtnText
            it.setTextColor(rightBtnTextColor)
            it.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightBtnTextSize)
            it.background = rightBtnBg
            it.setPadding(
                rightBtnPaddingLeft,
                rightBtnPaddingTop,
                rightBtnPaddingRight,
                rightBtnPaddingBottom
            )
            val tp: TextPaint = it.getPaint()
            tp.isFakeBoldText = isRightTextBold
        }
        ll_inputRow.addView(tv_rightBtn)
        tv_rightBtn.visibility = if (showRightBtn) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun initErrorView() {
        tv_error = TextView(context).also {
            val lp = LayoutParams(LayoutParams.WRAP_CONTENT, errorHeight)
            lp.topMargin = errorMarginTop
            lp.leftMargin = errorMarginLeft
            lp.rightMargin = errorMarginRight
            it.layoutParams = lp
            it.text = error
            it.setTextSize(TypedValue.COMPLEX_UNIT_PX, errorTextSize)
            it.setTextColor(errorTextColor)
        }
        addView(tv_error)
        tv_error.visibility = if (reserveErrorHeight) {
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
                et_input.inputType = InputType.TYPE_NULL
            }
            // 纯文本
            INPUT_TYPE_TEXT -> {
                et_input.inputType = InputType.TYPE_CLASS_TEXT
            }
            // 文本密码
            INPUT_TYPE_TEXT_PASSWORD -> {
                et_input.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            // 纯数字
            INPUT_TYPE_NUMBER -> {
                et_input.inputType = InputType.TYPE_CLASS_NUMBER
            }
            // 带小数点的数字
            INPUT_TYPE_NUMBER_DECIMAL -> {
                et_input.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            }
            // 数字密码
            INPUT_TYPE_NUMBER_PASSWORD -> {
                et_input.inputType = TYPE_CLASS_NUMBER or TYPE_NUMBER_VARIATION_PASSWORD
            }
            // 带正负符号的数字
            INPUT_TYPE_NUMBER_SIGNED -> {
                et_input.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED
            }
            // 多行输入
            INPUT_TYPE_TEXT_MULTI_LINE -> {
                et_input.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
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
        et_input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                beforeInputChanged?.invoke(et_input, s, start, count, after)
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                onInputChanged?.invoke(et_input, s, start, before, count)
            }

            override fun afterTextChanged(s: Editable?) {
                if (showClearIcon) {
                    if (et_input.text.toString().isNotEmpty()) {
                        iv_clear.visibility = View.VISIBLE
                    } else {
                        iv_clear.visibility = View.GONE
                    }
                } else {
                    if (iv_clear.visibility != View.GONE) {
                        iv_clear.visibility = View.GONE
                    }
                }
                afterInputChanged?.invoke(et_input, s)
            }
        })
        et_input.setOnFocusChangeListener { v, hasFocus ->
            if (tv_title.visibility == View.VISIBLE) {
                tv_title.setTextColor(
                    if (hasFocus) {
                        titleFocusTextColor
                    } else {
                        titleTextColor
                    }
                )
            }
            ll_input.background = if (hasFocus) {
                inputFocusBg
            } else {
                inputUnFocusBg
            }
            ll_inputRow.background = if (hasFocus) {
                inputRowFocusBg
            } else {
                inputRowUnFocusBg
            }
        }
        iv_pwdEye.singleClick {
            togglePasswordVisible()
            passwordVisibleClick?.invoke(et_input, isPwdVisible)
        }
        iv_clear.singleClick {
            clearText()
            clearClick?.invoke(et_input)
        }
    }


    private fun setInputEllipsize() {
        if (inputEllipsize > 0) {
            when (inputEllipsize) {
                INPUT_ELLIPSIZE_START -> {
                    et_input.setEllipsize(TextUtils.TruncateAt.START)
                }
                INPUT_ELLIPSIZE_END -> {
                    et_input.setEllipsize(TextUtils.TruncateAt.END)
                }
                INPUT_ELLIPSIZE_MIDDLE -> {
                    et_input.setEllipsize(TextUtils.TruncateAt.MIDDLE)
                }
                INPUT_ELLIPSIZE_MARQUEE -> {
                    et_input.setEllipsize(TextUtils.TruncateAt.MARQUEE)
                }
            }
        }
    }

    fun getTitleView(): TextView {
        return tv_title
    }

    fun getInputView(): EditText {
        return et_input
    }

    fun getInputRowView(): LinearLayout {
        return ll_inputRow
    }

    fun getLeftIconView(): ImageView {
        return iv_leftIcon
    }

    fun getPasswordVisibleView(): ImageView {
        return iv_pwdEye
    }

    fun getClearView(): ImageView {
        return iv_clear
    }

    fun getRightButtonView(): TextView {
        return tv_rightBtn
    }

    fun getErrorView(): TextView {
        return tv_error
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
        et_input.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
        et_input.setSelection(et_input.getText().length)
        iv_pwdEye.setImageDrawable(openEyeIcon)
    }

    private fun setPasswordNotVisibale() {
        et_input.setTransformationMethod(PasswordTransformationMethod.getInstance())
        et_input.setSelection(et_input.getText().length)
        iv_pwdEye.setImageDrawable(closeEyeIcon)
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
        et_input.setText("")
    }

    /**
     * 设置输入内容
     */
    fun setInput(content: CharSequence): MultiEditText {
        et_input.setText(content)
        return this
    }

    fun getInput(): String {
        return et_input.text.toString()
    }

    fun setError(text: String): MultiEditText {
        tv_error.text = text
        return this
    }

    fun showError(isShow: Boolean) {
        tv_error.visibility = if (isShow) {
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

    fun doRightButtonClick(block: ((et: EditText, btn: TextView) -> Unit)?): MultiEditText {
        tv_rightBtn.singleClick {
            block?.invoke(et_input, it)
        }
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