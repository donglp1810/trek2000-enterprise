package ui.fragment.group_management;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.trek2000.android.enterprise.R;

import java.util.ArrayList;

import adapter.group_management.MemberTabInGroupFeedFragmentAdapter;
import model.AllCompanyFeedItem;

public class MemberTabInGroupFeedFragment extends Fragment
        implements View.OnClickListener{

    /**
     * String section
     */

    /**
     * View section
     */
    private ListView mLvInMemberTabInGroupFeedFragment;
    private MemberTabInGroupFeedFragmentAdapter memberTabInGroupFeedFragmentAdapter;
    private ArrayList<String> mAlEx = new ArrayList<String>();
    /**
     * Others section
     */

    /**
     * Listener section
     */

    public static Fragment newInstance() {

        MemberTabInGroupFeedFragment fragment = new MemberTabInGroupFeedFragment();
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
                R.layout.fragment_member_tab_in_group_feed_fragment, container, false);

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
        memberTabInGroupFeedFragmentAdapter = new MemberTabInGroupFeedFragmentAdapter(getActivity(),
                R.layout.simple_list_item_in_member_tab_in_group_feed_fragment, mAlEx);
        mLvInMemberTabInGroupFeedFragment.setAdapter(memberTabInGroupFeedFragmentAdapter);
    }

    private void initialViews(View v) {
        mLvInMemberTabInGroupFeedFragment = (ListView) v.findViewById(R.id.lv_in_member_tab_in_group_feed_fragment);
    }

    /**
     * The others methods
     */
    private void dataExample () {
        mAlEx.add("1");
        mAlEx.add("1");
        mAlEx.add("1");
        mAlEx.add("1");
    }
}
