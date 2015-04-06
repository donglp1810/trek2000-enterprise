package ui.fragment.custom.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.trek2000.android.enterprise.R;

import java.io.File;
import java.util.ArrayList;

import adapter.AlbumListAdapter;
import model.Album;
import ui.activity.CustomCamera;
import utils.Utils;

public class AlbumListFragment extends Fragment
        implements View.OnClickListener {

    /**
     * Data section
     */
    private ArrayList<Album> mAlAlbumList = null;
    private ArrayList<File> mAlFiles = new ArrayList<>();
    private ArrayList<String> mAlAlbumNames = new ArrayList<>();

    /**
     * String section
     */

    /**
     * View section
     */
    private LinearLayout mLlTakeNew;
    private ListView mLv;
    private TextView mTvTakeNew;

    /**
     * Others section
     */

    /**
     * Listener section
     */

    public static Fragment newInstance() {
        AlbumListFragment fragment = new AlbumListFragment();
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_take_new:
                // todo Go to Custom Camera page.
                if (Utils.checkCameraHardware(getActivity())) {
                    // If device support Camera feature,
                    // call Camera activity, finish Gallery activity
                    startActivity(new Intent(getActivity(), CustomCamera.class));

                    // Should finish current activity
                    getActivity().finish();
                } else {
                    // If device not support Camera feature,
                    // show Toast message.
                    Toast.makeText(getActivity(), "Current device not support Camera!", Toast.LENGTH_SHORT).show();
                }
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
                R.layout.fragment_album_list, container, false);

        // Initial views
        initialViews(v);
        initialData();

        return v;
    }

    /**
     * Initialize methods
     */
    private void initialData() {
        /**
         * Set listener
         */
        mLlTakeNew.setOnClickListener(this);

        // Get folder list to shown on page
        mAlAlbumList = getAlbumList();

        // Set adapter for list view
        mLv.setAdapter(new AlbumListAdapter(
                getActivity(), R.layout.simple_list_item_in_album_list, mAlAlbumList));
    }

    private void initialViews(View v) {
        mLv = (ListView) v.findViewById(R.id.lv_in_fragment_custom_gallery);
        mLlTakeNew = (LinearLayout) v.findViewById(R.id.ll_take_new);
        mTvTakeNew = (TextView) v.findViewById(R.id.tv_take_new);
    }

    /**
     * The others methods
     */

    private ArrayList<Album> getAlbumList() {
        // Clear before add new list
        if (!mAlFiles.isEmpty()) mAlFiles.clear();
        if (!mAlAlbumNames.isEmpty()) mAlAlbumNames.clear();

        ArrayList<Album> mAlThumbnail = new ArrayList<Album>();

        // Get thumbnail list from Gallery
        File mFileFolder =
//                new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//                Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android");

        ArrayList<File> mAlAlbumNames = new ArrayList<>();
        mAlAlbumNames = Utils.getPhotoAndVideoFromSdCard(true, mAlAlbumNames, mFileFolder);

        for (int i = 0; i < mAlAlbumNames.size(); i++) {
            /**
             * From Absolute File Path, extract :
             * - Folder name
             * - Read folder to know how many video files, how many photo files
             * - Load Folder thumbnail from latest file,
             *      if is video file, show Video preview with Play button
             *      if is photo file, show Photo preview
             */

            // Set Number of files
            // need get correct folder first before get number of files in these folders
            if (!this.mAlAlbumNames.contains(mAlAlbumNames.get(i).getName())) {
                this.mAlAlbumNames.add(mAlAlbumNames.get(i).getName());

                File mFile = new File(mAlAlbumNames.get(i).getAbsolutePath());
                if (mFile.isDirectory() & !mFile.isHidden()) {
                    // Clear data in old folder before read new data in new folder
                    if (!this.mAlFiles.isEmpty()) this.mAlFiles.clear();

                    // File information
                    ArrayList<File> mAlFileInfo = Utils.getPhotoAndVideoFromSdCard(false, this.mAlFiles, mFile);

                    Bitmap mBitmap = null;
                    boolean media_type_of_latest_bitmap_folder_thumbnail = true;
                    int number_of_photos = 0;
                    int number_of_videos = 0;

                    // Set bitmap thumbnail, number of files & media type
                    if (!mAlFileInfo.isEmpty()) {
                        String THUMBNAIL_PATH = mAlFileInfo.get(mAlFileInfo.size() - 1).getAbsolutePath();

                        // Calculate how many photos, how many videos
                        for (int j = 0; j < mAlFileInfo.size(); j++) {
//                            Log.i("", "" + mAlFileInfo.get(j).getName());

                            // If the File Name is picture, increase photos++
                            // If the File Name is video, increase videos++
                            if (Utils.isPhotoOrVideo(mAlFileInfo.get(j).getName()) == true)
                                number_of_photos++;
                            else
                                number_of_videos++;
                        }

                        media_type_of_latest_bitmap_folder_thumbnail = Utils.isPhotoOrVideo(THUMBNAIL_PATH);

                        if (media_type_of_latest_bitmap_folder_thumbnail == true) {
                            // Photos
                            mBitmap = Utils.getThumbnail(THUMBNAIL_PATH);
                        } else {
                            // Videos
                            mBitmap = ThumbnailUtils.createVideoThumbnail(
                                    THUMBNAIL_PATH, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                            mBitmap = ThumbnailUtils.extractThumbnail(
                                    mBitmap,
                                    getResources().getInteger(R.integer.layout_height_item_in_custom_gallery),
                                    getResources().getInteger(R.integer.layout_width_item_in_custom_gallery));
                        }
                    } else
                        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.iv_default_avatar);

                    // Item
                    Album album = new Album(
                            getActivity(),
                            mAlAlbumNames.get(i).getName(),
                            mAlAlbumNames.get(i).getParent(),
                            mBitmap,
                            media_type_of_latest_bitmap_folder_thumbnail,
                            number_of_photos, number_of_videos);

                    // Add item into array list
                    mAlThumbnail.add(album);
                }
            }
        }

        return mAlThumbnail;
    }
}
