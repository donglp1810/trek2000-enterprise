package ui.fragment.drawer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.trek2000.android.enterprise.Enterprise;
import com.trek2000.android.enterprise.R;

import java.util.ArrayList;

import adapter.DrawerAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import define.MediaType;
import define.Page;
import define.SharedPreference;
import iterface.OnCallBackRefreshDrawerListener;
import listener.DrawerItemClickListener;
import model.DrawerItem;
import utils.DownloadImageAsync;

/**
 * Fragment used for managing interactions for and presentation of a navigation ibtn_drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-ibtn_drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerInNotificationsFragment extends Fragment{

    /**
     * Data section
     */
    /**
     * Interface section
     */
    /**
     * String section
     */
    /**
     * View section
     */
    public static DrawerLayout mDrawerLayout;
    public static View mViewContainer;
    private ActionBarDrawerToggle mAbdt;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(
                R.layout.fragment_navigation_drawer_in_notifcations, container, false);
        initialViews(v);
        initialData();
        return v;
    }



    /**
     * Initialize methods
     */

    private void initialData() {
    }

    private void initialViews(View v) {
    }

    /**
     * Users of this ui.fragment must call this method to set up the navigation ibtn_drawer interactions.
     *
     * @param fragmentId   The android:id of this ui.fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this ui.fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mViewContainer = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the ibtn_drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.bg_navigation_drawer, GravityCompat.START);
        // set up the ibtn_drawer's list view with items and click listener

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation ibtn_drawer and the action bar app icon.
        mAbdt = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.drawer,             /* nav ibtn_drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open ibtn_drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close ibtn_drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                // Allow click on any things on UI when drawer is closed
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                // Not allow click on any things on UI when drawer is opened

                /**
                 * Call new thread to get Profile information
                 */
            }
        };

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mAbdt.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mAbdt);
    }

}
