package com.meiling.framework.app.activity.camerax;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.display.DisplayManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.google.common.util.concurrent.ListenableFuture;
import com.meiling.framework.R;
import com.meiling.framework.base.BaseActivity;
import com.meiling.framework.databinding.ActivityCameraxBinding;
import com.meiling.framework.utils.log.Ulog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class CameraXCaptureImageActivity extends BaseActivity<ActivityCameraxBinding> {

    private ExecutorService cameraExecutor;
    private LocalBroadcastManager localBroadcastManager;
    private DisplayManager displayManager;

    private int mDisplayId = -1;

    private File outputDirectory;

    @Override
    public void setConfiguration() {
        keepScreenOn = true;
    }

    @Override
    public int layoutViewId() {
        return R.layout.activity_camerax;
    }

    @Override
    public void afterDestroy() {

    }

    @Override
    public void initView() {
        initExecutor();
        initVolumeReceiver();
        initDisplayManager();
        initOutputPath();
    }

    @Override
    public void lazyLoadCallback() {
        layoutBinding.preview.post(new Runnable() {
            @Override
            public void run() {
                mDisplayId = layoutBinding.preview.getDisplay().getDisplayId();

                updateCameraUi();// onCreate preview Post

                setUpCamera();// onCreate preview Post
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Redraw the camera UI controls
        updateCameraUi(); // onConfigurationChanged

        // Enable or disable switching between cameras
        updateCameraSwitchButton();// onConfigurationChanged
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null) {//???????????????
            cameraExecutor.shutdown();
        }
        if (localBroadcastManager != null) {// ??????????????????
            localBroadcastManager.unregisterReceiver(volumeDownReceiver);
        }
        if (displayManager != null) {
            displayManager.unregisterDisplayListener(displayListener);
        }
    }

    //******************************************************************************************************************

    private void initExecutor() {// ????????????CameraX??????????????????????????????
        Ulog.w("initExecutor");
        cameraExecutor = Executors.newSingleThreadExecutor();
    }


    private BroadcastReceiver volumeDownReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getIntExtra(Intent.EXTRA_KEY_EVENT, KeyEvent.KEYCODE_UNKNOWN) == KeyEvent.KEYCODE_VOLUME_DOWN) {
                // ?????????????????????????????????
                Ulog.w("onReceive");
                layoutBinding.cameraCaptureButton.performClick();
            }
        }
    };

    private void initVolumeReceiver() {
        Ulog.w("initVolumeReceiver");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.EXTRA_KEY_EVENT);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(volumeDownReceiver, filter);
    }

    private DisplayManager.DisplayListener displayListener = new DisplayManager.DisplayListener() {
        @Override
        public void onDisplayAdded(int displayId) {

        }

        @Override
        public void onDisplayRemoved(int displayId) {

        }

        @Override
        public void onDisplayChanged(int displayId) {
            Ulog.w("onDisplayChanged(displayId):" + displayId + "---(mDisplayId)" + mDisplayId);
            if (displayId == mDisplayId) {
                if (layoutBinding != null && layoutBinding.preview != null && layoutBinding.preview.getDisplay() != null) {
                    if (imageCapture != null) imageCapture.setTargetRotation(layoutBinding.preview.getDisplay().getRotation());
                    if (imageAnalyzer != null) imageAnalyzer.setTargetRotation(layoutBinding.preview.getDisplay().getRotation());
                }
            }
        }
    };

    private void initDisplayManager() {
        Ulog.w("initDisplayManager");
        displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        displayManager.registerDisplayListener(displayListener, null);
    }

    private void initOutputPath() {
        Ulog.w("initOutputPath");
        /**
         * todo ????????????data??????????????????????????????????????????????????????????????????????????????????????????????????????????????????MANAGE_EXTERNAL_STORAGE?????????????????????????????????File??????????????????
         *  ??????????????????????????????????????????????????????????????????????????????????????????????????????open failed: EPERM (Operation not permitted)???
         */
        outputDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);// todo ?????????????????????data?????????????????????????????????????????????????????????????????????????????????????????????
//        if (Build.VERSION.SDK_INT >= 29) {
//            outputDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        } else {
//            outputDirectory = Environment.getExternalStorageDirectory();// todo ?????????????????????????????????????????????????????????????????????????????????????????????
        /*
        2021-03-16 15:29:28.084 25247-25403/com.meiling.databinding E/AndroidRuntime: Photo capture failed: ${exception.message}
            androidx.camera.core.ImageCaptureException: Cannot save capture result to specified location
                at androidx.camera.core.ImageCapture.lambda$takePicture$5(ImageCapture.java:799)
                at androidx.camera.core.-$$Lambda$ImageCapture$wcrF0gZsLfYB9ZBrY3_kPHJuK5I.run(Unknown Source:2)
                at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
                at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
                at java.lang.Thread.run(Thread.java:764)
         */
//        }
        if (outputDirectory != null && outputDirectory.exists()) {

        } else {
            outputDirectory = getExternalFilesDir(null);
        }
    }

    //******************************************************************************************************************
    private void setUpCamera() {
        Ulog.w("setUpCamera");
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getApplicationContext());
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    Ulog.w("setUpCamera  addListener");
                    cameraProvider = cameraProviderFuture.get();

                    if (hasBackCamera()) {
                        lensFacing = CameraSelector.LENS_FACING_BACK;
                    } else if (hasFrontCamera()) {
                        lensFacing = CameraSelector.LENS_FACING_FRONT;
                    } else {
                        throw new IllegalStateException("Back and front camera are unavailable");
                    }
                    // Enable or disable switching between cameras
                    updateCameraSwitchButton(); // setUpCamera
                    // Build and bind the camera use cases
                    bindCameraUseCases();// setUpCamera addListener

                    Ulog.w("setUpCamera  addListener --- End");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(getApplicationContext()));
    }

    //******************************************************************************************************************

    private int lensFacing = CameraSelector.LENS_FACING_BACK;// ????????????

    private void bindCameraUseCases() {
        Ulog.w("bindCameraUseCases");
        DisplayMetrics metrics = new DisplayMetrics();
        if (layoutBinding != null && layoutBinding.preview != null && layoutBinding.preview.getDisplay() != null) {
            layoutBinding.preview.getDisplay().getRealMetrics(metrics);// ?????????????????????
        }
        Ulog.w("bindCameraUseCases RealMetrics:(width)" + metrics.widthPixels + "---(height)" + metrics.heightPixels);
        int screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels);// ??????????????????
        Ulog.w("bindCameraUseCases  ???????????????" + screenAspectRatio + "--- AspectRatio.RATIO_4_3:" + AspectRatio.RATIO_4_3
                + "--- AspectRatio.RATIO_16_9:" + AspectRatio.RATIO_16_9);
        int rotation = 0;
        if (layoutBinding != null && layoutBinding.preview != null && layoutBinding.preview.getDisplay() != null) {
            rotation = layoutBinding.preview.getDisplay().getRotation();//????????????View???????????????
            Ulog.w("bindCameraUseCases ?????????????????????" + rotation);
        }
        if (this.cameraProvider != null) {
//            ProcessCameraProvider cameraProvider = this.cameraProvider;//
        } else {
            throw new IllegalStateException("Camera initialization failed.");
        }

        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(lensFacing).build();

        preview = new Preview.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation
                .setTargetRotation(rotation)
                .build();

        imageCapture = new ImageCapture.Builder()
//                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)// todo ??????????????????????????????????????????????????????????????????
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)// ---- ????????????????????????????????????????????????????????????????????????
                // We request aspect ratio but no resolution to match preview config, but letting
                // CameraX optimize for whatever specific resolution best fits our use cases
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation)
                .setFlashMode(ImageCapture.FLASH_MODE_AUTO)// todo ?????????????????????
                .build();

        imageAnalyzer = new ImageAnalysis.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation)
                .build();

        imageAnalyzer.setAnalyzer(cameraExecutor, new LuminosityAnalyzer(new LumaListener() {
            @Override
            public void analyzeResult(Double result) {
//                Log.i(TAG, "??????????????????(?????????byte????????????)???" + (result != null ? result.toString() : "null"));
            }
        }));

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll();

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            mCamera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer);
            Ulog.w("bindCameraUseCases  ??????Camera");
            // Attach the viewfinder's surface provider to preview use case
            if (layoutBinding != null && layoutBinding.preview != null && layoutBinding.preview.getSurfaceProvider() != null) {
                preview.setSurfaceProvider(layoutBinding.preview.getSurfaceProvider());
                Ulog.w("bindCameraUseCases  ????????????");
            }
            Ulog.w("bindCameraUseCases  ---  End");
        } catch (Exception exc) {
            Ulog.e("Use case binding failed" + Ulog.getThrowableStackTrace(exc));
        }
    }

    //******************************************************************************************************************

    private int aspectRatio(int width, int height) {
        Ulog.w("updateCameraUi ??????????????????");
        double previewRatio = 1.0 * Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - RATIO_4_3_VALUE) <= Math.abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }

    //******************************************************************************************************************

    private void updateCameraUi() {
        Ulog.w("updateCameraUi");
        // todo ?????????????????????????????????????????????????????????????????????????????????kotlin??????????????????????????????Java??????????????????RxJava???????????????

        layoutBinding.cameraSwitchButton.setEnabled(false);
        layoutBinding.cameraSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ??????
                Ulog.w("updateCameraUi  ?????????????????????");
                if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
                    lensFacing = CameraSelector.LENS_FACING_BACK;
                } else {
                    lensFacing = CameraSelector.LENS_FACING_FRONT;
                }
                bindCameraUseCases();// updateCameraUi   ?????????????????????
            }
        });

        layoutBinding.cameraCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ulog.w("updateCameraUi  ????????????");
                doShutter();// ????????????
            }
        });
    }

    private void updateCameraSwitchButton() {
        Ulog.w("updateCameraSwitchButton");
        if (layoutBinding != null && layoutBinding.cameraSwitchButton != null) {
            layoutBinding.cameraSwitchButton.setEnabled(hasBackCamera() && hasFrontCamera());
        }
    }

    /**
     * Returns true if the device has an available back camera. False otherwise
     */
    private boolean hasBackCamera() {
        try {
            return cameraProvider != null ? cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) : false;
        } catch (CameraInfoUnavailableException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns true if the device has an available front camera. False otherwise
     */
    private boolean hasFrontCamera() {
        try {
            return cameraProvider != null ? cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) : false;
        } catch (CameraInfoUnavailableException e) {
            e.printStackTrace();
            return false;
        }
    }

    //******************************************************************************************************************
    private final String FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS";
    private final String PHOTO_EXTENSION = ".jpg";
    private final double RATIO_4_3_VALUE = 4.0 / 3.0;
    private final double RATIO_16_9_VALUE = 16.0 / 9.0;

    private Preview preview = null;
    private ImageCapture imageCapture = null;
    private ImageAnalysis imageAnalyzer = null;
    private Camera mCamera = null;// ????????????Camera???????????????????????????????????????
    private ProcessCameraProvider cameraProvider = null;

    private void doShutter() {// ????????????
        Ulog.w("doShutter ????????????---(lensFacing)" + lensFacing + "---???0  ???1");

        // ??????????????????
        File photoFile = new File(outputDirectory, new SimpleDateFormat(FILENAME, Locale.US).format(System.currentTimeMillis()) + PHOTO_EXTENSION);

        // ?????????????????????/?????????????????????metadata
        ImageCapture.Metadata metadata = new ImageCapture.Metadata();
//        metadata.setReversedHorizontal(lensFacing == CameraSelector.LENS_FACING_FRONT);// ?????????????????????????????????
        metadata.setReversedHorizontal(false);// ?????????????????????????????????

        // ?????????????????????metaData????????????????????????
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).setMetadata(metadata).build();

        imageCapture.takePicture(outputOptions, cameraExecutor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults output) {
                Uri savedUri = output.getSavedUri() != null ? output.getSavedUri() : Uri.fromFile(photoFile);

                // We can only change the foreground Drawable using API level 23+ API
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Update the gallery thumbnail with latest picture taken
//                    setGalleryThumbnail(savedUri)
                    // todo ???????????????????????????????????????????????????????????????????????????
                }

                // Implicit broadcasts will be ignored for devices running API level >= 24
                // so if you only target API level 24+ you can remove this statement
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    sendBroadcast(new Intent(android.hardware.Camera.ACTION_NEW_PICTURE, savedUri));
                }

                // If the folder selected is an external media directory, this is
                // unnecessary but otherwise other apps will not be able to access our
                // images unless we scan them using [MediaScannerConnection]
                String mimeType;
                if ("file".equals(savedUri.getScheme())) {
                    File temp = new File(savedUri.getPath());
                    mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(temp.getName().substring(temp.getName().lastIndexOf(".") + 1));
                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{temp.getAbsolutePath()}, new String[]{mimeType}, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Ulog.e("doShutter onScanCompleted ???" + path + "---" + (uri != null ? uri.getPath() : "null"));
                        }
                    });
                }

                shutSuccessFlash();// todo ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Ulog.e("Photo capture failed: ${exception.message}" + Ulog.getThrowableStackTrace(exception));
            }
        });


    }

    private void shutSuccessFlash() {
        // We can only change the foreground Drawable using API level 23+ API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Display flash animation to indicate that photo was captured
            layoutBinding.container.postDelayed(new Runnable() {// ??????????????????????????????????????????
                @Override
                public void run() {
                    layoutBinding.container.setForeground(new ColorDrawable(Color.WHITE));
                    layoutBinding.container.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layoutBinding.container.setForeground(null);
                        }
                    }, 50);
                }
            }, 100);
        }
    }

    private void setFlashMode() {
        imageCapture.setFlashMode(ImageCapture.FLASH_MODE_AUTO);// todo  ??????????????????????????????????????????API???????????????????????????API??????????????????????????????
    }

    private void doFocus() {// ??????????????????????????????
        if (mCamera != null) {
//            mCamera.getCameraControl().startFocusAndMetering(new FocusMeteringAction.Builder().);
        }
    }

    private void setZoom() {// ????????????
        if (mCamera != null) {
            mCamera.getCameraControl().setZoomRatio(mCamera.getCameraInfo().getZoomState().getValue().getMaxZoomRatio());// todo ???????????????????????????????????????????????????????????????????????????
            mCamera.getCameraInfo().getZoomState().getValue().getMaxZoomRatio();
            mCamera.getCameraInfo().getZoomState().getValue().getMinZoomRatio();

            mCamera.getCameraControl().setLinearZoom(1);//todo ??? 0 ~ 1 FLoat???

            mCamera.getCameraInfo().getZoomState().getValue().getLinearZoom();
            mCamera.getCameraInfo().getZoomState().getValue().getZoomRatio();
        }
    }

}