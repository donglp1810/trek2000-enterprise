package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.trek2000.android.enterprise.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import define.API;
import define.MediaType;

public class DownloadImageAsync extends AsyncTask<String, ImageView, Bitmap> {

    /**
     * Data section
     */
    public static ArrayList<Bitmap> mAlBitmap = new ArrayList<Bitmap>();

    /**
     * Interface section
     */

    /**
     * String section
     */
    public static int CONTENT_TYPE = 111;

    /**
     * View section
     */
    private ImageView mIv;

    /**
     * The other section
     */
    private Context mContext;

    /**
     * View section
     */

    /**
     * @param mContext markableImageView
     */
    public DownloadImageAsync(Context mContext, ImageView mIv, int contentType) {
        this.mContext = mContext;

        this.mIv = mIv;

        this.CONTENT_TYPE = contentType;

        // todo Always reset image to avoid load Old Image
//        Cloudstringers.fileItem.setImage(null);
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String URL = urls[0];

        /**
         * If user Development site, need change from HTTPS protocol to HTTP protocol
         */
        if (!API.IS_DEV_SITE_OR_PRODUCT_SITE) {
            URL = URL.replace("https", "http");
        }

        Bitmap mBitmap = null;
        InputStream mIs = null;
        try {
            mIs = new java.net.URL(URL).openStream();

            /**
             * OutOfMemory occur in here, need decrease image size
             *
             * Solution : scaled correctly
             *
             * is - The input stream that holds the raw data to be decoded into a bitmap.
             *
             * outPadding - If not null, return the padding rect for the bitmap if it exists,
             * otherwise set padding to [-1,-1,-1,-1]. If no bitmap is returned (null) then padding is unchanged.
             *
             * opts - null-ok; Options that control downsampling and whether the image should be completely decoded,
             * or just is size returned.
             */
            mBitmap = BitmapFactory.decodeStream(mIs);

            // Add into array Bitmaps
            mAlBitmap.add(mBitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mIs != null) {
                try {
                    mIs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return mBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap mBitmap) {
        super.onPostExecute(mBitmap);

        /**
         * Check Content type to show image in Detail page or Home page
         */
        if (CONTENT_TYPE == MediaType.PHOTO | CONTENT_TYPE == MediaType.VIDEO) {
            /**
             * Detail page
             */
            if (mIv != null & mBitmap != null) {
                /**
                 * Save bitmap into single way
                 */
//                Cloudstringers.fileItem.setImage(mBitmap);

                /**
                 * Avoid OutOfMemory here
                 */
                try {
                    if (mContext != null) {
                        if (mBitmap.getWidth() > mBitmap.getHeight()) {
                            /**
                             * Landscape
                             */

                            mIv.setImageBitmap(Bitmap.createScaledBitmap(
                                    mBitmap,
                                    Utils.getSizeOfScreen(mContext)[0],
                                    Utils.getSizeOfScreen(mContext)[0] * mBitmap.getHeight() / mBitmap.getWidth(), true));
                        } else {
                            /**
                             * Portrait
                             */
                            mIv.setImageBitmap(Bitmap.createScaledBitmap(
                                    mBitmap,
                                    Utils.getSizeOfScreen(mContext)[0] * mBitmap.getWidth() / mBitmap.getHeight(),
                                    Utils.getSizeOfScreen(mContext)[0], true));
                        }
                    }
                    // Set Bitmap for Image view
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            /**
             * Home page
             */
            if (mBitmap != null) {
                try {
                    // Set Bitmap for Image view
                    mIv.setImageBitmap(
                            Utils.compressImage(
                                    Bitmap.createScaledBitmap(
                                            mBitmap,
                                            mContext.getResources().getInteger(R.integer.width_file_view),
                                            mContext.getResources().getInteger(R.integer.height_file_view),
                                            false)));
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
            }
        }
    }
}