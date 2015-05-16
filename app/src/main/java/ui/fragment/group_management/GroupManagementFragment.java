package ui.fragment.group_management;

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

import adapter.group_management.GroupManagementAdapter;

public class GroupManagementFragment extends Fragment
        implements View.OnClickListener{

    /**
     * String section
     */

    /**
     * View section
     */

    private GridView mGvInGroupManagementFragment;
    private GroupManagementAdapter groupManagementAdapter;
    private ArrayList<String> mAlEx = new ArrayList<String>();
    /**
     * Others section
     */

    /**
     * Listener section
     */

    public static Fragment newInstance() {

        GroupManagementFragment fragment = new GroupManagementFragment();
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
                R.layout.fragment_group_management, container, false);
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
        groupManagementAdapter = new GroupManagementAdapter(getActivity(), R.layout.simple_grid_item_group_management, mAlEx);
        mGvInGroupManagementFragment.setAdapter(groupManagementAdapter);
    }

    private void initialViews(View v) {
        mGvInGroupManagementFragment = (GridView) v.findViewById(R.id.gv_in_group_management_fragment);
    }

    /**
     * The others methods
     */
    private void dataExample () {
        mAlEx.add("1");
        mAlEx.add("1");
    }
}
