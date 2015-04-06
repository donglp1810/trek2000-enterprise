package singleton;

import android.graphics.Bitmap;

import utils.DownloadImageAsync;

/**
 * Created by trek2000 on 18/8/2014.
 */
public class Item {

    /**
     * Single Ton method
     *
     * @return
     */
    private static Item item = null;
    /**
     * Profile Information
     */
    private int content_type = DownloadImageAsync.CONTENT_TYPE;
    //position item
    private int POSITION = 0;
    private String VIDEO_PREVIEW_MEDIUM = null;
    /**
     * View section
     */
    private Bitmap mBitmapImage;

    public static Item getInstance() {
        if (item == null) {
            item = new Item();
        }
        return item;
    }

    public Bitmap getImage() {
        return mBitmapImage;
    }

    public void setImage(Bitmap mBitmapImage) {
        this.mBitmapImage = mBitmapImage;
    }

    public int getPosition() {
        return POSITION;
    }

    public void setPosition(int pos) {
        this.POSITION = pos;
    }

}
