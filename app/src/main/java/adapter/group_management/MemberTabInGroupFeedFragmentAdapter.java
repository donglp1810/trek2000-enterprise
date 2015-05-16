package adapter.group_management;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.trek2000.android.enterprise.R;

import java.io.File;
import java.util.ArrayList;

import custom_view.MarkableImageView;
import de.hdodenhof.circleimageview.CircleImageView;
import model.Album;
import ui.fragment.custom.gallery.AlbumInsideFragment;

/**
 * Created by trek2000 on 17/10/2014.
 */
public class MemberTabInGroupFeedFragmentAdapter extends BaseAdapter {

    /**
     * Data section
     */
    private ArrayList<String> mAlEx = new ArrayList<String>();

    /**
     * String section
     */
    private int textViewResourceId;
    private DisplayImageOptions mDio;
    private ViewHolder viewHolder;
    /**
     * The other section
     */
    private Context mContext;

    private class ViewHolder {
        private CircleImageView circleIvItem;
        private ImageView mIvPhoto;
        private ImageView mIvVideo;
        private TextView mTvMemberName;
        private TextView mTvPhotoNumber;
        private TextView mTvVideoNumber;
    }

    public MemberTabInGroupFeedFragmentAdapter(Context context, int textViewResourceId, ArrayList<String> mAlEx ) {
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

    @Override
    public int getCount() {
        return mAlEx.size();
    }

    @Override
    public String getItem(int position) {
        return mAlEx.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        // Initialize variables
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(textViewResourceId, null);

            viewHolder.circleIvItem = (CircleImageView) convertView.findViewById(
                    R.id.circleiv_item_in_simple_list_item_in_member_tab_in_group_feed_fragment);
            viewHolder.mIvPhoto = (ImageView) convertView.findViewById(
                    R.id.iv_photo_in_simple_list_item_in_member_tab_in_group_feed_fragment);
            viewHolder.mIvVideo = (ImageView) convertView.findViewById(
                    R.id.iv_video_in_simple_list_item_in_member_tab_in_group_feed_fragment);
            viewHolder.mTvMemberName = (TextView) convertView.findViewById(
                    R.id.tv_member_name_in_simple_list_item_in_member_tab_in_group_feed_fragment);
            viewHolder.mTvPhotoNumber = (TextView) convertView.findViewById(
                    R.id.tv_photo_number_in_simple_list_item_in_member_tab_in_group_feed_fragment);
            viewHolder.mTvVideoNumber = (TextView) convertView.findViewById(
                    R.id.tv_video_number_in_simple_list_item_in_member_tab_in_group_feed_fragment);

            viewHolder.circleIvItem.setTag(pos);
            viewHolder.mIvPhoto.setTag(pos);
            viewHolder.mIvVideo.setTag(pos);
            viewHolder.mTvMemberName.setTag(pos);
            viewHolder.mTvPhotoNumber.setTag(pos);
            viewHolder.mTvVideoNumber.setTag(pos);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

            viewHolder.circleIvItem.setTag(pos);
            viewHolder.mIvPhoto.setTag(pos);
            viewHolder.mIvVideo.setTag(pos);
            viewHolder.mTvMemberName.setTag(pos);
            viewHolder.mTvPhotoNumber.setTag(pos);
            viewHolder.mTvVideoNumber.setTag(pos);
        }

        return convertView;
    }

    /**
     * Basic methods
     */
}
