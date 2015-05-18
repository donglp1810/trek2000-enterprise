package ui.fragment.custom.gallery;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import model.AlbumFileInside;
import ui.activity.CustomCamera;
import utils.Utils;

public class AlbumListFragment extends Fragment
        implements View.OnClickListener {

    /**
     * Data section
     */
    public static ArrayList<Album> mAlAlbums = new ArrayList<>();

    private ArrayList<AlbumFileInside> mAlAlbumFilesInsideAlbum = new ArrayList<>();

    /**
     * String section
     */
    private static int increase_index_of_file = 0;

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
                // Go to Custom Camera page.
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

        // Clear old array list before add new items
        if (!mAlAlbumFilesInsideAlbum.isEmpty())
            mAlAlbumFilesInsideAlbum.clear();

        // Initial views
        initialViews(v);
        initialData();

        return v;
    }

    /**
     * Initialize methods
     */
    private void initialData() {
        // Clear old list before add new items
        mAlAlbums = new ArrayList<>();

        /**
         * Set listener
         */
        mLlTakeNew.setOnClickListener(this);

        // Get folder list to shown on page
        mAlAlbums = getAlbumList();

        // Set adapter for list view
        mLv.setAdapter(new AlbumListAdapter(
                getActivity(),
                R.layout.simple_list_item_in_album_list,
                mAlAlbums));
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
        ArrayList<Album> mAlAlbums = new ArrayList<>();

        ArrayList<String> mAlAlbumNames = new ArrayList<>();
        ArrayList<String> mAlAlbumPhotoFolderPath = new ArrayList<>();
        ArrayList<String> mAlAlbumVideoFolderPath = new ArrayList<>();

        /**
         * which image, video properties are we querying
         */
        String[] projection = new String[]{
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DATE_TAKEN
        };

        // Get the base URI for the People table in the Contacts content provider.
        Uri mUriPhotos = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri mUriVideos = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        // Make the query.
        Cursor mCursorPhotos = getActivity().managedQuery(
                mUriPhotos,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                null        // Ordering
        );

        Cursor mCursorVideos = getActivity().managedQuery(
                mUriVideos,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                null        // Ordering
        );

        /**
         * Move in Array Photo
         */
        if (mCursorPhotos.moveToFirst()) {
            String dateTaken, photo;

            int dateTakenColumn = mCursorPhotos.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);
            int photoColumn = mCursorPhotos.getColumnIndex(
                    MediaStore.Images.Media.DATA);

            do {
                // Get the field values
                dateTaken = mCursorPhotos.getString(dateTakenColumn);
                photo = mCursorPhotos.getString(photoColumn);

                // Do something with the values.
                File mPhotoInsideInAlbum = new File(photo);
                if (!mPhotoInsideInAlbum.isDirectory()
                        & mPhotoInsideInAlbum.isFile()
                        & Utils.isPhotoOrVideo(mPhotoInsideInAlbum.getAbsolutePath()) == 0) {
                    String PHOTO_IN_FOLDER_PATH = mPhotoInsideInAlbum.getParent();

                    String[] SPLIT_PHOTO = mPhotoInsideInAlbum.getParent().split("/");
                    String ALBUM_NAME_OF_PHOTO = SPLIT_PHOTO[SPLIT_PHOTO.length - 1];

                    /**
                     * Add album into array list
                     */
                    // Check if album not exists before, put that album into array list
                    if (!mAlAlbumPhotoFolderPath.contains(PHOTO_IN_FOLDER_PATH)) {
                        /**
                         * Including into Album object by :
                         * - Extract bitmap of latest file
                         * - Get file count of one album
                         */
                        File mFilePhotoFolder = new File(PHOTO_IN_FOLDER_PATH);

                        // When get the number of photos, need also
                        // include photos in sub-folder
                        File mPhotoList[] = mFilePhotoFolder.listFiles();

                        ArrayList<File> mAlPhoto = new ArrayList<>();
                        for (int i = 0; i < mPhotoList.length; i++) {
                            mAlPhoto.add(mPhotoList[i]);
                        }
                        for (int i = mAlPhoto.size() - 1; i > 0; i--) {
                            if (Utils.isPhotoOrVideo(mAlPhoto.get(i).getAbsolutePath()) != 0)
                                mAlPhoto.remove(i);
                        }

                        int number_of_photos = getNumberOfFiles(true, PHOTO_IN_FOLDER_PATH);

                        // Add item into Array list Photos
                        Album album = new Album(
                                getActivity(), ALBUM_NAME_OF_PHOTO, dateTaken, PHOTO_IN_FOLDER_PATH, true);

                        // Set photo number
                        album.setNumberOfPhotos(number_of_photos);

                        // Get bitmap
                        increase_index_of_file = 0;
                        getBitmap(true, album, mAlPhoto);

                        // Add item Album object into array list
                        mAlAlbums.add(album);

                        // Add album name into array list also
                        mAlAlbumPhotoFolderPath.add(PHOTO_IN_FOLDER_PATH);

                        // Add all album names into this array list to check
                        // and put video into correct album name
                        mAlAlbumNames.add(ALBUM_NAME_OF_PHOTO);
                    }
                }
            } while (mCursorPhotos.moveToNext());
        }

        /**
         * Move in Array Video
         */
        if (mCursorVideos.moveToFirst()) {
            String dateTaken, video;

            int dateTakenColumn = mCursorVideos.getColumnIndex(
                    MediaStore.Video.Media.DATE_TAKEN);
            int videoColumn = mCursorVideos.getColumnIndex(
                    MediaStore.Video.Media.DATA);

            dateTaken = mCursorVideos.getString(dateTakenColumn);
            video = mCursorVideos.getString(videoColumn);

            File mVideoInsideInAlbum = new File(video);

            // todo
            Log.i("", "getAbsolutePath " + mVideoInsideInAlbum.getAbsolutePath());

            if (mVideoInsideInAlbum.isFile()
                    & Utils.isPhotoOrVideo(mVideoInsideInAlbum.getAbsolutePath()) == 1) {
                String VIDEO_IN_FOLDER_PATH = mVideoInsideInAlbum.getParent();

                String[] SPLIT_VIDEO = mVideoInsideInAlbum.getParent().split("/");
                String ALBUM_NAME_OF_VIDEO = SPLIT_VIDEO[SPLIT_VIDEO.length - 1];

                /**
                 * Add album into array list
                 */
                // Add album into array list
                // Check if album not exists before, put that album into array list
                if (!mAlAlbumVideoFolderPath.contains(VIDEO_IN_FOLDER_PATH)) {
                    /**
                     * Including into Album object by :
                     * - Extract bitmap of latest file
                     * - Get file count of one album
                     */
                    File mFileVideoFolder = new File(VIDEO_IN_FOLDER_PATH);
                    File[] mVideoList = mFileVideoFolder.listFiles();

                    ArrayList<File> mAlVideo = new ArrayList<>();

                    Log.i("", "Video list size - " + mVideoList.length);
                    for (int i = 0; i < mVideoList.length; i++) {
                        mAlVideo.add(mVideoList[i]);
                    }
                    for (int i = mAlVideo.size() - 1; i > 0; i--) {
                        Log.i("", mAlVideo.get(i).getAbsolutePath() + " - "
                                + Utils.isPhotoOrVideo(mAlVideo.get(i).getAbsolutePath()));
                        if (Utils.isPhotoOrVideo(mAlVideo.get(i).getAbsolutePath()) != 1) {
                            mAlVideo.remove(i);
                        }
                    }

                    int number_of_videos = getNumberOfFiles(false, VIDEO_IN_FOLDER_PATH);

                    /**
                     * Should check album name already existed or not before
                     * when putting files into array list photo,
                     * to avoid duplicate album name
                     * - If not exist, can add new Album name with new video
                     * - if already existed, just set Number of videos again.
                     *
                     */
                    if (!mAlAlbumNames.contains(ALBUM_NAME_OF_VIDEO)) {
                        // If not exists
                        // Add item into Array list Videos
                        Album album = new Album(
                                getActivity(), ALBUM_NAME_OF_VIDEO, dateTaken, VIDEO_IN_FOLDER_PATH, false);

                        // Set video number
                        album.setNumberOfVideos(number_of_videos);

                        // Get bitmap
                        increase_index_of_file = 0;
                        getBitmap(false, album, mAlVideo);

                        // Add item Album object into array list
                        mAlAlbums.add(album);
                    } else {
                        // If already existed

                        // Set Number of videos for that album
                        int j = mAlAlbumNames.indexOf(ALBUM_NAME_OF_VIDEO);
                        mAlAlbums.get(j).setNumberOfVideos(number_of_videos);

                        // Set Bitmap for latest video file if recognize that
                        // the taken date of video file is after than of photo
                        if (Long.valueOf(dateTaken) > Long.valueOf(mAlAlbums.get(j).getDateTaken())) {
                            increase_index_of_file = 0;
                            getBitmap(false, mAlAlbums.get(j), mAlVideo);
                        }
                    }

                    // Add album name into array list also
                    mAlAlbumVideoFolderPath.add(VIDEO_IN_FOLDER_PATH);
                }
            }
        }

        return mAlAlbums;
    }

//    private void getBitmap(boolean is_photo_or_video, Album album, File[] mFileList) {
//        try {
//            Bitmap mBitmapFolderThumbnail = null;
//            String FILE_PATH = mFileList[mFileList.length - 1 - increase_index_of_file].getAbsolutePath();
//            File mFile = new File(FILE_PATH);
//
//            // Get bitmap
//            if (is_photo_or_video
//                    & mFile.isFile()
//                    & !mFile.isDirectory()
//                    & !mFile.isHidden()
//                    & Utils.isPhotoOrVideo(FILE_PATH) == 0) {
//                // Photos
//                mBitmapFolderThumbnail = Utils.getThumbnail(
//                        getActivity(), true,
//                        null,
//                        FILE_PATH);
//            } else if (!is_photo_or_video
//                    & mFile.isFile()
//                    & !mFile.isDirectory()
//                    & !mFile.isHidden()
//                    & Utils.isPhotoOrVideo(FILE_PATH) == 1) {
//                // Videos
//                Log.i("", increase_index_of_file + " " + mFileList.length + " Video Path - " + FILE_PATH);
//
//                mBitmapFolderThumbnail = Utils.getThumbnail(
//                        getActivity(), false,
//                        null,
//                        FILE_PATH);
//            } else {
//                increase_index_of_file++;
//
////                Log.i("", pos + " INCREASE " + increase_index_of_file + " " + (mFileList.length - 1));
//                if (increase_index_of_file < mFileList.length - 1)
//                    getBitmap(is_photo_or_video, album, mFileList);
//            }
//
//            // Save bitmap folder thumbnail
//            album.setBitmapFolderThumbnail(mBitmapFolderThumbnail);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void getBitmap(boolean is_photo_or_video, Album album, ArrayList<File> mFileList) {
        try {
            Bitmap mBitmapFolderThumbnail = null;
            String FILE_PATH = mFileList.get(mFileList.size() - 1 - increase_index_of_file).getAbsolutePath();
            File mFile = new File(FILE_PATH);

            // Get bitmap
            if (is_photo_or_video
                    & mFile.isFile()
                    & !mFile.isDirectory()
                    & !mFile.isHidden()
                    & Utils.isPhotoOrVideo(FILE_PATH) == 0) {
                // Photos
                mBitmapFolderThumbnail = Utils.getThumbnail(
                        getActivity(), true,
                        null,
                        FILE_PATH);
            } else if (!is_photo_or_video
                    & mFile.isFile()
                    & !mFile.isDirectory()
                    & !mFile.isHidden()
                    & Utils.isPhotoOrVideo(FILE_PATH) == 1) {
                // Videos
                mBitmapFolderThumbnail = Utils.getThumbnail(
                        getActivity(), false,
                        null,
                        FILE_PATH);
            } else {
                increase_index_of_file++;
                if (increase_index_of_file < mFileList.size() - 1)
                    getBitmap(is_photo_or_video, album, mFileList);
            }

            // Save bitmap folder thumbnail
            album.setBitmapFolderThumbnail(mBitmapFolderThumbnail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getNumberOfFiles(boolean isPhotoOrVideo, String FOLDER_PATH) {
        ArrayList<File> mAl = new ArrayList<>();
        mAl = Utils.getPhotoAndVideoFromSdCard(mAl, new File(FOLDER_PATH));

        /**
         * Check to put only photo, video files to show in Custom Gallery
         */
        int number_of_files = 0;
        for (int j = 0; j < mAl.size(); j++) {
            // If the File Name is picture, increase photos++
            // If the File Name is video, increase videos++
            if (isPhotoOrVideo) {
                if (mAl.get(j).isFile()
                        && Utils.isPhotoOrVideo(mAl.get(j).getName()) == 0) {
                    number_of_files++;
                }
            } else {
                if (mAl.get(j).isFile()
                        && Utils.isPhotoOrVideo(mAl.get(j).getName()) == 1) {
                    number_of_files++;
                }
            }
        }

        return number_of_files;
    }
}
