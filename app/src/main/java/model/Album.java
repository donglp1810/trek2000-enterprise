package model;

import android.content.Context;
import android.graphics.Bitmap;

import com.trek2000.android.enterprise.R;


/**
 * Created by trek2000 on 12/2/2015.
 */
public class Album {

    /**
     * String section
     */
    private String ALBUM_NAME = null;
    private String FOLDER_PATH = null;

    private Bitmap mBitmapFolderThumbnail;

    private boolean media_type_of_latest_bitmap_folder_thumbnail = true;
    private int number_of_photos = 0;
    private int number_of_videos = 0;

    /**
     * The others section
     */
    private Context mContext;

    public Album(
            Context mContext, String ALBUM_NAME, String FOLDER_PATH,
            Bitmap mBitmapFolderThumbnail,
            boolean media_type_of_latest_bitmap_folder_thumbnail,
            int number_of_photos, int number_of_videos) {
        this.mContext = mContext;

        this.ALBUM_NAME = ALBUM_NAME;
        this.FOLDER_PATH = FOLDER_PATH;

        this.mBitmapFolderThumbnail = mBitmapFolderThumbnail;

        this.media_type_of_latest_bitmap_folder_thumbnail = media_type_of_latest_bitmap_folder_thumbnail;
        this.number_of_photos = number_of_photos;
        this.number_of_videos = number_of_videos;
    }

    public String getAlbumName() {
        return ALBUM_NAME;
    }

    public String getFolderPath() {
        return FOLDER_PATH;
    }

    public Bitmap getBitmapFolderThumbnail() {
        return mBitmapFolderThumbnail;
    }

    public boolean getMediaTypeOfLatestBitmapFolderThumbnail() {
        return media_type_of_latest_bitmap_folder_thumbnail;
    }

    public String getNumberOfPhotos() {
        if (number_of_photos == 0 | number_of_photos == 1)
            return number_of_photos + " " + mContext.getString(R.string.photo);
        else
            return number_of_photos + " " + mContext.getString(R.string.photos);
    }

    public String getNumberOfVideos() {
        if (number_of_videos == 0 | number_of_videos == 1)
            return number_of_videos + " " + mContext.getString(R.string.video);
        else
            return number_of_videos + " " + mContext.getString(R.string.videos);
    }
}
