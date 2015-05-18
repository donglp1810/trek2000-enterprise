package ui.fragment.review;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.trek2000.android.enterprise.R;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import ui.activity.CustomGallery;
import utils.Utils;

public class ReviewFragment extends Fragment
        implements View.OnClickListener, View.OnTouchListener {

    /**
     * String section
     */
    private static String FILE_PATH = null;

    /**
     * Data section
     */
    private Bitmap mBitmap;

    /**
     * View section
     */
    private ImageButton mIbtnBack;
    private ImageView mIvPreview;
    private FrameLayout mFl;
    private FrameLayout mFlAuthorBar;
    private FrameLayout mFlDownloadBar;
    private TextView mTvAuthor;
    private TextView mTvCurrentPosition;
    private TextView mTvDownload;
    private VideoView mVvPreview;

    /**
     * Others section
     */

    /**
     * Listener section
     */

    public static Fragment newInstance(String preview) {
        FILE_PATH = preview;

        ReviewFragment fragment = new ReviewFragment();
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back_in_fragment_preview:
                // todo
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
                R.layout.fragment_review, container, false);

        // Should Hide Top Bar in Custom Gallery if stay in current page
        CustomGallery.mLlAlbumName.setVisibility(View.GONE);

        // Initial views
        initialViews(v);
        initialData();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Recycle Bitmap to avoid Out
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }

        // Should Show Top Bar in Custom Gallery if get out of current page
        CustomGallery.mLlAlbumName.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        /**
         * When touch on the screen,
         * if currently the bar was shown, should hide it,
         * otherwise, show it
         */
        if (mFlAuthorBar.isShown()) {
            mFlAuthorBar.setVisibility(View.INVISIBLE);
            mFlDownloadBar.setVisibility(View.INVISIBLE);
        } else {
            mFlAuthorBar.setVisibility(View.VISIBLE);
            mFlDownloadBar.setVisibility(View.VISIBLE);
        }

        return false;
    }

    /**
     * Initialize methods
     */
    private void initialData() {
        /**todo
         * Check is video or photo to show views correctly
         */
//        if (Utils.isPhotoOrVideo(mFile.getAbsolutePath())) {
            // Photo
            mIvPreview.setVisibility(View.VISIBLE);

            try {
                mBitmap = getBitmapFromUri(getActivity(), Uri.parse(FILE_PATH));
                mIvPreview.setImageBitmap(mBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
//        } else {
//            // Videos
//        }

        // Set listener
        mIbtnBack.setOnClickListener(this);

        mFl.setOnTouchListener(this);
    }

    private void initialViews(View v) {
        mIbtnBack = (ImageButton) v.findViewById(R.id.ibtn_back_in_fragment_preview);
        mIvPreview = (ImageView) v.findViewById(R.id.iv_photo_in_fragment_preview);
        mFl = (FrameLayout) v.findViewById(R.id.fl_in_fragment_preview);
        mFlAuthorBar = (FrameLayout) v.findViewById(R.id.fl_author_bar_in_fragment_preview);
        mFlDownloadBar = (FrameLayout) v.findViewById(R.id.fl_download_bar_in_fragment_preview);
        mTvAuthor = (TextView) v.findViewById(R.id.tv_author_in_fragment_preview);
        mTvCurrentPosition = (TextView) v.findViewById(R.id.tv_current_position_in_fragment_preview);
        mTvDownload = (TextView) v.findViewById(R.id.tv_download_in_fragment_preview);
        mVvPreview = (VideoView) v.findViewById(R.id.vv_video_in_fragment_preview);
    }

    /**
     * The others methods
     */

    private Bitmap getBitmapFromUri(Context mContext, Uri contentUri) {
        String path = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = mContext.getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            path = cursor.getString(columnIndex);
        }
        cursor.close();
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }
}
