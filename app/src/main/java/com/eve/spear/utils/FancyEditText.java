package com.eve.spear.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eve.spear.R;
import com.eve.spear.helper.DisplayHelper;


public class FancyEditText extends RelativeLayout implements TextWatcher {
    private TextView hint;
    private EditText input;
    private LinearLayout inputWrapper;
    private boolean enableClear;
    private FancyEditTextListener listener;

    public interface FancyEditTextListener {
        void onTextChanged(String value);
    }

    public FancyEditText(Context context) {
        super(context);
        init(null);
    }

    public FancyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FancyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setListener(FancyEditTextListener listener) {
        this.listener = listener;
    }

    private void init(AttributeSet attrs) {
        View.inflate(getContext(), R.layout.view_fancy_edittext, this);
        hint = findViewById(R.id.hint);
        input = findViewById(R.id.input);
        inputWrapper = findViewById(R.id.inputWrapper);

        input.addTextChangedListener(this);

        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FancyEditText, 0, 0);
            try {
                String hintString = ta.getString(R.styleable.FancyEditText_editTextHint);
                hint.setText(hintString);
                input.setHint(hintString);
                enableClear = ta.getBoolean(R.styleable.FancyEditText_clearEnable, false);
            } finally {
                ta.recycle();
            }
        }
        findViewById(R.id.clear).setOnClickListener((View v) -> {
            input.setText("");
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    public String getText() {
        return input.getText().toString();
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().trim().length() > 0) {
            showHint();
            if (enableClear)
                findViewById(R.id.clear).setVisibility(VISIBLE);
            input.setTypeface(input.getTypeface(), Typeface.BOLD);
        } else {
            hint.setVisibility(GONE);
            findViewById(R.id.clear).setVisibility(GONE);
            input.setTypeface(null, Typeface.NORMAL);

        }
        inputWrapper.setBackgroundResource(s.toString().length() > 0 ? R.drawable.bg_frame_gray : R.drawable.bg_light_gray);

        if (listener != null)
            listener.onTextChanged(s.toString().trim());
    }

    private void showHint() {
        if (hint.getVisibility() == VISIBLE || hint.getText().toString().length() <= 0)
            return;

        hint.setVisibility(VISIBLE);
        Path path = new Path();
        path.rMoveTo(DisplayHelper.dip2px(getContext(), 12), DisplayHelper.dip2px(getContext(), 15));
        path.rLineTo(0, DisplayHelper.dip2px(getContext(), -15));
        ObjectAnimator animation = ObjectAnimator.ofFloat(hint, View.X, View.Y, path);
        animation.setDuration(80);
        animation.start();
    }
}
