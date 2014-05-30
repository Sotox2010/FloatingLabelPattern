package com.example.floatinglabelpattern.widget;

import com.example.floatinglabelpattern.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class FloatingLabelEditText extends FloatingLabelControl<EditText> {

    private static final String TAG = "FloatingLabelEditText";

    private static final int DEFAULT_TEXT_SIZE = 18;

    private EditText mEditText = (EditText) getControlView();

    private String mHint;
    private String mText;
    private int mTextSize;
    private int mTextAppearance;

    public FloatingLabelEditText(Context context) {
        this(context, null);
    }

    public FloatingLabelEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.floatingLabelEditTextStyle);
    }

    public FloatingLabelEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.FloatingLabelEditText, defStyle, 0);

            mHint = a.getString(R.styleable.FloatingLabelEditText_android_hint);
            mText = a.getString(R.styleable.FloatingLabelEditText_android_text);

            mTextAppearance = a.getResourceId(
                    R.styleable.FloatingLabelEditText_android_textAppearance, -1);

            if (mTextAppearance != -1) {
                mEditText.setTextAppearance(context, mTextAppearance);
            }

            mTextSize = a.getDimensionPixelSize(
                    R.styleable.FloatingLabelEditText_android_textSize, DEFAULT_TEXT_SIZE);

            a.recycle();
        }

        mEditText.setText(mText);
        mEditText.setHint(mHint);
        mEditText.setTextSize(mTextSize);
        setFloatingLabelText(mHint);

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setFloatingLabelFocused(hasFocus);
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    slideUpToTop(true);
                } else {
                    slideDownToBottom(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }

    @Override
    protected void inflateControlView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.floating_label_edit_text_merge, this);
    }

}
