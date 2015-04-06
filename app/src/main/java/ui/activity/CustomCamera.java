package ui.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;

import com.trek2000.android.enterprise.R;

import ui.fragment.custom.camera.CameraPreviewFragment;
import utils.Utils;

/**
 * Created by trek2000 on 25/2/2015.
 */
public class CustomCamera extends FragmentActivity
        implements View.OnClickListener {

    /**
     * Single ton section
     */
    public static singleton.Camera camera = singleton.Camera.getInstance();

    /**
     * String section
     */
    public static int current_orientation = 0;

    /**
     * View section
     */
    public static Camera mCamera;
    public static Camera.CameraInfo mCameraInfo =
            new Camera.CameraInfo();
    public static MediaRecorder mMediaRecorder;

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance(int camera_id) {
        Camera c = null;
        try {
            c = Camera.open(camera_id); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            e.printStackTrace();
        }

        return c; // returns null if mCamera is unavailable
    }

    public static void releaseCamera() {
        // Should release camera before go to the other page
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    public static void releaseMediaRecorder() {
        // Should release camera before go to the other page
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    // for the Preview - from http://developer.android.com/reference/android/hardware/Camera.html#setDisplayOrientation(int)
    // note, if orientation is locked to landscape this is only called when setting up the activity, and will always have the same orientation
    public static void setCameraDisplayOrientation(Activity activity, int camera_id) {
        mCameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(camera_id, mCameraInfo);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String rotate_preview = sharedPreferences.getString("preference_rotate_preview", "0");

        if (rotate_preview.equals("180")) {
            degrees = (degrees + 180) % 360;
        }

        int result;
        if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (mCameraInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {
            result = (mCameraInfo.orientation - degrees + 360) % 360;
        }

        // On Sony Device, has issue : Black screen was shown on it
        mCamera.setDisplayOrientation(result);
    }

    /**
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back_in_activity_custom_gallery:
                finish();
                break;
            case R.id.tv_take_new:
                // Go to Custom Camera page.
                Utils.clearOldBackStack(this);
                break;
            case R.id.tv_upload_in_activity_custom_gallery:
                // todo After selected files in Folder page,
                // begin upload after closed Activity Custom Gallery
                finish();

                // todo transfer Array List had selected files inside to Enterprise activity
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_camera);

        // Should initialize new Camera after released
        mCamera = null;
        mCamera = getCameraInstance(define.Camera.CAMERA_BACK);

        // Always open camera with default mode - Back Camera mode.
        CameraPreviewFragment.IS_BACK_CAMERA_OR_FRONT_CAMERA = true;

        // Set Camera Display Orientation
        if (mCamera != null)
            setCameraDisplayOrientation(this, define.Camera.CAMERA_FONT);

        // Get orientation listener
        new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                getCurrentOrientation(orientation);
            }
        }.enable();

        // Should show Fragment Custom Camera Preview firstly
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fl_custom_camera, CameraPreviewFragment.newInstance())
                .commitAllowingStateLoss();

        initialVariables();
        initialData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Should release camera before go to the other page
        releaseCamera();
        releaseMediaRecorder();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            CameraPreviewFragment.stopAndRelaseRecordingVideo();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Should release camera before go to the other page
        releaseCamera();
        releaseMediaRecorder();
    }

    /**
     * Initial methods
     */

    private void initialData() {
        // Set listener
    }

    private void initialVariables() {
    }

    /**
     * Basic methods
     */

    private int getCurrentOrientation(int orientation) {
        orientation = (orientation + 45) / 90 * 90;
        this.current_orientation = orientation % 360;

        return current_orientation;
//        int new_rotation = 0;
//        if (mCameraInfo.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            new_rotation = (mCameraInfo.orientation - orientation + 360) % 360;
//        } else {
//            new_rotation = (mCameraInfo.orientation + orientation) % 360;
//        }
//        if (new_rotation != current_rotation) {
//            this.current_rotation = new_rotation;
//        }
    }
}
