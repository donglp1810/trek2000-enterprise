package adapter.group_management;

/**
 * Created by Dong Le on 5/15/2015.
 */
import com.trek2000.android.enterprise.R;
import com.viewpagerindicator.IconPagerAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import ui.fragment.group_management.MemberTabInGroupFeedFragment;
import ui.fragment.group_management.PhotoAndVDOTabInGroupFeedFragment;
import ui.fragment.group_management.SettingTabInGroupFeedFragment;

public class GroupFeedFragmentPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter{
    /**
     * View session
     */
    private Context mContext;

    public GroupFeedFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getIconResId(int index) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Fragment getItem(int position)
    {
        // TODO Auto-generated method stub
        Fragment fragment = null;
        switch(position){
            case 0:
                Log.i("", "getItem 0: " + position);
                fragment = new PhotoAndVDOTabInGroupFeedFragment();
                break;
            case 1:
                Log.i("", "getItem 1: " + position);
                fragment = new MemberTabInGroupFeedFragment();
                break;
            case 2:
                Log.i("", "getItem 2: " + position);
                fragment = new SettingTabInGroupFeedFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position){
        String title = "";
        switch(position){
            case 0:
                title = mContext.getString(R.string.photo_and_vdo) + "(45)";
                break;
            case 1:
                title = mContext.getString(R.string.member) + "(12)";
                break;
            case 2:
                title = mContext.getString(R.string.setting);
                break;
        }
        return title;
    }
}
