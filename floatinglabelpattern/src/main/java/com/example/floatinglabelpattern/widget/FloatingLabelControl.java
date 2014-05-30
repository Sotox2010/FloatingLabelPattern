package com.example.floatinglabelpattern.widget;

import com.example.floatinglabelpattern.R;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class FloatingLabelControl<T extends View> extends LinearLayout {

    /**
     * The default interpolator for animations.
     */
    private static final TimeInterpolator ANIMATOR_INTERPOLATOR = new DecelerateInterpolator(2.0f);

    /**
     * ARGB evaluator for color transition animations.
     */
    private static final ArgbEvaluator COLOR_EVALUATOR = new ArgbEvaluator();

    /**
     * Custom property for text color transitions on TextView.
     */
    private static final Property<TextView, Integer> TEXT_COLOR_PROPERTY =
            new TextViewColorProperty(Integer.TYPE, "textColor");

    private TextView mFloatingLabel;
    private T mControlView;

    private CharSequence mFloatingLabelText;
    private int mFloatingLabelFocusedColor;
    private int mFloatingLabelUnfocusedColor;
    private int mFloatingLabelTextAppearance;
    private long mFloatingLabelAnimTime = 333L;
    private boolean mLaidOut = false;
    private boolean mFloating = false;
    private Rect mBoundsRect = new Rect();

    private ViewPropertyAnimator mFloatingLabelAnimator;
    private ObjectAnimator mFloatingLabelColorAnimator;

    public FloatingLabelControl(Context context) {
        this(context, null);
    }

    public FloatingLabelControl(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.floatingLabelControlStyle);
    }

    @SuppressWarnings("unchecked")
    public FloatingLabelControl(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Resources resources = context.getResources();

        LayoutInflater.from(context).inflate(R.layout.floating_label_control_merge, this);
        inflateControlView(context);

        mFloatingLabel = (TextView) getChildAt(0);
        mControlView = (T) getChildAt(1);
        mFloatingLabelAnimator = mFloatingLabel.animate();
        mFloatingLabelAnimator.setInterpolator(ANIMATOR_INTERPOLATOR);
        mFloatingLabelColorAnimator = ObjectAnimator.ofInt(mFloatingLabel, TEXT_COLOR_PROPERTY, 0);
        mFloatingLabelColorAnimator.setEvaluator(COLOR_EVALUATOR);
        mFloatingLabelColorAnimator.setInterpolator(ANIMATOR_INTERPOLATOR);

        mFloatingLabelFocusedColor = resources.getColor(R.color.floatingLabelFocused);
        mFloatingLabelUnfocusedColor = resources.getColor(R.color.floatingLabelUnfocused);

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(
                    attrs, R.styleable.FloatingLabelControl, defStyle, 0);

            mControlView.setId(a.getResourceId(R.styleable.FloatingLabelControl_controlId, -1));

            mFloatingLabelTextAppearance = a.getResourceId(
                    R.styleable.FloatingLabelControl_floatingLabelTextAppearance,
                            R.style.TextAppearance_FloatingLabel);

            mFloatingLabelFocusedColor = a.getColor(
                    R.styleable.FloatingLabelControl_floatingLabelFocusedColor,
                            mFloatingLabelFocusedColor);

            mFloatingLabelUnfocusedColor = a.getColor(
                    R.styleable.FloatingLabelControl_floatingLabelUnfocusedColor,
                            mFloatingLabelUnfocusedColor);

            mFloatingLabelText = a.getString(R.styleable.FloatingLabelControl_floatingLabelText);

            a.recycle();
        }

        mFloatingLabel.setTextAppearance(context, mFloatingLabelTextAppearance);
        mFloatingLabel.setTextColor(mFloatingLabelUnfocusedColor);
        mFloatingLabel.setText(mFloatingLabelText);

        setAddStatesFromChildren(true);
    }

    protected abstract void inflateControlView(Context context);

    protected void setFloatingLabelFocused(boolean hasFocus) {
        if (hasFocus) {
            focusOn(true);
        } else {
            focusOff(true);
        }
    }

    protected void toggleFloatingLabel() {
        if (mFloating) {
            slideUpToTop(false);
        } else {
            slideDownToBottom(false);
        }
    }

    protected void focusOn(boolean animated) {
        mFloatingLabelColorAnimator.setIntValues(mFloatingLabelFocusedColor);
        mFloatingLabelColorAnimator.setDuration(animated ? mFloatingLabelAnimTime : 0);
        mFloatingLabelColorAnimator.start();
    }

    protected void focusOff(boolean animated) {
        mFloatingLabelColorAnimator.setIntValues(mFloatingLabelUnfocusedColor);
        mFloatingLabelColorAnimator.setDuration(animated ? mFloatingLabelAnimTime : 0);
        mFloatingLabelColorAnimator.start();
    }

    protected void slideUpToTop(boolean animated) {
        mFloatingLabelAnimator.alpha(1f)
                .translationY(0)
                .setDuration(animated ? mFloatingLabelAnimTime : 0)
                .start();
        mFloating = true;
    }

    protected void slideDownToBottom(boolean animated) {
        mFloatingLabelAnimator.alpha(0f)
                .translationY(mFloatingLabel.getHeight() / 2)
                .setDuration(animated ? mFloatingLabelAnimTime : 0)
                .start();
        mFloating = false;
    }

    protected T getControlView() {
        return mControlView;
    }

    public void setFloatingLabelText(CharSequence text) {
        if (!TextUtils.equals(mFloatingLabelText, text)) {
            mFloatingLabel.setText(text);
            mFloatingLabelText = text;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      mLaidOut = false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mLaidOut) {
            toggleFloatingLabel();
            mLaidOut = true;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        getDrawingRect(mBoundsRect);
        setTouchDelegate(new TouchDelegate(mBoundsRect, mControlView));
    }

}
