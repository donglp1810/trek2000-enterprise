package ui.fragment.custom.camera;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trek2000.android.enterprise.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import define.Extension;
import surface.CameraPreview;
import ui.activity.CustomCamera;
import utils.Utils;

public class CameraPreviewFragment extends Fragment
        implements View.OnClickListener {

    /**
     * Data section
     */

    /**
     * String section
     */
    public static int crop_height = 0;
    public static int crop_width = 0;
    private static int current_camera_id = define.Camera.CAMERA_BACK;

    private boolean IS_ALREADY_ADDED_CAMERA_REVIEW_INSIDE = false;
    public static boolean IS_BACK_CAMERA_OR_FRONT_CAMERA = true;
    private boolean IS_FIRST_TIME_GO_TO_CAMERA_PREVIEW_PAGE = false;
    public static boolean IS_PHOTO_MODE_OR_VIDEO_MODE = true;
    private static boolean IS_RECORDING_VIDEO = false;

    private static int minute = 0;

    private String RECORDED_FILE_PATH = null;

    private static int second = 0;

    /**
     * The others methods
     */

    private Camera.PictureCallback mPhoto = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // Pass True value to indicate choose Photo mode
            File pictureFile =
                    getOutputMediaFile(true);
            if (pictureFile == null) {
                Log.i("", "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

                // After take picture successfully,
                //      - Need refresh Gallery to see new image
                //      - Go to preview page to see taken picture

                // Refresh Gallery
                Utils.addPictureToGallery(getActivity(), pictureFile.getAbsolutePath());

                // Go to Review page
                ((FragmentActivity) getActivity())
                        .getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fl_custom_camera,
                                CameraReviewFragment.newInstance(pictureFile.getAbsolutePath()))
                        .commitAllowingStateLoss();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * View section
     */
    private CameraPreview mCameraPreview;
    private FrameLayout mFlCameraPreview;
    private FrameLayout mFlSwitchFrontOrBackCamera;
    private FrameLayout mFlTakePhotoOrRecordVideo;
    private ImageButton mIbtnClose;
    private static ImageButton mIbtnTakePhotoOrRecordVideo;
    private ImageButton mIbtnSwitchCropMode;
    private ImageButton mIbtnSwitchFullMode;
    private ImageButton mIbtnSwitchFrontOrBackCamera;
    private ImageButton mIbtnSwitchTakePhotoOrRecordVideo;
    private LinearLayout mLlElapseTime;
    private static TextView mTvElapseTime;

    /**
     * Others section
     */

    // new Counter that counts 3000 ms with a tick each 1000 ms
    private static CountDownTimer mCdt = new CountDownTimer(24 * 60 * 60 * 1000, 1000) {
        public void onTick(long millisUntilFinished) {
            //update the UI with the new count
            second += 1;

            if (second == 2)
                // Enable again for user clicked to stop recording video
                mIbtnTakePhotoOrRecordVideo.setEnabled(true);

            if (second == 60) {
                minute = minute + 1;

                second = 0;
            }
            if (minute < 10) {
                mTvElapseTime.setText("0" + minute + " : " + second);
            } else
                mTvElapseTime.setText(minute + " : " + second);
        }

        public void onFinish() {
            //start the activity
        }
    };

    public static Fragment newInstance() {
        CameraPreviewFragment fragment = new CameraPreviewFragment();
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_close:
                // Close current activity
                getActivity().finish();
                break;
            case R.id.ibtn_take_photo_or_record_video:
                /**
                 * Need check currently user choose Take Photo mode or Record Video mode.
                 * Depend on which mode, use Action
                 * - Take photo
                 * - Record video
                 * relatively.
                 */
                if (IS_PHOTO_MODE_OR_VIDEO_MODE) {
                    /**
                     * Take photo mode
                     */
                    // Begin Take picture
                    try {
                        CustomCamera.mCamera.takePicture(null, null, mPhoto);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    /**
                     * Record video mode
                     */
                    // Begin Record video

                    // Configure MediaRecorder
                    if (IS_RECORDING_VIDEO) {
                        try {
                            // stop recording and release camera
                            stopAndRelaseRecordingVideo();

                            // Transfer to Review page to see Recording video at there
                            // Send the file path also
                            ((FragmentActivity) getActivity())
                                    .getSupportFragmentManager().beginTransaction()
                                    .addToBackStack(null)
                                    .replace(R.id.fl_custom_camera,
                                            CameraReviewFragment.newInstance(RECORDED_FILE_PATH))
                                    .commitAllowingStateLoss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        // initialize video camera with Back mode or Front mode
                        if (prepareVideoRecorder(current_camera_id)) {
                            // Camera is available and unlocked, MediaRecorder is prepared,
                            // now you can start recording
                            CustomCamera.mMediaRecorder.start();

                            // inform the user that recording has started
                            IS_RECORDING_VIDEO = true;

                            // Begin set Recording time in here
                            //start the countDown
                            mCdt.start();

                            // hide the other views while recording video
                            mIbtnClose.setVisibility(View.INVISIBLE);
                            mIbtnSwitchFrontOrBackCamera.setVisibility(View.INVISIBLE);
                            mIbtnSwitchCropMode.setVisibility(View.INVISIBLE);
                            mIbtnSwitchFullMode.setVisibility(View.INVISIBLE);
                            mIbtnSwitchTakePhotoOrRecordVideo.setVisibility(View.INVISIBLE);

                            mIbtnTakePhotoOrRecordVideo.setImageResource(R.drawable.ibtn_stop_record_video);

                            // Disable for a while
                            mIbtnTakePhotoOrRecordVideo.setEnabled(false);
                        } else {
                            // prepare didn't work, release the camera
                            CustomCamera.releaseMediaRecorder();
                        }
                    }
                }
                break;
            case R.id.ibtn_switch_to_crop_mode:
                // Set Crop mode
                CustomCamera.camera.setCropModeOrFullMode(true);

                switchCropModeOrFullMode();
                break;
            case R.id.ibtn_switch_to_full_mode:
                // Set Full mode
                CustomCamera.camera.setCropModeOrFullMode(false);

                switchCropModeOrFullMode();
                break;
            case R.id.ibtn_switch_take_photo_or_record_video:
                // Show Take photo or Record video correctly
                switchPhotoOrVideoMode();
                break;
            case R.id.ibtn_switch_back_or_front_camera:
                // Switch between Front Camera & Back Camera
                if (CustomCamera.mCamera != null) {
                    // Stop preview
                    CustomCamera.mCamera.stopPreview();

                    //NB: if you don't release the current camera before switching, you app will crash
                    CustomCamera.releaseCamera();

                    // New setting for Camera
                    // Should initialize new Camera after released
                    current_camera_id = getCurrentCameraID();

                    CustomCamera.mCamera = CustomCamera.getCameraInstance(current_camera_id);

                    // Set Camera Display Orientation
                    CustomCamera.setCameraDisplayOrientation(getActivity(), current_camera_id);

                    try {
                        // Set preview display to refresh current face, use when change camera mode
                        CustomCamera.mCamera.setPreviewDisplay(mCameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    CustomCamera.mCamera.startPreview();
                }
                break;

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define first time go to this page, reset after destroy thi page
        IS_FIRST_TIME_GO_TO_CAMERA_PREVIEW_PAGE = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = getLayoutInflater(savedInstanceState).inflate(
                R.layout.fragment_camera_preview, container, false);

        // Initial views
        initialViews(v);
        initialData();

        // Should remove view parent before add child
        if (IS_ALREADY_ADDED_CAMERA_REVIEW_INSIDE) {
            Utils.removeViewParent(mCameraPreview);
        }

        /**
         * Add Camera Preview into Layout
         */
        // Create our Preview view and set it as the content of our activity.
        mCameraPreview = new CameraPreview(getActivity(), CustomCamera.mCamera);
        mFlCameraPreview.addView(mCameraPreview);

        // This boolean detail that in the next time include Camera Preview into layout
        // Should remove current Camera Preview before add new one
        IS_ALREADY_ADDED_CAMERA_REVIEW_INSIDE = true;

        // Reset Camera every time go Back to current page from the other pages
        resetCamera();

        // Switch Crop mode | Full mode
        switchCropModeOrFullMode();

        /**
         * Should show views correctly
         * - First time go to this page with default mode is Photo
         * - After back from Review page with previous mode after chose
         */
        switchPhotoOrVideoMode();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Reset
        IS_ALREADY_ADDED_CAMERA_REVIEW_INSIDE = false;

        IS_FIRST_TIME_GO_TO_CAMERA_PREVIEW_PAGE = false;
    }



    /**
     * Initialize methods
     */
    private void initialData() {
        // Set listener
        mIbtnClose.setOnClickListener(this);
        mIbtnSwitchCropMode.setOnClickListener(this);
        mIbtnSwitchFullMode.setOnClickListener(this);
        mIbtnSwitchFrontOrBackCamera.setOnClickListener(this);
        mIbtnSwitchTakePhotoOrRecordVideo.setOnClickListener(this);
        mIbtnTakePhotoOrRecordVideo.setOnClickListener(this);
    }

    private void initialViews(View v) {
        mFlCameraPreview = (FrameLayout) v.findViewById(R.id.fl_camera_preview);
        mFlSwitchFrontOrBackCamera = (FrameLayout) v.findViewById(R.id.fl_take_photo_or_record_video);
        mFlTakePhotoOrRecordVideo = (FrameLayout) v.findViewById(R.id.fl_switch_camera_font_or_back);

        mIbtnClose = (ImageButton) v.findViewById(R.id.ibtn_close);
        mIbtnSwitchCropMode = (ImageButton) v.findViewById(R.id.ibtn_switch_to_crop_mode);
        mIbtnSwitchFullMode = (ImageButton) v.findViewById(R.id.ibtn_switch_to_full_mode);
        mIbtnSwitchFrontOrBackCamera = (ImageButton) v.findViewById(
                R.id.ibtn_switch_back_or_front_camera);
        mIbtnSwitchTakePhotoOrRecordVideo = (ImageButton) v.findViewById(
                R.id.ibtn_switch_take_photo_or_record_video);
        mIbtnTakePhotoOrRecordVideo = (ImageButton) v.findViewById(
                R.id.ibtn_take_photo_or_record_video);
        mLlElapseTime = (LinearLayout) v.findViewById(
                R.id.ll_elapse_time);
        mTvElapseTime = (TextView) v.findViewById(
                R.id.tv_elapse_time);
    }

    /**
     * Basic methods
     */

    /**
     * Get current camera ID : Back Camera or Front Camera
     */
    private static int getCurrentCameraID() {
        int current_camera_id = 0;
        if (IS_BACK_CAMERA_OR_FRONT_CAMERA) {
            IS_BACK_CAMERA_OR_FRONT_CAMERA = false;

            // Currently - Back, switch to Front
            current_camera_id = 1;
        } else {
            IS_BACK_CAMERA_OR_FRONT_CAMERA = true;

            // Currently - Front, switch to Back
            current_camera_id = 0;
        }

        Log.i("", "getCurrentCameraID " + current_camera_id);

        return current_camera_id;
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(boolean mode) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), define.Camera.ENTERPRISE);

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.i("", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = null;
        if (mode) {
            // Photo mode
            mediaFile = new File(
                    mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + Extension.JPG);
        } else {
            // Video mode
            mediaFile = new File(
                    mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + Extension.MP4);
        }

        String FILE_PATH = mediaFile.getAbsolutePath();

        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(getActivity(),
                new String[]{FILE_PATH}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
        return mediaFile;
    }

    private boolean prepareVideoRecorder(int mode){
        // Should release before use new Preview for Recording Video mode
        CustomCamera.releaseCamera();

        // Initialize camera
        CustomCamera.mCamera = CustomCamera.getCameraInstance(mode);

        // Set orientation display
        CustomCamera.setCameraDisplayOrientation(getActivity(), mode);

        // Should release before use new Preview for Recording Video mode
        CustomCamera.releaseMediaRecorder();

        CustomCamera.mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        CustomCamera.mCamera.unlock();
        CustomCamera.mMediaRecorder.setCamera(CustomCamera.mCamera);

        // Step 2: Set sources
        CustomCamera.mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        CustomCamera.mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        CustomCamera.mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file - pass False value to indicate choose Video mode
        RECORDED_FILE_PATH = getOutputMediaFile(false).toString();
        CustomCamera.mMediaRecorder.setOutputFile(RECORDED_FILE_PATH);

        // Step 5: Set the preview output
        /**
         * Define Orientation of image in here,
         * if in portrait mode, use value = 90,
         * if in landscape mode, use value = 0
         */
        if (current_camera_id == define.Camera.CAMERA_BACK)
            // Back Camera
            CustomCamera.mMediaRecorder = Utils.rotateBackVideo(CustomCamera.mMediaRecorder);
        else
            // Front Camera
            CustomCamera.mMediaRecorder = Utils.rotateFrontVideo(CustomCamera.mMediaRecorder);

        // Set preview display to refresh current screen
        CustomCamera.mMediaRecorder.setPreviewDisplay(mCameraPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            CustomCamera.mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            CustomCamera.releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            CustomCamera.releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void resetCamera() {
        if (CustomCamera.mCamera == null) {
            int current_camera_id = getCurrentCameraID();
            CustomCamera.mCamera = CustomCamera.getCameraInstance(current_camera_id);
        }
    }

    public static void stopAndRelaseRecordingVideo() {
        if (CustomCamera.mMediaRecorder != null) {
            // stop recording and release camera
            // stop the recording
            CustomCamera.mMediaRecorder.stop();

            // release the MediaRecorder object
            CustomCamera.releaseMediaRecorder();
        }

        if (CustomCamera.mCamera != null) {
            // take camera access back from MediaRecorder
            CustomCamera.mCamera.lock();

            // inform the user that recording has stopped
            IS_RECORDING_VIDEO = false;

            // Stop the preview before transfer to Review page
            CustomCamera.mCamera.stopPreview();

            // After stop recording video, need reset time value
            mCdt.cancel();
            minute = 0;
            second = 0;
        }
    }

    private void switchCropModeOrFullMode() {
        /**
         * In the first time go to Custom Camera, should do these things:
         * - Select Full Mode (boolean = false).
         * - After check Single ton way, use Crop Mode or Full Mode follow boolean value
         * (Crop mode : true, Full mode : false)
         */
        if (CustomCamera.camera.isCropModeOrFullMode()) {
            /**
             * Crop mode
             */

            // Set Drawable resource
            mIbtnSwitchCropMode.setImageResource(R.drawable.ibtn_square_rectangle_selected);
            mIbtnSwitchFullMode.setImageResource(R.drawable.ibtn_rectangle_unselected);

            // Set Crop Bar background is black also : Top Black bar & Bottom black bar
            mFlSwitchFrontOrBackCamera.setBackgroundColor(
                    getActivity().getResources().getColor(android.R.color.black));
            mFlTakePhotoOrRecordVideo.setBackgroundColor(
                    getActivity().getResources().getColor(android.R.color.black));

            int height_bottom_bar = (Utils.getSizeOfScreen(getActivity())[1]
                    - Utils.getSizeOfScreen(getActivity())[0]) / 2;
            int height_top_bar = (Utils.getSizeOfScreen(getActivity())[1]
                    - Utils.getSizeOfScreen(getActivity())[0]) / 2;

            mFlSwitchFrontOrBackCamera.setMinimumHeight(height_top_bar);
            mFlTakePhotoOrRecordVideo.setMinimumHeight(height_bottom_bar);

            // Calculate the Height of Crop bar
            crop_height = Utils.getSizeOfScreen(getActivity())[1]
                    - (height_bottom_bar + height_top_bar);
            crop_width = Utils.getSizeOfScreen(getActivity())[0];
        } else {
            /**
             * Full mode
             */

            // Set Drawable resource
            mIbtnSwitchCropMode.setImageResource(R.drawable.ibtn_square_rectangle_unselected);
            mIbtnSwitchFullMode.setImageResource(R.drawable.ibtn_rectangle_selected);

            // Set Crop Bar background is black also : Top Black bar & Bottom black bar
            mFlSwitchFrontOrBackCamera.setBackgroundColor(
                    getActivity().getResources().getColor(android.R.color.transparent));
            mFlTakePhotoOrRecordVideo.setBackgroundColor(
                    getActivity().getResources().getColor(android.R.color.transparent));
        }
    }

    private void switchPhotoOrVideoMode() {
        if (!IS_PHOTO_MODE_OR_VIDEO_MODE
                | IS_FIRST_TIME_GO_TO_CAMERA_PREVIEW_PAGE) {
            IS_FIRST_TIME_GO_TO_CAMERA_PREVIEW_PAGE = false;

            // Switch to Photo mode from Video mode
            IS_PHOTO_MODE_OR_VIDEO_MODE = true;

            // Show views correctly
            mIbtnSwitchTakePhotoOrRecordVideo.setImageResource(
                    R.drawable.ibtn_switch_record_video);
            mIbtnTakePhotoOrRecordVideo.setImageResource(R.drawable.ibtn_take_photo);

            mLlElapseTime.setVisibility(View.INVISIBLE);
        } else {
            // Switch to Video mode from Photo mode
            IS_PHOTO_MODE_OR_VIDEO_MODE = false;

            mIbtnSwitchTakePhotoOrRecordVideo.setImageResource(
                    R.drawable.ibtn_switch_take_photo);
            mIbtnTakePhotoOrRecordVideo.setImageResource(R.drawable.ibtn_record_video);

            mLlElapseTime.setVisibility(View.VISIBLE);
        }
    }
}
