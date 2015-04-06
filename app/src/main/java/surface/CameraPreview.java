package surface;

/**
 * Created by trek2000 on 16/3/2015.
 */

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ui.activity.CustomCamera;

/**
 * A basic Camera preview class
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the mCamera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
            e.printStackTrace();
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // todo
        Camera.Parameters parameters = mCamera.getParameters();

        parameters.setJpegQuality(100);
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setPreviewSize(
                parameters.getSupportedPreviewSizes().get(0).width,
                parameters.getSupportedPreviewSizes().get(0).height);
//        parameters.setPictureSize(3264, 2176);

//        for (Camera.Size size : parameters.getSupportedPictureSizes()) {
//            // 640 480
//            // 960 720
//            // 1024 768
//            // 1280 720
//            // 1600 1200
//            // 2560 1920
//            // 3264 2448
//            // 2048 1536
//            // 3264 1836
//            // 2048 1152
//            // 3264 2176
//            Log.i("", "Pictures size " + size.width + " " + size.height);
//        }
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            // 960 720
            // 1280 720
            // 640 480
            // 352 288
            // 320 240
            Log.i("", "Preview size " + size.width + " " + size.height);
        }

        CustomCamera.mCamera.setParameters(parameters);

        Camera.Size size = CustomCamera.mCamera.getParameters().getPreviewSize();
        // 960 720
        Log.i("", "surfaceChanged " + size.width + " " + size.height);

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
