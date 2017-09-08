package com.davidlin54.ilovezappos;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import java.math.BigDecimal;

/**
 * Created by david.lin1ibm.com on 2017-09-08.
 */

public class CurrencyEditText extends android.support.v7.widget.AppCompatEditText {


    public CurrencyEditText(Context context) {
        super(context);
    }

    public CurrencyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CurrencyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setText("0.00");
        addTextChangedListener(new CurrencyTextWatcher(this));
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        setSelection(getText().toString().length());
    }

    private class CurrencyTextWatcher implements TextWatcher {
        private EditText editText;
        private final BigDecimal HUNDRED = new BigDecimal(100);

        public CurrencyTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            editText.removeTextChangedListener(this);

            String[] decimal = editable.toString().split("\\.");

            if (decimal.length != 2) return;

            BigDecimal value = null;
            // deleted value
            if (decimal[1].length() < 2) {
                value = new BigDecimal(editable.toString());
                value = value.divide(BigDecimal.TEN);
            } else if (decimal[1].length() > 2) {
                value = new BigDecimal(editable.toString().substring(0, editable.toString().length() - 1));
                value = value.multiply(BigDecimal.TEN);
                value = value.add(new BigDecimal(editable.toString().substring(editable.toString().length() - 1)).divide(HUNDRED));
            } else {
                value = new BigDecimal(editable.toString());
            }

            editText.setText(String.format("%.2f", value));

            editText.addTextChangedListener(this);
        }
    }
}
