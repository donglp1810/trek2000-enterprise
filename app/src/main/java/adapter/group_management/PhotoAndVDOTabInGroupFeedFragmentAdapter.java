package adapter.group_management;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.trek2000.android.enterprise.Enterprise;
import com.trek2000.android.enterprise.R;

import java.util.ArrayList;

import adapter.company.GridViewAllCompanyFeedAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import model.AllCompanyFeedItem;
import ui.fragment.company.CompanyFeedFragment;
import ui.fragment.group_management.GroupFeedFragment;


/**
 * Created by trek2000 on 21/8/2014.
 */
public class PhotoAndVDOTabInGroupFeedFragmentAdapter extends BaseAdapter {

    /**
     * Data section
     */
    private ArrayList<AllCompanyFeedItem> mAlEx = new ArrayList<AllCompanyFeedItem>();

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
    private ViewHolder viewHolder;
    private GridViewPhotoAndVDOTabInGroupFeedFragmentAdapter gridViewPhotoAndVDOTabInGroupFeedFragmentAdapter = null;

    private class ViewHolder {
        private GridView mGv;
        private TextView mTvElapsedTime;
        private TextView mTvFileNumber;
    }

    /**
     * @param context
     */
    public PhotoAndVDOTabInGroupFeedFragmentAdapter(Context context, int textViewResourceId, ArrayList<AllCompanyFeedItem> mAlEx) {
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
    public AllCompanyFeedItem getItem(int position) {
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

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(textViewResourceId, null);
            viewHolder.mGv= (GridView) convertView.findViewById(R.id.gv_in_simple_list_photo_and_vdo_tab_in_group_feed_fragment);
            viewHolder.mTvElapsedTime = (TextView) convertView.findViewById(R.id.tv_elapsed_time_in_simple_list_photo_and_vdo_tab_in_group_feed_fragment);
            viewHolder.mTvFileNumber = (TextView) convertView.findViewById(R.id.tv_file_number_in_simple_list_photo_and_vdo_tab_in_group_feed_fragment);

            viewHolder.mGv.setTag(position);
            viewHolder.mTvElapsedTime.setTag(position);
            viewHolder.mTvFileNumber.setTag(position);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();

            viewHolder.mGv.setTag(position);
            viewHolder.mTvElapsedTime.setTag(position);
            viewHolder.mTvFileNumber.setTag(position);
        }
        //set data
        gridViewPhotoAndVDOTabInGroupFeedFragmentAdapter = new GridViewPhotoAndVDOTabInGroupFeedFragmentAdapter(mContext, mAlEx.get(position).getmAlExChil());
        viewHolder.mGv.setAdapter(gridViewPhotoAndVDOTabInGroupFeedFragmentAdapter);
        //on click listener
        return convertView;
    }

}