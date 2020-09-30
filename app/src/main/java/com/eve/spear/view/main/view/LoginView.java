package com.eve.spear.view.main.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.eve.spear.R;
import com.eve.spear.helper.SharedPrefsHelper;
import com.eve.spear.utils.FancyEditText;
import com.eve.spear.view.dashboard.DashboardActivity;

public class LoginView extends LinearLayout {
    private FancyEditText email;

    public LoginView(Context context) {
        super(context);
        init();
    }

    public LoginView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoginView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.view_login, this);
        email = findViewById(R.id.email);
        findViewById(R.id.login).setOnClickListener((View v) -> {
            if (email.getText().trim().length() <= 0) {
                Toast.makeText(getContext(), "Enter email to continue login/register.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent dashboardIntent = new Intent(getContext(), DashboardActivity.class);
            SharedPrefsHelper.getInstance().getPref().edit().putString("email", email.getText()).apply();
            dashboardIntent.putExtra("email", email.getText());
            getContext().startActivity(dashboardIntent);
            ((Activity) getContext()).finish();
        });
    }
}
