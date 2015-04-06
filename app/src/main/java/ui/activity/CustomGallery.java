package ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trek2000.android.enterprise.R;

import ui.fragment.custom.gallery.AlbumListFragment;

/**
 * Created by trek2000 on 25/2/2015.
 */
public class CustomGallery extends FragmentActivity
        implements View.OnClickListener {

    public static LinearLayout mLlAlbumName;
    public static TextView mTvUpload;
    /**
     * View section
     */
    private ImageButton mIbtnBack;

    /**
     * Basic methods
     */

    public static void modifyUploadTextView(Context mContext, boolean visible) {
        /**
         * If visible is true
         * - Enable = true, show Orange text color
         * If visible is false
         * - Enable = false, show Gray text color
         */
        if (visible) {
            mTvUpload.setEnabled(true);
            mTvUpload.setTextColor(mContext.getResources().getColor(R.color.orange_light));
        } else {
            mTvUpload.setEnabled(false);
            mTvUpload.setTextColor(mContext.getResources().getColor(R.color.gray_dark_in_under_action_bar));
        }
    }

    /**
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back_in_activity_custom_gallery:
                finish();
                break;
            case R.id.tv_take_new:
                // todo Go to Custom Camera page.
                break;
            case R.id.tv_upload_in_activity_custom_gallery:
                // todo After selected files in Folder page,
                // begin upload after closed Activity Custom Gallery
                finish();

                // todo transfer Array List had selected files inside to Enterprise activity
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_gallery);

        // Should show Fragment Custom Gallery firstly
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fl_in_activity_custom_gallery, AlbumListFragment.newInstance())
                .commitAllowingStateLoss();

        initialVariables();
        initialData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Initial methods
     */

    private void initialData() {
        // Set listener
        mTvUpload.setOnClickListener(this);

        // In default, should set Upload text view correctly
        modifyUploadTextView(this, false);
    }

    private void initialVariables() {
        mIbtnBack = (ImageButton) findViewById(R.id.ibtn_back_in_activity_custom_gallery);
        mLlAlbumName = (LinearLayout) findViewById(R.id.ll_album_name_in_activity_custom_gallery);
        mTvUpload = (TextView) findViewById(R.id.tv_upload_in_activity_custom_gallery);
    }
}
