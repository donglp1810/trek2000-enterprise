package adapter.group_management;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.trek2000.android.enterprise.R;

import java.util.ArrayList;

import custom_view.MarkableImageView;


/**
 * Created by trek2000 on 21/8/2014.
 */
public class GridViewPhotoAndVDOTabInGroupFeedFragmentAdapter extends BaseAdapter {

    /**
     * Data section
     */
    private ArrayList<String> mAlEx = new ArrayList<String>();

    /**
     * Interface section
     */

    /**
     * String section
     */
    private int textViewResourceId;
    /**
     * The other section
     */
    private Context mContext;
    private DisplayImageOptions mDio;

    /**
     * @param context
     */
    public GridViewPhotoAndVDOTabInGroupFeedFragmentAdapter(Context context, ArrayList<String> mAlEx) {
        this.mContext = context;
        this.textViewResourceId = textViewResourceId;
        this.mAlEx = mAlEx;

        mDio = new DisplayImageOptions.Builder()
                .showImageOnLoading(mContext.getResources().getColor(R.color.black))
                .delayBeforeLoading(1000)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    /**
     * @return
     */
    public int getCount() {
        return mAlEx.size();
    }

    /**
     * @param position
     * @return
     */
    public String getItem(int position) {
        return mAlEx.get(position);
    }

    /**
     * @param position
     * @return
     */
    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        /**
         * Begin draw items in Grid View
         */
        MarkableImageView markableImageView = null;
        if (convertView == null) {  // if it's not recycled, initialize some attributes\
            markableImageView = new MarkableImageView(mContext);
            markableImageView.setLayoutParams(new GridView.LayoutParams(
                    mContext.getResources().getInteger(R.integer.height_file_view),
                    mContext.getResources().getInteger(R.integer.width_file_view)));
            markableImageView.setPadding(0, 0, 0, 0);
            markableImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            markableImageView.setTag(position);

            convertView = (MarkableImageView) markableImageView;
        } else {
            if (convertView instanceof MarkableImageView) {
                markableImageView = (MarkableImageView) convertView;
                markableImageView.setTag(position);

//                /**
//                 * check item selected or not and draw image select
//                 */
//                if (mAlAlbumDetailItems.get(pos).isChecked()) {
//                    ((MarkableImageView) markableImageView.findViewWithTag(pos)).setChecked(true);
//                } else {
//                    ((MarkableImageView) markableImageView.findViewWithTag(pos)).setChecked(false);
//                }
            }
        }

        /**
         * Check case using Loading image while Uploading
         * or after completed get All items from server
         */
//            if (mAlAlbumDetailItems.get(pos).getSplash() != null) {
//                /**
//                 * Case : after completed get All items from server
//                 */
//                /**
//                 * Check current version is DEV site or PRODUCT site to use correctly link
//                 */
//                String URL = null;
//                if (!API.IS_DEV_SITE_OR_PRODUCT_SITE) {
//                    URL = mAlAlbumDetailItems.get(pos).getSplash().replace("https", "http");
//                } else {
//                    URL = mAlAlbumDetailItems.get(pos).getSplash();
//                }
//
//                Cloudstringers.imageLoader
//                        .displayImage(URL, markableImageView, mDio, new SimpleImageLoadingListener() {
//                            @Override
//                            public void onLoadingStarted(String imageUri, View view) {
//                            }
//
//                            @Override
//                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                            }
//
//                            @Override
//                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                                // Always check size not is zero
//                                if (!mAlAlbumDetailItems.isEmpty()) {
//                                    /**
//                                     * Check video or not to draw Play video button on it
//                                     */
//                                    if (mAlAlbumDetailItems.get(pos) != null) {
//                                        if (mAlAlbumDetailItems.get(pos).getContentType() == MediaType.VIDEO) {
//                                            if (((MarkableImageView) view.findViewWithTag(pos)) != null) {
//                                                ((MarkableImageView) view.findViewWithTag(pos)).setVideo(true);
//                                            }
//                                        } else {
//                                            if (((MarkableImageView) view.findViewWithTag(pos)) != null) {
//                                                ((MarkableImageView) view.findViewWithTag(pos)).setVideo(false);
//                                            }
//                                        }
//                                    }
//
//                                    if (loadedImage != null & view.findViewWithTag(pos) != null)
//                                        ((MarkableImageView) view.findViewWithTag(pos))
//                                                .setImageBitmap(Bitmap.createScaledBitmap(
//                                                        loadedImage,
//                                                        mContext.getResources().getInteger(R.integer.width_file_view),
//                                                        mContext.getResources().getInteger(R.integer.height_file_view), false));
//                                }
//                            }
//                        }, new ImageLoadingProgressListener() {
//                            @Override
//                            public void onProgressUpdate(
//                                    String imageUri, View view, int current, int total) {
//                            }
//                        });
//            } else {
                /**
                 * Case : Load uploading image
                 */
                MarkableImageView mivLoading = new MarkableImageView(mContext);
                mivLoading.setLayoutParams(new GridView.LayoutParams(
                        230,
                        230));
                mivLoading.setImageResource(R.drawable.iv_default_avatar);

//                final MediaController mc = new MediaController(mContext);
//                mc.setMediaPlayer((GifDrawable) mivLoading.getDrawable());
//                mc.setAnchorView(mivLoading);
//
//                mivLoading.setEnabled(false);

                convertView = (MarkableImageView) mivLoading;
//            }

        return convertView;
    }

}