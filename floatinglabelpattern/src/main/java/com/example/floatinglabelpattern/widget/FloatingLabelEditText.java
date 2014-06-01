package com.example.floatinglabelpattern.widget;

import com.example.floatinglabelpattern.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class FloatingLabelEditText extends FloatingLabelControl<EditText> {

    private static final String TAG = "FloatingLabelEditText";

    /**
     * The default main EditText text size.
     */
    private static final int DEFAULT_TEXT_SIZE = 18;

    private final EditText mEditText = (EditText) getControlView();

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

            int inputType = a.getInteger(R.styleable.FloatingLabelEditText_android_inputType, -1);

            if (inputType != -1) {
                mEditText.setInputType(inputType);
            }

            a.recycle();
        }

        mEditText.setText(mText);
        mEditText.setHint(mHint);
        mEditText.setTextSize(mTextSize);
        setFloatingLabelText(mHint);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    fadeInToTop(true);
                } else {
                    fadeOutToBottom(true);
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

    public CharSequence getText() {
        return mEditText.getText();
    }

    public void setText(int resId) {
        setText(getContext().getString(resId));
    }

    public void setText(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            mEditText.setText(text);
        }
    }

    public CharSequence getHint() {
        return mEditText.getHint();
    }

    public void setHint(int resId) {
        setHint(getContext().getString(resId));
    }

    public void setHint(CharSequence hint) {
        if (!TextUtils.isEmpty(hint)) {
            mEditText.setHint(hint);
            setFloatingLabelText(hint);
        }
    }

    public void setTextAppearance(Context context, int resId) {
        if (resId != mTextAppearance) {
            mEditText.setTextAppearance(context, resId);
            mTextAppearance = resId;
        }
    }

    public void setTextSize(float size) {
        mEditText.setTextSize(size);
    }

    public void setTextSize(int unit, float size) {
        mEditText.setTextSize(unit, size);
    }

    public void setInputType(int type) {
        mEditText.setInputType(type);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        mEditText.addTextChangedListener(watcher);
    }

    public void removeTextChangedListener(TextWatcher watcher) {
        mEditText.removeTextChangedListener(watcher);
    }

}
