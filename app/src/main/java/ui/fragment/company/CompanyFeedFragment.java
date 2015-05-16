package ui.fragment.company;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.trek2000.android.enterprise.R;

import java.util.ArrayList;

import adapter.company.AllCompanyFeedAdapter;
import adapter.company.CompanyFeedAdapter;
import model.AllCompanyFeedItem;

public class CompanyFeedFragment extends Fragment
        implements View.OnClickListener{

    /**
     * String section
     */

    /**
     * View section
     */

    private ListView mLvInCompanyFeedFragment;
    private ArrayList<AllCompanyFeedItem> mAlEx = new ArrayList<AllCompanyFeedItem>();
    private CompanyFeedAdapter companyFeedAdapter = null;
    /**
     * Others section
     */

    /**
     * Listener section
     */

    public static Fragment newInstance() {

        CompanyFeedFragment fragment = new CompanyFeedFragment();
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
                R.layout.fragment_company_feed, container, false);

        dataExample ();

        // Initial views
        initialViews(v);
        initialData();
        return v;
    }

    /**
     * Initialize methods
     */
    private void initialData() {
        companyFeedAdapter = new CompanyFeedAdapter(getActivity(), R.layout.simple_list_item_company_feed, mAlEx);
        mLvInCompanyFeedFragment.setAdapter(companyFeedAdapter);
    }

    private void initialViews(View v) {
        mLvInCompanyFeedFragment = (ListView) v.findViewById(R.id.lv_in_company_feed_fragment);
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
