package ui.fragment.company;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.trek2000.android.enterprise.R;

import java.util.ArrayList;

import adapter.company.AllCompanyFeedAdapter;
import model.AllCompanyFeedItem;

public class AllCompanyFeedFragment extends Fragment
        implements View.OnClickListener{

    /**
     * String section
     */
    public static Boolean IS_IN_ALL_COMPANY_FEED_FRAGMENT = false;
    /**
     * View section
     */

    private ListView mLvInAllCompanyFeedFragment;
    private ArrayList<AllCompanyFeedItem> mAlEx = new ArrayList<AllCompanyFeedItem>();
    private AllCompanyFeedAdapter allCompanyFeedAdapter = null;
    /**
     * Others section
     */

    /**
     * Listener section
     */

    public static Fragment newInstance() {

        AllCompanyFeedFragment fragment = new AllCompanyFeedFragment();
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
                R.layout.fragment_all_company_feed, container, false);

        IS_IN_ALL_COMPANY_FEED_FRAGMENT = true;

        dataExample();

        // Initial views
        initialViews(v);
        initialData();
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        IS_IN_ALL_COMPANY_FEED_FRAGMENT = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IS_IN_ALL_COMPANY_FEED_FRAGMENT = false;
    }
    /**
     * Initialize methods
     */
    private void initialData() {
        allCompanyFeedAdapter = new AllCompanyFeedAdapter(getActivity(), R.layout.simple_list_item_all_company_feed, mAlEx);
        mLvInAllCompanyFeedFragment.setAdapter(allCompanyFeedAdapter);
    }

    private void initialViews(View v) {
        mLvInAllCompanyFeedFragment = (ListView) v.findViewById(R.id.lv_in_all_company_feed_fragment);
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
