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
public class NavigationDrawerFragment extends Fragment
        implements OnCallBackRefreshDrawerListener, View.OnClickListener {

    // Remember the position of the selected item.
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    /**
     * Data section
     */
    public static DrawerAdapter drawerAdapter = null;
    public static ArrayList<DrawerItem> mAlDrawer = new ArrayList<DrawerItem>();
    /**
     * Interface section
     */
    public static OnCallBackRefreshDrawerListener onCallBackRefreshDrawerListener;
    /**
     * String section
     */
    public static boolean IS_LOG_OUT = false;
    public static DrawerLayout mDrawerLayout;
    public static ListView mLvInAllCompanyFeed;
    public static View mViewContainer;
    private int mCurrentSelectedPosition = 0;
    /**
     * View section
     */
    // Helper component that ties the action bar to the navigation ibtn_drawer.
    private ActionBarDrawerToggle mAbdt;
    private Button mBtnLogOut;
    private CircleImageView circleIv;
    // A pointer to the current callbacks instance (the Activity).
    private NavigationDrawerCallbacks mNdCallbacks;

    /**
     * Basic methods
     */

    public static void onClickLogOut(Context mContext) {
        // Set boolean be true to define user already log out account
        IS_LOG_OUT = true;

        // Clear current cache for images
        Enterprise.imageLoader.clearDiskCache();
        Enterprise.imageLoader.clearMemoryCache();

        // Clear old data of old account,
        // Before load new data of new account

        // Put to shared preferences null for all things to take user should
        // be sign in again, and transfer to Log In page after that
        if (SharedPreference.mSpEditor != null) {
            SharedPreference.mSpEditor.putString(SharedPreference.USER_GLOBAL_ID, null);
            SharedPreference.mSpEditor.commit();
        }

        // Reset user data
//        Cloudstringers.user.setPassword(null);
//        Cloudstringers.user.setToken(null);
//        Cloudstringers.user.setUserGlobalID(0);
//        Cloudstringers.user.setUserEmail(null);

        /**
         * Reset current page = 0 again for next time can see the files begin loading again
         */

        /**
         * Release all dialogs after used
         */
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mNdCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_log_out:
                // Log out
                onClickLogOut(getActivity());
                break;
            case R.id.ibtn_drawer_in_action_bar:
                /**
                 * Check the navigation bar was showing or not
                 */
                if (!NavigationDrawerFragment.mViewContainer.isShown()) {
                    NavigationDrawerFragment.mDrawerLayout
                            .openDrawer(NavigationDrawerFragment.mViewContainer);
                } else {
                    NavigationDrawerFragment.mDrawerLayout
                            .closeDrawer(NavigationDrawerFragment.mViewContainer);
                }
                break;

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the ibtn_drawer toggle component.
        mAbdt.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize callback for drawer button
        onCallBackRefreshDrawerListener = (OnCallBackRefreshDrawerListener) this;

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**todo
         * Should define 2 cases to load data correctly, Navigation drawer for
         * - All company feeds
         * - Company feeds
         */
        View v = null;
        switch (Enterprise.user.getPage()) {
            case Page.all_company_feed:
                /**
                 * All company feeds
                 */

                v = inflater.inflate(
                        R.layout.fragment_navigation_drawer_in_company_feed, container, false);

                initialVariablesInAllCompanyFeed(v);
                initialDataInAllCompanyFeed();
                break;
            case Page.company_feed:
                /**
                 * Company feeds
                 */
                break;
        }

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNdCallbacks = null;
    }

    @Override
    public void onRefreshDrawer(String AVATAR) {
        // Set avatar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new DownloadImageAsync(getActivity(), circleIv, MediaType.PHOTO).executeOnExecutor(
                    AsyncTask.THREAD_POOL_EXECUTOR, AVATAR);
        else
            new DownloadImageAsync(getActivity(), circleIv, MediaType.PHOTO).execute(AVATAR);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    /**
     * Initialize methods
     */

    private void initialData() {
        mBtnLogOut.setOnClickListener(this);
    }

    private void initialDataInAllCompanyFeed() {
        /**
         * Set adapter for list view
         */
        mAlDrawer = new ArrayList<DrawerItem>() {
        };

        // todo Should put Notifications data correctly
        // Feeds - Default : need set first selected item is Feeds
        DrawerItem mDiFeeds = new DrawerItem(true, -1);
        mAlDrawer.add(mDiFeeds);
        // Notifications
        DrawerItem mDiNotifications = new DrawerItem(false, 0);
        mAlDrawer.add(mDiNotifications);
        // Card management
        DrawerItem mDiCardManagement = new DrawerItem(false, 0);
        mAlDrawer.add(mDiCardManagement);
        // User management
        DrawerItem mDiUserManagement = new DrawerItem(false, 0);
        mAlDrawer.add(mDiUserManagement);
        // Group management
        DrawerItem mDiGroupManagement = new DrawerItem(false, 0);
        mAlDrawer.add(mDiGroupManagement);
        //setting
        DrawerItem mDiSetting = new DrawerItem(false, -1);
        mAlDrawer.add(mDiSetting);

        // Set adapter
        drawerAdapter = new DrawerAdapter(
                getActivity(),
                R.layout.simple_list_item_in_fragment_navigation_drawer_in_company_name,
                mAlDrawer);
        mLvInAllCompanyFeed.setAdapter(drawerAdapter);

        // Set OnItemClick listener
        mLvInAllCompanyFeed.setOnItemClickListener(
                new DrawerItemClickListener(getActivity(), mAlDrawer));
    }

    private void initialVariables(View v) {
        circleIv = (CircleImageView) v.findViewById(R.id.circle_iv_avatar);

        mBtnLogOut = (Button) v.findViewById(R.id.btn_log_out);
    }

    private void initialVariablesInAllCompanyFeed(View v) {
        mLvInAllCompanyFeed = (ListView) v
                .findViewById(R.id.lv_in_fragment_navigation_drawer_in_company_feed);
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

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mViewContainer);
        }
        if (mNdCallbacks != null) {
            mNdCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    /**
     * Callbacks interface that all activities using this ui.fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation ibtn_drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

}
