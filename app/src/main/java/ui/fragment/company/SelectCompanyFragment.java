package ui.fragment.company;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.trek2000.android.enterprise.R;

import java.util.ArrayList;

import adapter.company.SelectCompanyAdapter;

public class SelectCompanyFragment extends Fragment
        implements View.OnClickListener{

    /**
     * String section
     */
    public static Boolean IS_IN_SELECT_COMPANY_FRAGMENT = false;
    /**
     * View section
     */
    private GridView mGvInSelectCompanyFragment;
    /**
     * Others section
     */
    public static SelectCompanyAdapter selectCompanyAdapter = null;
    private ArrayList<String> mAlEx = new ArrayList<String>();

    /**
     * Listener section
     */

    //purpose transfer data
    public static Fragment newInstance() {
        SelectCompanyFragment fragment = new SelectCompanyFragment();
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
                R.layout.fragment_select_company, container, false);
        IS_IN_SELECT_COMPANY_FRAGMENT = true;
        dataExample ();
        // Initial views
        initialViews(v);
        initialData();
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        IS_IN_SELECT_COMPANY_FRAGMENT = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IS_IN_SELECT_COMPANY_FRAGMENT = false;
    }

    /**
     * Initialize methods
     */
    private void initialData() {
        selectCompanyAdapter = new SelectCompanyAdapter(getActivity(), R.layout.simple_list_item_select_company, mAlEx);
        mGvInSelectCompanyFragment.setAdapter(selectCompanyAdapter);
    }

    private void initialViews(View v) {
        mGvInSelectCompanyFragment = (GridView) v.findViewById(R.id.gv_select_company);
    }

    private void dataExample () {
        mAlEx.add("1");
        mAlEx.add("1");
        mAlEx.add("1");
        mAlEx.add("1");
        mAlEx.add("1");
        mAlEx.add("1");
        mAlEx.add("1");
        mAlEx.add("1");
        mAlEx.add("1");
        mAlEx.add("1");
    }
    /**
     * The others methods
     */
}
