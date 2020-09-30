package com.eve.spear.view.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.eve.spear.R;
import com.eve.spear.helper.SharedPrefsHelper;
import com.eve.spear.view.about.AboutActivity;
import com.eve.spear.view.kyc.KYCActivity;
import com.eve.spear.view.main.MainActivity;
import com.eve.spear.view.settings.SettingsActivity;

public class DashboardActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (SharedPrefsHelper.getInstance().getPref().contains("email"))
            ((TextView) findViewById(R.id.email)).setText(SharedPrefsHelper.getInstance().getPref().getString("email", ""));

        findViewById(R.id.kyc).setOnClickListener((View v) -> {
            startActivity(new Intent(this, KYCActivity.class));
        });
        findViewById(R.id.settings).setOnClickListener((View v) -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
        findViewById(R.id.about).setOnClickListener((View v) -> {
            startActivity(new Intent(this, AboutActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPrefsHelper.getInstance().getPref().contains("email")) {
            ((TextView) findViewById(R.id.email)).setText(SharedPrefsHelper.getInstance().getPref().getString("email", ""));
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
