package com.halimjr11.luma.ui.components

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.color.MaterialColors
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.halimjr11.luma.R
import com.halimjr11.luma.data.utils.orZero
import com.google.android.material.R as MaterialRes

class LumaInputField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    private val editTextField = TextInputEditText(context)
    private var inputTypeMode: InputTypeMode = InputTypeMode.TEXT
    private var enableAutoValidate = true

    enum class InputTypeMode { TEXT, USERNAME, EMAIL, PASSWORD }

    init {
        addView(editTextField)
        setupEditText()
        ViewCompat.setBackgroundTintList(this, null)
        context.theme.applyStyle(R.style.Widget_Luma_TextInputLayout, true)
        context.theme.obtainStyledAttributes(attrs, R.styleable.LumaInputField, 0, 0).apply {
            try {
                val modeValue = getInt(R.styleable.LumaInputField_inputTypeMode, 0)
                inputTypeMode = when (modeValue) {
                    1 -> InputTypeMode.EMAIL
                    2 -> InputTypeMode.PASSWORD
                    else -> InputTypeMode.USERNAME
                }

                val customHint = getString(R.styleable.LumaInputField_hintText)
                enableAutoValidate = getBoolean(R.styleable.LumaInputField_enableAutoValidate, true)
                setBoxCornerRadii(12f, 0f, 0f, 12f)
                setupInputType()
                if (!customHint.isNullOrEmpty()) hint = customHint
            } finally {
                recycle()
            }
        }
    }

    private fun setupInputType() {
        when (inputTypeMode) {
            InputTypeMode.USERNAME -> {
                if (hint.isNullOrEmpty()) hint = context.getString(R.string.hint_username)
                editTextField.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                endIconMode = END_ICON_NONE
            }

            InputTypeMode.EMAIL -> {
                if (hint.isNullOrEmpty()) hint = context.getString(R.string.hint_email)
                editTextField.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                endIconMode = END_ICON_NONE
            }

            InputTypeMode.PASSWORD -> {
                if (hint.isNullOrEmpty()) hint = context.getString(R.string.hint_password)
                editTextField.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                endIconMode = END_ICON_PASSWORD_TOGGLE
            }

            else -> {
                editTextField.inputType = InputType.TYPE_CLASS_TEXT
                endIconMode = END_ICON_NONE
            }
        }
    }

    private fun setupEditText() {
        editTextField.layoutParams = FrameLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

        editTextField.setTextAppearance(R.style.TextAppearance_Luma_Body)
        editTextField.setBackgroundResource(0)
        editTextField.textSize = 16f
        editTextField.isSingleLine = true
        editTextField.setPadding(0, 24, 0, 24)
        editTextField.setTextColor(
            MaterialColors.getColor(
                editTextField,
                MaterialRes.attr.colorOnSurface
            )
        )
        editTextField.setHintTextColor(
            MaterialColors.getColor(
                editTextField,
                MaterialRes.attr.colorOnSurfaceVariant
            )
        )

        if (enableAutoValidate) {
            editTextField.doOnTextChanged { text, _, _, _ ->
                if (text?.length.orZero() > 0) {
                    validate(text.toString())
                }
            }
        }
    }

    private fun validate(input: String) {
        when (inputTypeMode) {
            InputTypeMode.USERNAME -> {
                error = if (input.length < 3)
                    context.getString(R.string.error_username_short)
                else null
            }

            InputTypeMode.EMAIL -> {
                val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()
                error = if (!isValid && input.isNotEmpty())
                    context.getString(R.string.error_email_invalid)
                else null
            }

            InputTypeMode.PASSWORD -> {
                error = if (input.length < 8)
                    context.getString(R.string.error_password_short)
                else null
            }

            else -> Unit
        }
    }

    fun getText(): String {
        return editTextField.text?.toString().orEmpty().also {
            validate(it)
        }
    }
}
