package ui.fragment.group_management;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.trek2000.android.enterprise.R;

import java.util.ArrayList;

import model.AllCompanyFeedItem;

public class SettingTabInGroupFeedFragment extends Fragment
        implements View.OnClickListener{

    /**
     * String section
     */

    /**
     * View section
     */
    private ArrayList<AllCompanyFeedItem> mAlEx = new ArrayList<AllCompanyFeedItem>();
    /**
     * Others section
     */

    /**
     * Listener section
     */

    public static Fragment newInstance() {

        SettingTabInGroupFeedFragment fragment = new SettingTabInGroupFeedFragment();
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
                R.layout.fragment_setting_tab_in_group_feed_fragment, container, false);

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
    }

    private void initialViews(View v) {
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
