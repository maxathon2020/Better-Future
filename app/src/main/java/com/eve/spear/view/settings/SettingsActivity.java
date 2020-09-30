package com.eve.spear.view.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.eve.spear.R;
import com.eve.spear.helper.SharedPrefsHelper;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.back).setOnClickListener((View v) -> finish());
        findViewById(R.id.logout).setOnClickListener((View v) -> {
            SharedPrefsHelper.getInstance().getPref().edit().remove("email").apply();
            finish();
        });
    }
}
