package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.trek2000.android.enterprise.Enterprise;
import com.trek2000.android.enterprise.R;

import java.io.File;
import java.util.ArrayList;

import custom_view.MarkableImageView;

/**
 * Created by trek2000 on 21/8/2014.
 */

public class AlbumInsideAdapter extends BaseAdapter {

    /**
     * Data section
     */
    private ArrayList<File> mAlAlbumItem = new ArrayList<File>();

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

    public AlbumInsideAdapter(Context context, int textViewResourceId, ArrayList<File> mAlAlbumItem) {
        this.mContext = context;
        this.textViewResourceId = textViewResourceId;
        this.mAlAlbumItem = mAlAlbumItem;

        mDio = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.color.black)
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
        Log.i("", "getCount " + mAlAlbumItem.size());

        return mAlAlbumItem.size();
    }

    /**
     * @param position
     * @return
     */
    public File getItem(int position) {
        return mAlAlbumItem.get(position);
    }

    /**
     * @param position
     * @return
     */
    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int pos, View convertView, ViewGroup parent) {
        /**
         * Begin draw items in Grid View
         */
        ViewHolder viewHolder = null;
        if (convertView == null) {  // if it's not recycled, initialize some attributes\
            // draw view item
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(textViewResourceId, null);

            // initial element of view item
            viewHolder = new ViewHolder();
            viewHolder.mIvItem = (MarkableImageView) convertView.findViewById(
                    R.id.iv_in_simple_grid_item_in_fragment_album_inside);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Should load file from sd card by parsing Uri
        String FILE_PATH = mAlAlbumItem.get(pos).getAbsolutePath();

        Log.i("", "FILE_PATH " + FILE_PATH);

        // Display image
        Enterprise.imageLoader.displayImage(
                Uri.fromFile(new File(FILE_PATH)).toString(),
                viewHolder.mIvItem, mDio,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (loadedImage != null & view != null) {
                            // todo
                            ((MarkableImageView) view).setImageResource(R.drawable.iv_default_avatar);
//                            ((ImageView) view).setImageBitmap(Bitmap.createScaledBitmap(
//                                    loadedImage,
//                                    mContext.getResources().getInteger(R.integer.width_file_view),
//                                    mContext.getResources().getInteger(R.integer.height_file_view),
//                                    false));
                        }
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(
                            String imageUri, View view, int current, int total) {
                    }
                });

        return convertView;
    }

    /**
     * View section
     */
    private class ViewHolder {
        MarkableImageView mIvItem;
    }
}
