package com.meiling.framework.app.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Toast;

import com.meiling.framework.R;
import com.meiling.framework.app.activity.camerax.CameraXCaptureImageActivity;
import com.meiling.framework.app.fragment.DataBindFragment;
import com.meiling.framework.base.BaseActivity;
import com.meiling.framework.databinding.ActivityMainBinding;
import com.meiling.framework.utils.gson.GsonUtil;
import com.meiling.framework.utils.log.Ulog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    /**
     * todo DataBinding在当前页面修改时，能够够及时进行变更，但在跨页面时，
     * 如果关联起来的话，如果处理不好，会使得错误的选择（修改）立马生效
     */

    @Override
    public void setConfiguration() {
        isFullScreen = true;
        isDarkNavigationBarButton = false;
        // 从Android 9 的表现上来看【isDarkNavigationBarButton】
        customNavigationBarColor = Color.parseColor("#ff000000");
    }

    @Override
    public int layoutViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void afterDestroy() {

    }

    @Override
    public void initView() {
        layoutBinding.setClickPresenter(new MainClickPresenter());
    }

    @Override
    public void lazyLoadCallback() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag(String.valueOf(0)) != null) {
//                mainFragment = (DataBindFragment) getSupportFragmentManager().findFragmentByTag(String.valueOf(0));
//                transaction.replace(R.id.container, mainFragment, String.valueOf(0));
//                transaction.commit();
            Ulog.w("不执行---重复添加");
        } else {
            mainFragment = new DataBindFragment();
            transaction.add(R.id.container, mainFragment, String.valueOf(0));
            transaction.commit();
            Ulog.w("执行---添加");
        }
    }

    private DataBindFragment mainFragment;

    public class MainClickPresenter {
        /**
         * todo 回调的方法可见性最好使用public，默认的方法会使得布局文件无法访问到对应的方法
         *
         * @param view
         */
        public void jumpToActivity(View view) {
//            startActivity(new Intent(getApplicationContext(), DataBindButtonActivity.class));
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            if (getSupportFragmentManager().findFragmentByTag(String.valueOf(0)) != null) {
////                mainFragment = (DataBindFragment) getSupportFragmentManager().findFragmentByTag(String.valueOf(0));
////                transaction.replace(R.id.container, mainFragment, String.valueOf(0));
////                transaction.commit();
//                Ulog.w("不执行---重复添加");
//            } else {
//                mainFragment = new DataBindFragment();
//                transaction.add(R.id.container, mainFragment, String.valueOf(0));
//                transaction.commit();
//                Ulog.w("执行---添加");
//            }


//            Ulog.w("执行---点击回调");
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
//            } else {
//                startActivity(new Intent(getApplicationContext(), CameraXCaptureImageActivity.class));
//            }
            // todo databinding对应的ListView/RecyclerView用法
//            startActivity(new Intent(getApplicationContext(), DataBindRecyclerViewActivity.class));

            // todo 异步任务替代类
//            RxJavaUtil.getInstance().doExample(getApplicationContext(),"A");
//            RxJavaUtil.getInstance().doExample(getApplicationContext(),"B");

            commonRequestPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void requestPermissionIllegalArgument(int requestCode) {
        super.requestPermissionIllegalArgument(requestCode);
        Ulog.w("请求参数非法【权限】");
    }

    @Override
    public void requestPermissionIgnore(int requestCode) {
        super.requestPermissionIgnore(requestCode);
        Ulog.w("请求被忽略【权限】");
    }

    @Override
    public void requestPermissionSuccess(int requestCode) {
        super.requestPermissionSuccess(requestCode);
        Toast.makeText(this, "请求通过", Toast.LENGTH_SHORT).show();
        switch (requestCode){
            case REQUEST_CODE_PERMISSION:{
                startActivity(new Intent(getApplicationContext(), CameraXCaptureImageActivity.class));
                break;
            }
        }
    }

    @Override
    public void requestPermissionFailure(List<String> deniedPermission, int requestCode) {
        super.requestPermissionFailure(deniedPermission, requestCode);
        Toast.makeText(this, "请求失败：" + GsonUtil.getInstance().toJsonStr(deniedPermission), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /**
         * todo 关于Android10到Android11以上的文件访问问题：
         *  1、getExternalFilesDir(String type) 不需要权限（APP的内部空间，删除APP也会删除文件）
         *  2、MediaStore访问文件，在获取后可以在当前页面进行访问，但超出后可能存在访问不到的问题
         *  （例如：获取地址后，在Service中去通过File访问，确定会无法访问到）
         *  3、申请获取Manage_external_storage权限，当通过后，能够访问全部路径下文件（公共路径中的，以及私有路径下的也可以）
         */
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (PackageManager.PERMISSION_GRANTED == (grantResults == null ? -1 : grantResults[0])) {

                // Take the user to the success fragment when permission is granted
//                Toast.makeText(getApplicationContext(), "Permission request granted", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), CameraXCaptureImageActivity.class));
//                startActivity(new Intent(getApplicationContext(), CameraXCaptureImageActivity.class));
//                getCameraInfo();
            } else {
                Toast.makeText(getApplicationContext(), "Permission request denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 获取摄像头相关的参数，（前后、拍摄范围【像素宽高】）
     */
    private void getCameraInfo() {// 使用Camera2 API
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIdList = cameraManager.getCameraIdList();
            if (cameraIdList != null && cameraIdList.length > 0) {
                for (String id : cameraIdList) {
                    CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
                    int orientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                    int[] capabilities = characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
                    List<Integer> capabilitiesList = toArray(capabilities);
//                            Arrays.stream(capabilities).boxed().collect(Collectors.toList());// todo 该方法需要在API 24以及以上版本才可用【Android7 以及以上系统】
                    StreamConfigurationMap cameraConfig = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                    if (capabilitiesList.contains(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE)) {
                        Size[] sizeList = cameraConfig.getOutputSizes(MediaRecorder.class);
                        if (sizeList != null && sizeList.length > 0) {
                            for (Size size : sizeList) {
                                double secondsPerFrame = cameraConfig.getOutputMinFrameDuration(MediaRecorder.class, size) / 10000000.0;
                                int fps = secondsPerFrame > 0 ? (int) (1.0 / secondsPerFrame) : 0;
                                String fpsLabel = fps > 0 ? String.valueOf(fps) : "N/A";
                                Log.e("AndroidRuntime", id + "-" + lensOrientationString(orientation) + "-" + size.getWidth() + "-" + size.getHeight() + "-" + fpsLabel);
                            }
                        }
                    }
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> toArray(int[] list) {
        List<Integer> mList = new ArrayList<>();
        if (list != null && list.length > 0) {
            int size = list.length;
            for (int i = 0; i < size; i++) {
                mList.add(list[i]);
            }
        }
        return mList;
    }

    private String lensOrientationString(int value) {
        switch (value) {
            case CameraCharacteristics.LENS_FACING_BACK: {
                return "Back";
            }
            case CameraCharacteristics.LENS_FACING_FRONT: {
                return "Front";
            }
            case CameraCharacteristics.LENS_FACING_EXTERNAL: {
                return "External";
            }
            default: {
                return "Back";
            }
        }
    }
}