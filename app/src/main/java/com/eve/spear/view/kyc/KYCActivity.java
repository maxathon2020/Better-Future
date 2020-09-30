package com.eve.spear.view.kyc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.eve.network.volley.FileUploadTask;
import com.eve.network.volley.model.UploadRequest;
import com.eve.spear.R;

public class KYCActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc);
        findViewById(R.id.back).setOnClickListener((View v) -> finish());

//        findViewById(R.id.submit).setOnClickListener((View v) -> {
//
//            new FileUploadTask(new UploadRequest(), new byte[1024]).execute();
//        });
    }
}
