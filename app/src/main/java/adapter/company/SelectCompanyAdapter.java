package adapter.company;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.trek2000.android.enterprise.Enterprise;
import com.trek2000.android.enterprise.R;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ui.fragment.company.AllCompanyFeedFragment;
import ui.fragment.company.CompanyFeedFragment;


/**
 * Created by trek2000 on 21/8/2014.
 */
public class SelectCompanyAdapter extends BaseAdapter {

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
        private ImageView mIvBadge;
        private TextView mTvBadgeNumber;
        private TextView mTvCompanyName;
        private TextView mTvCompanyNumber;
    }

    /**
     * @param context
     */
    public SelectCompanyAdapter(Context context, int textViewResourceId, ArrayList<String> mAlEx) {
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
            viewHolder.mIvBadge = (ImageView) convertView.findViewById(R.id.iv_badge_in_simple_list_item_select_company);
            viewHolder.circleIvItem = (CircleImageView) convertView.findViewById(R.id.circleiv_item_in_simple_list_item_select_company);
            viewHolder.mTvBadgeNumber = (TextView) convertView.findViewById(R.id.tv_badge_number_in_simple_list_item_select_company);
            viewHolder.mTvCompanyName = (TextView) convertView.findViewById(R.id.tv_company_name_in_simple_list_item_select_company);
            viewHolder.mTvCompanyNumber = (TextView) convertView.findViewById(R.id.tv_company_number_in_simple_list_item_select_company);

            viewHolder.mIvBadge.setTag(position);
            viewHolder.circleIvItem.setTag(position);
            viewHolder.mTvBadgeNumber.setTag(position);
            viewHolder.mTvCompanyName.setTag(position);
            viewHolder.mTvCompanyNumber.setTag(position);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();

            viewHolder.mIvBadge.setTag(position);
            viewHolder.circleIvItem.setTag(position);
            viewHolder.mTvBadgeNumber.setTag(position);
            viewHolder.mTvCompanyName.setTag(position);
            viewHolder.mTvCompanyNumber.setTag(position);
        }

        //set data
        switch (position) {
            case 0:
                viewHolder.circleIvItem.setImageResource(R.drawable.btn_create_company);
                viewHolder.mIvBadge.setVisibility(View.GONE);
                viewHolder.mTvBadgeNumber.setVisibility(View.GONE);
                viewHolder.mTvCompanyName.setVisibility(View.GONE);
                viewHolder.mTvCompanyNumber.setVisibility(View.GONE);
                break;
            case 1:
                viewHolder.circleIvItem.setImageResource(R.drawable.iv_empty_album);
                viewHolder.mIvBadge.setVisibility(View.VISIBLE);
                viewHolder.mTvBadgeNumber.setVisibility(View.VISIBLE);
                viewHolder.mTvCompanyName.setVisibility(View.VISIBLE);
                viewHolder.mTvCompanyNumber.setVisibility(View.VISIBLE);
                viewHolder.mTvCompanyName.setText(mContext.getString(R.string.all_company));
                break;
                default:
                    viewHolder.circleIvItem.setImageResource(R.drawable.iv_default_avatar);
                    viewHolder.mIvBadge.setVisibility(View.GONE);
                    viewHolder.mTvBadgeNumber.setVisibility(View.GONE);
                    viewHolder.mTvCompanyName.setVisibility(View.VISIBLE);
                    viewHolder.mTvCompanyNumber.setVisibility(View.GONE);
        }

        //on click listener
        viewHolder.circleIvItem.findViewWithTag(position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 0:
                        //create new company

                        break;
                    case 1:
                        // go to All company feed
                        ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(
                                R.id.fl_in_activity_enterprise, AllCompanyFeedFragment.newInstance()
                        ).commitAllowingStateLoss();
                        Enterprise.mTvTitle.setText(mContext.getString(R.string.all_company));
                        break;
                    default:
                        // go to company feed
                        ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(
                                R.id.fl_in_activity_enterprise, CompanyFeedFragment.newInstance()
                        ).commitAllowingStateLoss();
                        Enterprise.mTvTitle.setText("Company Name");
                }
            }
        });
        return convertView;
    }

}