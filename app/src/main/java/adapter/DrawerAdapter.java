package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.trek2000.android.enterprise.R;

import java.util.ArrayList;

import define.Position;
import model.DrawerItem;

/**
 * Created by trek2000 on 17/10/2014.
 */
public class DrawerAdapter extends BaseAdapter {

    /**
     * Data section
     */
    private ArrayList<DrawerItem> mAl = new ArrayList<DrawerItem>();

    /**
     * String section
     */
    private int textViewResourceId;

    /**
     * The other section
     */
    private Context mContext;

    public DrawerAdapter(Context context, int textViewResourceId, ArrayList<DrawerItem> mAl) {
        this.mContext = context;
        this.textViewResourceId = textViewResourceId;

        this.mAl = mAl;
    }

    @Override
    public int getCount() {
        return mAl.size();
    }

    @Override
    public DrawerItem getItem(int position) {
        return mAl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        // Initialize variables
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater mLiInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mLiInflater.inflate(textViewResourceId, null);

            viewHolder.mIvIcon = (ImageView) convertView.findViewById(
                    R.id.iv_in_simple_list_item_in_fragment_navigation_drawer_in_company_name);
            viewHolder.mTvTitle = (TextView) convertView.findViewById(
                    R.id.tv_title_in_simple_list_item_in_fragment_navigation_drawer_in_company_name);
            viewHolder.mTvNumberOfNotifications = (TextView) convertView.findViewById(
                    R.id.tv_number_of_notifications_in_fragment_navigation_drawer_in_company_name);

            viewHolder.mIvIcon.setTag(pos);
            viewHolder.mTvTitle.setTag(pos);
            viewHolder.mTvNumberOfNotifications.setTag(pos);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

            viewHolder.mIvIcon.setTag(pos);
            viewHolder.mTvTitle.setTag(pos);
            viewHolder.mTvNumberOfNotifications.setTag(pos);
        }

        // Set icon
        /**
         * If item currently be selected, change these things:
         * - Icon
         * - Text
         * - Rectangle of text view color
         * - Rectangle of text view background color
         */
        setData(pos, mAl.get(pos).isSelected(), (ImageView) viewHolder.mIvIcon.findViewWithTag(pos));

        // Set Title & title color
        if (mAl.get(pos).isSelected())
            ((TextView) viewHolder.mTvTitle.findViewWithTag(pos)).setTextColor(
                    mContext.getResources().getColor(android.R.color.black));
        else
            ((TextView) viewHolder.mTvTitle.findViewWithTag(pos)).setTextColor(
                    mContext.getResources().getColor(android.R.color.white));

        // set title
        ((TextView) viewHolder.mTvTitle.findViewWithTag(pos))
                .setText(mAl.get(pos).getTitle());

        // Set number of notifications
        if (mAl.get(pos).getNumberOfNotifications() < 0)
            // Should hide views have value < 0
            ((TextView) viewHolder.mTvNumberOfNotifications.findViewWithTag(pos))
                    .setVisibility(View.GONE);
        else
            ((TextView) viewHolder.mTvNumberOfNotifications.findViewWithTag(pos)).setText(
                    mAl.get(pos).getNumberOfNotifications() + "");

        // Notifications line should set orange background for Number Of Notifications
        if (pos == 1) {
            ((TextView) viewHolder.mTvNumberOfNotifications.findViewWithTag(pos))
                    .setBackgroundResource(R.drawable.custom_tv_number_of_notifications_in_notifications);
            ((TextView) viewHolder.mTvNumberOfNotifications.findViewWithTag(pos))
                    .setTextColor(mContext.getResources().getColor(android.R.color.white));
        }

        return convertView;
    }

    private void setData(int pos, boolean selected, ImageView mIv) {
        switch (pos) {
            case Position.card_management:
                if (selected)
                    mIv.setImageResource(R.drawable.iv_card_management_selected);
                else
                    mIv.setImageResource(R.drawable.iv_card_management_unselected);

                // Set title also
                mAl.get(pos).setTitle(mContext.getString(R.string.card_management));
                break;
            case Position.feeds:
                if (selected)
                    mIv.setImageResource(R.drawable.iv_feeds_selected);
                else
                    mIv.setImageResource(R.drawable.iv_feeds_unselected);

                // Set title also
                mAl.get(pos).setTitle(mContext.getString(R.string.feeds));
                break;
            case Position.group_management:
                if (selected)
                    mIv.setImageResource(R.drawable.iv_group_management_selected);
                else
                    mIv.setImageResource(R.drawable.iv_group_management_unselected);

                // Set title also
                mAl.get(pos).setTitle(mContext.getString(R.string.group_management));
                break;
            case Position.notifications:
                if (selected)
                    mIv.setImageResource(R.drawable.iv_notifications_selected);
                else
                    mIv.setImageResource(R.drawable.iv_notifications_unselected);

                // Set title also
                mAl.get(pos).setTitle(mContext.getString(R.string.notifications));
                break;
            case Position.user_management:
                if (selected)
                    mIv.setImageResource(R.drawable.iv_user_management_selected);
                else
                    mIv.setImageResource(R.drawable.iv_user_management_unselected);

                // Set title also
                mAl.get(pos).setTitle(mContext.getString(R.string.user_management));
                break;
        }
    }

    private class ViewHolder {
        private ImageView mIvIcon;
        private TextView mTvTitle;
        private TextView mTvNumberOfNotifications;
    }

    /**
     * Basic methods
     */
}
