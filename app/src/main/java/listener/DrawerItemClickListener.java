package listener;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.trek2000.android.enterprise.Enterprise;
import com.trek2000.android.enterprise.R;

import java.util.ArrayList;

import define.Position;
import model.DrawerItem;
import ui.fragment.drawer.NavigationDrawerFragment;

/**
 * Created by trek2000 on 12/2/2015.
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener {

    private ArrayList<DrawerItem> mAl = new ArrayList<DrawerItem>();

    private Context mContext;

    public DrawerItemClickListener(Context mContext, ArrayList<DrawerItem> mAl) {
        this.mContext = mContext;
        this.mAl = mAl;
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        selectItem(mContext, position);
    }

    /**
     * Basic methods
     */

    private void setSelectedItem(int pos) {
        // Should reset before set new item was selected
        for (int i = 0; i < NavigationDrawerFragment.mAlDrawer.size(); i++)
            NavigationDrawerFragment.mAlDrawer.get(i).setSelected(false);

        // Set selected item
        NavigationDrawerFragment.mAlDrawer.get(pos).setSelected(true);

        // Notify data set changed.
        NavigationDrawerFragment.drawerAdapter.notifyDataSetChanged();
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(Context mContext, int pos) {
        // todo Create a new fragment and specify the planet to show based on position
        Fragment fragment = null;
        switch (pos) {
            case Position.card_management:
                fragment = new Fragment();
                break;
            case Position.feeds:
                fragment = new Fragment();
                break;
            case Position.group_management:
                fragment = new Fragment();
                break;
            case Position.notifications:
                fragment = new Fragment();
                break;
            case Position.user_management:
                fragment = new Fragment();
                break;
        }

//        Bundle args = new Bundle();
//        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_in_activity_enterprise, fragment)
                .commitAllowingStateLoss();

        // Highlight the selected item, update the title, and close the drawer
        // Set selected position
        setSelectedItem(pos);

        // Set title
        setTitle(mAl.get(pos).getTitle());

        NavigationDrawerFragment.mDrawerLayout.closeDrawer(
                NavigationDrawerFragment.mViewContainer);
    }

    public void setTitle(CharSequence title) {
//        Constants.mAb.setTitle(title);
        Enterprise.mTvTitle.setText(title);
    }
}