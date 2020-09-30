package com.eve.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;

import timber.log.Timber;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.os.Build.VERSION_CODES.M;

public class CameraUtils {
    private static CameraUtils instance;
    private final int REQUEST_CAMERA_PERMISSION = 2002;
    private File resultFile;
    private Context context;
    private CameraListener listener;
    private String filename;
    private String fileProviderPath;

    public interface CameraListener {
        void onCameraData(String filePath);
    }

    private CameraUtils() {

    }

    public static CameraUtils getInstance(Context context) {
        instance = instance == null ? new CameraUtils() : instance;
        instance.context = context;
        return instance;
    }

    public void setFileProviderPath(String path){
        fileProviderPath = path;
    }

    public void dispose() {
        instance.listener = null;
        resultFile = null;
        context = null;
//        instance = null;
    }

    public void openCamera(CameraListener listener, String filename) {
        instance.listener = listener;
        instance.filename = filename;
        if (Build.VERSION.SDK_INT >= M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            } else {
                startCamera();
            }
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        resultFile = null;
        try {
            resultFile = createImageFile();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (resultFile != null) {
            Uri photoURI = FileProvider.getUriForFile(context,
                    fileProviderPath,
                    resultFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            ((Activity) context).startActivityForResult(cameraIntent, 2222);
        }
    }

    private File createImageFile() {
        try {
            String imageFileName = instance.filename != null ? instance.filename : "JPEG_" + System.currentTimeMillis() + "_";
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (!storageDir.exists()) {
                if (!storageDir.mkdirs()) {
                    return null;
                }
            }
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",  /* suffix */
                    storageDir      /* directory */
            );
            image.createNewFile();

            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
//                Toast.makeText(getContext(), "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
//                finish();
            } else {
                startCamera();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2222) {
                try {
                    Luban.with(context)
                            .load(resultFile)
                            .ignoreBy(100)
                            .filter((String path) ->
                                    !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"))
                            )
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(File file) {
                                    if (instance.listener != null)
                                        instance.listener.onCameraData(Uri.fromFile(file).getPath());
                                    resultFile = null;
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Timber.e(e);
                                }
                            }).launch();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
