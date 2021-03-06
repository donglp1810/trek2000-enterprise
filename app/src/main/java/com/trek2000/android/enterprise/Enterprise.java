package com.trek2000.android.enterprise;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;

import define.Constants;
import singleton.Item;
import singleton.User;
import ui.activity.CustomGallery;
import ui.fragment.company.AllCompanyFeedFragment;
import ui.fragment.company.SelectCompanyFragment;
import ui.fragment.drawer.NavigationDrawerFragment;
import ui.fragment.drawer.NavigationDrawerInNotificationsFragment;
import utils.SmartUriDecoder;

import static define.SharedPreference.PREFS;
import static define.SharedPreference.mSp;
import static define.SharedPreference.mSpEditor;


public class Enterprise extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, View.OnClickListener {

    /**
     * Image loader to load image flexible, avoid OutOfMemory exception
     */
    public static ImageLoader imageLoader = ImageLoader.getInstance();
    public static ImageLoaderConfiguration imageLoaderConfiguration;

    /**
     * Single ton way
     */
    public static User user = User.getInstance();
    public static Item item = Item.getInstance();
    public static TextView mTvTitle;
    /**
     * View section
     */
    private ImageButton mIbtnAdd;
    private ImageButton mIbtnBack;
    private ImageButton mIbtnDrawer;
    private ImageButton mIbtnUpload;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private NavigationDrawerInNotificationsFragment mNavigationDrawerInNotificationsFragment;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_add_in_action_bar:
                /**
                 * Cases
                 * - User management page
                 * - Group name page
                 */
                break;
            // todo MOVE BACK BUTTON TO FRAGMENT
            case R.id.ibtn_back_in_action_bar:
                /**
                 * Cases
                 * - Add To Group page
                 * - Country page
                 * - Company Logo page
                 * - Group Name page
                 */
                break;
            case R.id.ibtn_create_group_in_action_bar:
                /**
                 * Cases
                 * - Group Management page
                 */
                break;
            case R.id.ibtn_drawer_in_action_bar:
                //check if is in all company feed or select company show NavigationDrawerInNotificationsFragment
                // else if is in company feed show NavigationDrawerFragment
                Log.i("" ,"AllCompanyFeedFragment.IS_IN_ALL_COMPANY_FEED_FRAGMENT " + AllCompanyFeedFragment.IS_IN_ALL_COMPANY_FEED_FRAGMENT);
                Log.i("" ,"SelectCompanyFragment.IS_IN_SELECT_COMPANY_FRAGMENT " + SelectCompanyFragment.IS_IN_SELECT_COMPANY_FRAGMENT);
//            if (AllCompanyFeedFragment.IS_IN_ALL_COMPANY_FEED_FRAGMENT ||
//                    SelectCompanyFragment.IS_IN_SELECT_COMPANY_FRAGMENT) {
//                /**
//                 * Check the navigation bar was showing or not
//                 */
//                if (!NavigationDrawerInNotificationsFragment.mViewContainer.isShown()) {
//                    NavigationDrawerInNotificationsFragment.mDrawerLayout
//                            .openDrawer(NavigationDrawerInNotificationsFragment.mViewContainer);
//                } else {
//                    NavigationDrawerInNotificationsFragment.mDrawerLayout
//                            .closeDrawer(NavigationDrawerInNotificationsFragment.mViewContainer);
//                }
//
//            } else {
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
//            }
                break;
            case R.id.ibtn_upload_in_action_bar:
                /**
                 * Cases
                 * - Group Name page
                 */
                // todo Go to Custom Gallery to upload
                startActivityForResult(new Intent(Enterprise.this, CustomGallery.class), 1000);
                break;
            // todo MOVE ADD TO GROUP BUTTON TO FRAGMENT
            case R.id.tv_add_to_group_in_action_bar:
                /**
                 * Cases
                 * - Add To Group page
                 */
                break;
            // todo MOVE DONE BUTTON TO FRAGMENT
            case R.id.tv_done_in_action_bar:
                /**
                 * Cases
                 * - Create Group page
                 * - Group Name page
                 */
                break;
            // todo MOVE DOWNLOAD BUTTON TO FRAGMENT
            case R.id.tv_download_in_action_bar:
                /**
                 * Cases
                 * - Group Name page
                 */
                break;
            case R.id.tv_title_in_action_bar:
                //TODO go to all company fragment
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.fl_in_activity_enterprise, SelectCompanyFragment.newInstance()
                ).commitAllowingStateLoss();
                mTvTitle.setText(getString(R.string.all_company));
                break;

        }
    }
    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise);
        /**
         * Action Bar
         */
        Constants.mAb = getSupportActionBar();
        Constants.mAb.setDisplayShowHomeEnabled(false);
        Constants.mAb.setDisplayShowTitleEnabled(false);
        Constants.mAb.setDisplayShowCustomEnabled(true);

        // Set Custom Action Bar (Should locate after initialized)
        Constants.mAb.setCustomView(R.layout.action_bar);

        // Show action bar
        Constants.mAb.show();

        // Set title is null
        setTitle("");

        ImageDecoder smartUriDecoder = new SmartUriDecoder(
                getContentResolver(), new BaseImageDecoder(false));

        // This configuration tuning is custom.
        // You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        imageLoaderConfiguration =
                new ImageLoaderConfiguration.Builder(this)
                        .threadPriority(Thread.NORM_PRIORITY - 2)
                        .denyCacheImageMultipleSizesInMemory()
                        .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                        .diskCacheSize(50 * 1024 * 1024) // 50 Mb.
                        .imageDecoder(smartUriDecoder)
                        .tasksProcessingOrder(QueueProcessingType.LIFO)
                        .writeDebugLogs() // Remove for release app
                        .build();
        // Initialize ImageLoader with configuration.
        imageLoader.init(imageLoaderConfiguration);

        /**
         * If the user has not signed in yet,
         * should directly to sign in page
         */
        mSp = getSharedPreferences(PREFS, 1);
        mSpEditor = mSp.edit();

        initialVariables();
        initialData();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    /**
     * Initialize variables
     */

    private void initialData() {
        // Set listener
        mIbtnAdd.setOnClickListener(this);
        mIbtnBack.setOnClickListener(this);
        mIbtnDrawer.setOnClickListener(this);
        mIbtnUpload.setOnClickListener(this);
        mTvTitle.setOnClickListener(this);

        // Set up the Drawer Layout.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Set up the Drawer Layout.
        mNavigationDrawerInNotificationsFragment.setUp(
                R.id.navigation_drawer_in_notifications,
                (DrawerLayout) findViewById(R.id.drawer_layout_in_notifications));

        //load all companay feed
        loadAllCompanyFeed();
    }
    private void initialVariables() {
        mIbtnAdd = (ImageButton) findViewById(R.id.ibtn_add_in_action_bar);
        mIbtnBack = (ImageButton) findViewById(R.id.ibtn_back_in_action_bar);
        mIbtnDrawer = (ImageButton) findViewById(R.id.ibtn_drawer_in_action_bar);
        mIbtnUpload = (ImageButton) findViewById(R.id.ibtn_upload_in_action_bar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerInNotificationsFragment = (NavigationDrawerInNotificationsFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_in_notifications);

        mTvTitle = (TextView) findViewById(R.id.tv_title_in_action_bar);
    }

    /**
     * Basic methods
     */
    private void loadAllCompanyFeed() {
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fl_in_activity_enterprise, AllCompanyFeedFragment.newInstance()
        ).commitAllowingStateLoss();
    }
}
