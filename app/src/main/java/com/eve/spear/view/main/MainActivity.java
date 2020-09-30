package com.eve.spear.view.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.eve.spear.R;
import com.eve.spear.helper.SharedPrefsHelper;
import com.eve.spear.view.dashboard.DashboardActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SharedPrefsHelper.getInstance().getPref().contains("email"))
            startActivity(new Intent(this, DashboardActivity.class));
    }
}