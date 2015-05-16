package ui.fragment.group_management;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trek2000.android.enterprise.R;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import java.util.ArrayList;

import adapter.group_management.GroupFeedFragmentPagerAdapter;
import model.AllCompanyFeedItem;

public class GroupFeedFragment extends Fragment
        implements View.OnClickListener{

    /**
     * String section
     */

    /**
     * View section
     */
    private ViewPager mVpInGroupFeedFragment;
    private PageIndicator mPiInGroupFeedFragment;
    private GroupFeedFragmentPagerAdapter groupFeedFragmentPagerAdapter;
    private ArrayList<AllCompanyFeedItem> mAlEx = new ArrayList<AllCompanyFeedItem>();
    /**
     * Others section
     */

    /**
     * Listener section
     */

    public static Fragment newInstance() {

        GroupFeedFragment fragment = new GroupFeedFragment();
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = getLayoutInflater(savedInstanceState).inflate(
                R.layout.fragment_group_feed, container, false);
        dataExample();

        // Initial views
        initialViews(v);
        initialData();
        return v;
    }

    /**
     * Initialize methods
     */
    private void initialData() {
        groupFeedFragmentPagerAdapter = new GroupFeedFragmentPagerAdapter(getActivity(),
                getActivity().getSupportFragmentManager());
        mVpInGroupFeedFragment.setAdapter(groupFeedFragmentPagerAdapter);
        mPiInGroupFeedFragment.setViewPager(mVpInGroupFeedFragment);
        mPiInGroupFeedFragment.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                switch(position){
//                    case 0:
//                        new PhotoAndVDOTabInGroupFeedFragment();
//                        break;
//                    case 1:
//                        new MemberTabInGroupFeedFragment();
//                        break;
//                    case 2:
//                        new SettingTabInGroupFeedFragment();
//                        break;
//                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initialViews(View v) {
        mVpInGroupFeedFragment = (ViewPager) v.findViewById(R.id.vp_in_group_feed_fragment);
        mPiInGroupFeedFragment = (TitlePageIndicator) v.findViewById(R.id.title_page_indicator_in_group_feed_fragment);
    }

    /**
     * The others methods
     */
    private void dataExample () {
        ArrayList<String> mAlEx1 = new ArrayList<String>();
        mAlEx1.add("1");
        mAlEx1.add("1");
        mAlEx1.add("1");
        mAlEx1.add("1");
        mAlEx1.add("1");
        mAlEx1.add("1");
        AllCompanyFeedItem allCompanyFeedItem1 = new AllCompanyFeedItem("1" , mAlEx1);
        mAlEx.add(allCompanyFeedItem1);
        mAlEx.add(allCompanyFeedItem1);
        mAlEx.add(allCompanyFeedItem1);
        mAlEx.add(allCompanyFeedItem1);
        mAlEx.add(allCompanyFeedItem1);
        mAlEx.add(allCompanyFeedItem1);
        mAlEx.add(allCompanyFeedItem1);
        mAlEx.add(allCompanyFeedItem1);
        mAlEx.add(allCompanyFeedItem1);
        mAlEx.add(allCompanyFeedItem1);
    }
}
