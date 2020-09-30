package com.eve.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.eve.network.R;

import timber.log.Timber;


public class ErrorDialog extends Dialog implements View.OnClickListener {
    private String title;
    private String error;
    private View.OnClickListener onClickListener;
    private String status;


    public ErrorDialog(Context context, String title, String message) {
        super(context);
        this.title = title;
        this.error = message;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    public ErrorDialog setOnClickListener(View.OnClickListener listener) {
        onClickListener = listener;
        return this;
    }

    @Override
    public void show() {
//        if (status.equals("-1"))
//            return;
        super.show();
    }

    public ErrorDialog(Context context, String errorText) {
        super(context);
        this.error = errorText;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        setContentView(R.layout.dialog_error);

        if (title != null && title.length() > 0) {
            findViewById(R.id.titleText).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.titleText)).setText(title);
        }

        String errorText = error;

        Timber.e("<><><><><> %s", "" + errorText);
        ((TextView) findViewById(R.id.contentText)).setText(errorText);

        findViewById(R.id.ok).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ok) {
            if (onClickListener != null)
                onClickListener.onClick(v);

            dismiss();
        }
    }
}
