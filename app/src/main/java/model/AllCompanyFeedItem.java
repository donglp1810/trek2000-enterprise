package model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by trek2000 on 12/2/2015.
 */
public class AllCompanyFeedItem {

    /**
     * String section
     */
    private String EX;
    private ArrayList <String> mAlExChil;

    /**
     * The others section
     */

    public AllCompanyFeedItem(String ex, ArrayList <String> mAlExChil) {

        this.EX = ex;

        this.mAlExChil = mAlExChil;
    }

    public String getEx() {
        return EX;
    }

    public ArrayList <String> getmAlExChil() {
        return mAlExChil;
    }

}
