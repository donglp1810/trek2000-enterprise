package adapter.company;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.trek2000.android.enterprise.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import model.AllCompanyFeedItem;


/**
 * Created by trek2000 on 21/8/2014.
 */
public class CompanyFeedAdapter extends BaseAdapter {

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
    private GridViewCompanyFeedAdapter gridViewCompanyFeedAdapter = null;

    private class ViewHolder {
        private CircleImageView circleIvAvatar;
        private GridView mGv;
        private TextView mTvElapsedTime;
        private TextView mTvFileNumberUploaded;
        private TextView mTvGroupName;
        private TextView mTvUserName;
    }

    /**
     * @param context
     */
    public CompanyFeedAdapter(Context context, int textViewResourceId, ArrayList<AllCompanyFeedItem> mAlEx) {
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
            viewHolder.circleIvAvatar = (CircleImageView) convertView.findViewById(R.id.circleiv_avatar_in_simple_list_item_company_feed);
            viewHolder.mGv= (GridView) convertView.findViewById(R.id.gv_in_simple_list_item_company_feed);
            viewHolder.mTvElapsedTime = (TextView) convertView.findViewById(R.id.tv_elapsed_time_in_simple_list_item_company_feed);
            viewHolder.mTvFileNumberUploaded = (TextView) convertView.findViewById(R.id.tv_file_number_uploaded_in_simple_list_item_company_feed);
            viewHolder.mTvGroupName = (TextView) convertView.findViewById(R.id.tv_group_name_in_simple_list_item_company_feed);
            viewHolder.mTvUserName = (TextView) convertView.findViewById(R.id.tv_user_name_in_simple_list_item_company_feed);

            viewHolder.circleIvAvatar.setTag(position);
            viewHolder.mGv.setTag(position);
            viewHolder.mTvElapsedTime.setTag(position);
            viewHolder.mTvFileNumberUploaded.setTag(position);
            viewHolder.mTvGroupName.setTag(position);
            viewHolder.mTvUserName.setTag(position);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();

            viewHolder.circleIvAvatar.setTag(position);
            viewHolder.mGv.setTag(position);
            viewHolder.mTvElapsedTime.setTag(position);
            viewHolder.mTvFileNumberUploaded.setTag(position);
            viewHolder.mTvGroupName.setTag(position);
            viewHolder.mTvUserName.setTag(position);
        }
        //set data
        viewHolder.circleIvAvatar.setImageResource(R.drawable.iv_default_avatar);
        gridViewCompanyFeedAdapter = new GridViewCompanyFeedAdapter(mContext, mAlEx.get(position).getmAlExChil());
        viewHolder.mGv.setAdapter(gridViewCompanyFeedAdapter);
        return convertView;
    }

}