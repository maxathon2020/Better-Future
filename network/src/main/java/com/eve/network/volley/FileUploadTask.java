package com.eve.network.volley;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.eve.network.NetworkConfig;
import com.eve.network.R;
import com.eve.network.volley.model.UploadRequest;
import com.eve.network.volley.model.UploadResponse;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class FileUploadTask extends AsyncTask<Void, String, String> {
    private UploadRequest data;
    private byte[] file;
    private FileUploadListener listener;
    private ConnectivityManager connectivityManager;

    public interface FileUploadListener {
        void onSuccess(UploadResponse result);

        void onError(String code, String response);
    }

    public FileUploadTask(UploadRequest data, byte[] file) {
        this.data = data;
        this.file = file;

        connectivityManager = (ConnectivityManager) NetworkConfig.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public FileUploadTask setListener(FileUploadListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    protected String doInBackground(Void... voids) {
        uploadFile(data, file);
        return null;
    }


    public void uploadFile(UploadRequest data, byte[] file) {
        try {
            if (!isConnected()) {
                String errmsg = NetworkConfig.getContext().getString(R.string.error_internet_msg);
                if (listener != null)
                    listener.onError("1000", errmsg);
                return;
            }
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .addNetworkInterceptor(logging)
                    .build();
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart(data.filename, data.filename + ".jpg",
                            RequestBody.create(MediaType.parse("image/jpeg"),
                                    file)).build();
            Request request = new Request.Builder()
                    .url(data.host)
                    .post(body)
                    .addHeader("Authorization", data.authorization)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseString = response.body().string();
                if (listener != null) {
                    listener.onSuccess(new Gson().fromJson(responseString, UploadResponse.class));
                }
                return;
            }
            if (listener != null)
                listener.onError(String.valueOf(response.code()), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isConnected() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
