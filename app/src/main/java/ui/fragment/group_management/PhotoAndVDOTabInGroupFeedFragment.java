package ui.fragment.group_management;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.trek2000.android.enterprise.R;

import java.util.ArrayList;

import adapter.group_management.PhotoAndVDOTabInGroupFeedFragmentAdapter;
import model.AllCompanyFeedItem;

public class PhotoAndVDOTabInGroupFeedFragment extends Fragment
        implements View.OnClickListener{

    /**
     * String section
     */

    /**
     * View section
     */
    private ListView mLvInPhotoAndVDOInGroupFeedFragment;
    private PhotoAndVDOTabInGroupFeedFragmentAdapter photoAndVDOTabInGroupFeedFragmentAdapter;
    private ArrayList<AllCompanyFeedItem> mAlEx = new ArrayList<AllCompanyFeedItem>();
    /**
     * Others section
     */

    /**
     * Listener section
     */

    public static Fragment newInstance() {

        PhotoAndVDOTabInGroupFeedFragment fragment = new PhotoAndVDOTabInGroupFeedFragment();
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
                R.layout.fragment_photo_and_vdo_tab_in_group_feed_fragment, container, false);

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
        photoAndVDOTabInGroupFeedFragmentAdapter = new PhotoAndVDOTabInGroupFeedFragmentAdapter(getActivity(),
                R.layout.simple_list_item_photo_and_vdo_tab_in_group_feed_fragment, mAlEx);
        mLvInPhotoAndVDOInGroupFeedFragment.setAdapter(photoAndVDOTabInGroupFeedFragmentAdapter);
    }

    private void initialViews(View v) {
        mLvInPhotoAndVDOInGroupFeedFragment = (ListView) v.findViewById(R.id.lv_in_photo_and_vdo_tab_in_group_feed_fragment);
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
