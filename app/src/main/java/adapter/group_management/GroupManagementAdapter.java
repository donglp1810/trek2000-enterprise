package adapter.group_management;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.trek2000.android.enterprise.Enterprise;
import com.trek2000.android.enterprise.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ui.fragment.company.AllCompanyFeedFragment;
import ui.fragment.company.CompanyFeedFragment;
import ui.fragment.group_management.GroupFeedFragment;


/**
 * Created by trek2000 on 21/8/2014.
 */
public class GroupManagementAdapter extends BaseAdapter {

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
    private ViewHolder viewHolder;

    private class ViewHolder {
        private CircleImageView circleIvItem;
        private TextView mTvGroupName;
        private Button mBtnFileNumber;
    }

    /**
     * @param context
     */
    public GroupManagementAdapter(Context context, int textViewResourceId, ArrayList<String> mAlEx) {
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

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(textViewResourceId, null);
            viewHolder.circleIvItem = (CircleImageView) convertView.findViewById(R.id.circleiv_item_in_simple_list_item_group_management);
            viewHolder.mTvGroupName = (TextView) convertView.findViewById(R.id.tv_group_name_in_simple_list_item_group_management);
            viewHolder.mBtnFileNumber = (Button) convertView.findViewById(R.id.btn_file_number_in_simple_list_item_group_management);

            viewHolder.circleIvItem.setTag(position);
            viewHolder.mTvGroupName.setTag(position);
            viewHolder.mBtnFileNumber.setTag(position);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();

            viewHolder.circleIvItem.setTag(position);
            viewHolder.mTvGroupName.setTag(position);
            viewHolder.mBtnFileNumber.setTag(position);
        }

        //set data
        viewHolder.circleIvItem.setImageResource(R.drawable.iv_default_avatar);
        //on click listener
        viewHolder.circleIvItem.findViewWithTag(position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(
                        R.id.fl_in_activity_enterprise, GroupFeedFragment.newInstance()
                ).commitAllowingStateLoss();
                Enterprise.mTvTitle.setText("Group Name");
            }
        });
        return convertView;
    }
}