package com.example.test;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import androidx.annotation.NonNull;

/**
 * Created by Abhishek on 28-11-2015.
 */
@SuppressWarnings("ConstantConditions")
@TargetApi(Build.VERSION_CODES.M)
public class FlashLightUtils {
    private CameraManager mCameraManager;
    private CameraManager.TorchCallback mTorchCallback;
    private Context context;
    private Boolean isFlashOn =false;

    public FlashLightUtils(Context context) throws CameraAccessException {
        this.context = context;
        openCamera();
    }

    private void openCamera() throws CameraAccessException {
        if (mCameraManager == null)
            mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        if (isFlashAvailable()) {
            mTorchCallback = new CameraManager.TorchCallback() {
                @Override
                public void onTorchModeUnavailable(@NonNull String cameraId) {
                    super.onTorchModeUnavailable(cameraId);

                }

                @Override
                public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                    super.onTorchModeChanged(cameraId, enabled);
                    if (enabled)
                        isFlashOn = true;
                    else
                        isFlashOn = false;
                }
            };
            mCameraManager.registerTorchCallback(mTorchCallback, null);
        }
    }

    private boolean isFlashAvailable() throws CameraAccessException {
        CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics("0");
        return cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
    }

    public void turnOnFlash() throws CameraAccessException {
        String[] cameraIds = getCameraManager().getCameraIdList();
        for (String id : cameraIds) {
            CameraCharacteristics characteristics = getCameraManager().getCameraCharacteristics(id);
            if (characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                getCameraManager().setTorchMode(id, true);
                isFlashOn = true;
            }
        }
    }

    public void turnOffFlash() throws CameraAccessException {
        String[] cameraIds = getCameraManager().getCameraIdList();
        for (String id : cameraIds) {
            CameraCharacteristics characteristics = getCameraManager().getCameraCharacteristics(id);
            if (characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                getCameraManager().setTorchMode(id, false);
                isFlashOn = false;
            }
        }
    }

    public void release() {
        if (mCameraManager != null) {
            mCameraManager.unregisterTorchCallback(mTorchCallback);
            mCameraManager = null;
        }
    }

    //region Accessors

    private CameraManager getCameraManager() throws CameraAccessException {
        if (mCameraManager == null) {
            openCamera();
        }
        return mCameraManager;
    }
    //endregion


    public Boolean getFlashOn() {
        return isFlashOn;
    }
}