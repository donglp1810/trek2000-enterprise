package ui.fragment.custom.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.trek2000.android.enterprise.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import define.Constants;
import ui.activity.CustomCamera;
import utils.Utils;

public class CameraReviewFragment extends Fragment
        implements View.OnClickListener {
    /**
     * Data section
     */

    /**
     * The others methods
     */

    /**
     * String section
     */
    private static String FILE_PATH;
    /**
     * View section
     */
    private Button mBtnRetake;
//    private TextureVideoView mVvVideo;

    /**
     * Others section
     */
    private Button mBtnUse;
    private ImageView mIvPhoto;
    private VideoView mVvVideo;

    /**
     * Listener section
     */

    public static Fragment newInstance(String filePath) {
        FILE_PATH = filePath;

        CameraReviewFragment fragment = new CameraReviewFragment();
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_retake:
                // Transfer to Camera Preview page by re-using fragment from Back Stack
                Utils.clearOldBackStack(getActivity());
                break;
            case R.id.btn_use:
                // todo Use taken photo for uploading
                // Need finish activity after set again single ton
                // Set File Path into single ton way
//                Cloudstringers.fileItem.setFilePath(Camera.FILE_PATH);

                /**
                 * Finish Preview activity and go to onActivityForResult of activity cloudstringers
                 */
                getActivity().finish();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         * Set Orientation for page
         */
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View v = getLayoutInflater(savedInstanceState).inflate(
                R.layout.fragment_camera_review, container, false);

        // Initial views
        initialViews(v);
        initialData();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Back to previous mode already chose : Photo mode or Video mode
        if (!CameraPreviewFragment.IS_PHOTO_MODE_OR_VIDEO_MODE)
            CameraPreviewFragment.IS_PHOTO_MODE_OR_VIDEO_MODE = true;
        else
            CameraPreviewFragment.IS_PHOTO_MODE_OR_VIDEO_MODE = false;
    }

    /**
     * Initialize methods
     */
    private void initialData() {
        // Set listener
        mBtnRetake.setOnClickListener(this);
        mBtnUse.setOnClickListener(this);

        // Set Review
        // Need check the taken file is photo or video to set correctly
        if (Utils.isPhotoOrVideo(FILE_PATH) == 0) {
            // Photo

            // Show Use Photo text
            mBtnUse.setText(getString(R.string.use_photo));

            // Hide Image View, Show Video View player
            mIvPhoto.setVisibility(View.VISIBLE);
            mVvVideo.setVisibility(View.GONE);

            /**
             * Show image on View correctly
             */
            showPhotoOnUI(true);
        } else if (Utils.isPhotoOrVideo(FILE_PATH) == 1) {
            // Video

            // Show Use VDO text
            mBtnUse.setText(getString(R.string.use_video));

            // Hide Image View, Show Video View player
            mIvPhoto.setVisibility(View.GONE);
            mVvVideo.setVisibility(View.VISIBLE);

            /**
             * Set up video view
             */
            showVideoOnUI();
        }
    }

    private void initialViews(View v) {
        mBtnRetake = (Button) v.findViewById(R.id.btn_retake);
        mBtnUse = (Button) v.findViewById(R.id.btn_use);
        mIvPhoto = (ImageView) v.findViewById(R.id.iv_review_photo);
        mVvVideo = (VideoView) v.findViewById(R.id.vv_review_video);
//        mVvVideo = (reTextuVideoView) v.findViewById(R.id.vv_review_video);
    }

    private void showPhotoOnUI(boolean is_success) {
        try {
            Bitmap mBitmap = null;

            // Check OutOfMemory error happened or not.
            // If success, decode file normally
            // If fail, need put sampleSize when decoding
            if (is_success)
                mBitmap = BitmapFactory.decodeFile(
                        FILE_PATH);
            else
                mBitmap = BitmapFactory.decodeFile(
                        FILE_PATH, Utils.getBitmapOptions());

            // Rotate Back photo only once in here
            Bitmap mBitmapRotated = Utils.rotateBackImage(mBitmap);

            // Begin crop photo in here
            if (CustomCamera.camera.isCropModeOrFullMode()) {
                /**
                 * Crop mode
                 */

                int width = mBitmapRotated.getWidth();
//                int height = mBitmapRotated.getHeight();
//                int newWidth = CameraPreviewFragment.crop_width;
                int newHeight = CameraPreviewFragment.crop_height;

                // create matrix for the manipulation
                Matrix matrix = new Matrix();

//                Log.i("", "Bitmap width - " + width + " Bitmap height " + height
//                        + " new Bitmap width " + newWidth + " new Bitmap height " + newHeight);

                // todo Should use best resolution from camera
                // recreate the new Bitmap
                Bitmap resizedBitmap = Bitmap.createBitmap(
                        mBitmapRotated,
                        // todo Define X, Y where to begin crop
                        0, newHeight/2 - 80,
                        width, newHeight + 200, matrix, true);

                mIvPhoto.setImageBitmap(resizedBitmap);

                // todo Should overwrite the file inside sd card.
                //create a file to write bitmap data
                File f = new File(FILE_PATH);
                f.createNewFile();

                //Convert bitmap to byte array
                Bitmap bitmap = resizedBitmap;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } else {
                /**
                 * Full Screen mode
                 */

                // Set image on View
                if (CameraPreviewFragment.IS_BACK_CAMERA_OR_FRONT_CAMERA) {
                    // Back Camera
                    mIvPhoto.setImageBitmap(mBitmapRotated);
                } else {
                    // Front Camera

                    // Rotate Front photo need again in here
                    Bitmap mBitmapRotatedFront = null;
                    if (!CameraPreviewFragment.IS_BACK_CAMERA_OR_FRONT_CAMERA) {
                        mBitmapRotatedFront = Utils.rotateFrontImage(getActivity(), mBitmapRotated);
                    }

                    mIvPhoto.setImageBitmap(mBitmapRotatedFront);
                }
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();

            showPhotoOnUI(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showVideoOnUI() {
        File mFile = new File(FILE_PATH);

        Log.i("", "FILE_PATH " + FILE_PATH);

        // ex. FILE PATH = /mnt/sdcard/Pictures/Enterprise/VID_20150327_143555.mp4
        mVvVideo.setVideoPath(mFile.getAbsolutePath());

        // set play video view dialog details photo
        MediaController mMc = new MediaController(getActivity());
        mMc.setAnchorView(mVvVideo);
        mMc.setMediaPlayer(mVvVideo);

        mVvVideo.requestFocus();
        mVvVideo.setBackgroundColor(Color.WHITE);
        mVvVideo.setMediaController(mMc);
        mVvVideo.setZOrderOnTop(true);
        mVvVideo.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mVvVideo.isPlaying()) {
                    mVvVideo.pause();

                    // Show full-screen button again
                    mVvVideo.setVisibility(View.VISIBLE);
                } else {
                    mVvVideo.start();
                }

                return false;
            }
        });

//        updateTextureViewSize(2, 2);

        if (!mVvVideo.isPlaying())
            mVvVideo.start();
    }

//    private void updateTextureViewSize(int viewWidth, int viewHeight) {
//        float scaleX = 1.0f;
//        float scaleY = 1.0f;
//
//        // fit the scale to match the entire view
////        if(640 > 320){
////            scaleX = 640 / viewWidth * viewHeight / 320;
////        } else {
////            scaleY = 320 / viewHeight * viewWidth / 640;
////        }
//
////        if (mVvVideo.getWidth() > viewWidth && mVvVideo.getHeight() > viewHeight) {
////            scaleX = mVvVideo.getWidth() / viewWidth;
////            scaleY = mVvVideo.getHeight() / viewHeight;
////        } else if (mVvVideo.getWidth() < viewWidth && mVvVideo.getHeight() < viewHeight) {
////            scaleY = viewWidth / mVvVideo.getWidth();
////            scaleX = viewHeight / mVvVideo.getHeight();
////        } else if (viewWidth > mVvVideo.getWidth()) {
////            scaleY = (viewWidth / mVvVideo.getWidth()) / (viewHeight / mVvVideo.getHeight());
////        } else if (viewHeight > mVvVideo.getHeight()) {
////            scaleX = (viewHeight / mVvVideo.getHeight()) / (viewWidth / mVvVideo.getWidth());
////        }
//
//        // Calculate pivot points, in our case crop from center
//        int pivotPointX = viewWidth / 2;
//        int pivotPointY = viewHeight / 2;
//
//        Matrix matrix = new Matrix();
//        matrix.setScale(scaleX, scaleY, pivotPointX, pivotPointY);
//
//        mVvVideo.setTransform(matrix);
//        mVvVideo.setLayoutParams(new RelativeLayout.LayoutParams(viewWidth, viewHeight));
//    }

}
