package ui.fragment.custom.gallery;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.trek2000.android.enterprise.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import adapter.AlbumInsideAdapter;
import custom_view.MarkableImageView;
import ui.activity.CustomGallery;
import ui.fragment.review.ReviewFragment;
import utils.Utils;

public class AlbumInsideFragment extends Fragment
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static HashMap<Integer, model.File> mHmSelectedFiles = new HashMap<>();
    /**
     * Data section
     */
    private ArrayList<model.File> mAlFiles = new ArrayList<model.File>();

    /**
     * String section
     */
    private boolean IS_LONG_CLICK_CONTINUE = false;

    public static final String ARGUMENT_ALBUM_NAME = "ALBUM_NAME";
    public static final String ARGUMENT_FOLDER_PATH = "FOLDER_PATH";

    /**
     * View section
     */
    private GridView mGv;

    /**
     * Others section
     */

    /**
     *
     * @return
     */
    public static Fragment newInstance() {
        AlbumInsideFragment fragment = new AlbumInsideFragment();
        return fragment;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * Set Orientation for page
         */
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View v = getLayoutInflater(savedInstanceState).inflate(
                R.layout.fragment_album_inside, container, false);

        // Initial views
        initialViews(v);
        initialData();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Hide Upload text view after exit Album Inside fragment
//        CustomGallery.mTvUpload.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
        // on Select Item
        onSelectItem(false, v, pos, mAlFiles);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View v, int pos, long id) {
        // on Select Item
        onSelectItem(true, v, pos, mAlFiles);

        // Should return true to avoid onItemClick was called.
        return true;
    }

    /**
     * Initialize methods
     */
    private void initialData() {
        // Show Upload text view
//        CustomGallery.mTvUpload.setVisibility(View.VISIBLE);

        // Set listener
        mGv.setOnItemClickListener(this);
        mGv.setOnItemLongClickListener(this);

        /**
         * Get folder path after selected Album
         */
        String ALBUM_NAME = getArguments().getString(ARGUMENT_ALBUM_NAME);
        String FOLDER_PATH = getArguments().getString(ARGUMENT_FOLDER_PATH);

        /**
         * Read all photos & videos inside selected Album without taking care
         * about sub-folder inside.
         * Need list all of them.
         */
        ArrayList<File> mAlFileList = getAlbumInside(FOLDER_PATH);

        /**
         * Need set Title is selected Album
         */
        CustomGallery.mTvAlbumName.setText(ALBUM_NAME);

        // Should clear old data before add new data
        if (!mAlFiles.isEmpty())    mAlFiles.clear();

        // Add item into array list
        try {
            for (int i = 0; i < mAlFileList.size(); i++) {
                File mFile = new File(mAlFileList.get(i).getAbsolutePath());

                if (mAlFileList.get(i).isFile()) {
                    // Get image Uri from File path
                    Uri mUri = null;
                    if (Utils.isPhotoOrVideo(mFile.getAbsolutePath()) == 0)
                        mUri = Utils.getImagePreviewOfUri(
                                true, getActivity(), mFile);
                    else if (Utils.isPhotoOrVideo(mFile.getAbsolutePath()) == 1)
                        mUri = Utils.getImagePreviewOfUri(
                                false, getActivity(), mFile);

                    if (mFile.exists() & mFile.isFile()) {
                        // Add into array list
                        model.File file = new model.File(
                                getActivity(),
                                mAlFileList.get(i).getName(),
                                mUri.toString(), false);

                        // Check file type to see it is photo or video
                        if (Utils.isPhotoOrVideo(mFile.getAbsolutePath()) == 0)
                            file.setVideo(false);
                        else if (Utils.isPhotoOrVideo(mFile.getAbsolutePath()) == 1) {
                            file.setVideo(true);

                            // set duration of video
                            file.setDurationOfVideo(Utils.getDurationOfVideo(
                                    getActivity(), mFile.getAbsolutePath()));
                        }

                        // Add item into array list
                        mAlFiles.add(file);
                    } else if (mFile.exists() & mFile.isDirectory()) {
                        ArrayList<File> mAl = new ArrayList<>();
                        mAl = Utils.getPhotoAndVideoFromSdCard(
                                mAl, new File(mFile.getAbsolutePath()));

                        for (int j = 0; j < mAl.size(); j++) {
                            // Add into array list
                            if (mAl.get(j).isFile()) {
                                model.File file = new model.File(
                                        getActivity(),
                                        mAlFileList.get(j).getName(),
                                        mUri.toString(), false);

                                // Check file type to see it is photo or video
                                if (Utils.isPhotoOrVideo(mFile.getAbsolutePath()) == 0)
                                    file.setVideo(false);
                                else if (Utils.isPhotoOrVideo(mFile.getAbsolutePath()) == 1) {
                                    file.setVideo(true);

                                    // set duration of video
                                    file.setDurationOfVideo(Utils.getDurationOfVideo(
                                            getActivity(), mFile.getAbsolutePath()));
                                }

                                // Add into array list
                                mAlFiles.add(file);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set adapter
        mGv.setAdapter(new AlbumInsideAdapter(
                getActivity(),
                R.layout.simple_grid_item_in_fragment_album_inside,
                mAlFiles));
    }

    private void initialViews(View v) {
        mGv = (GridView) v.findViewById(R.id.gv_in_fragment_album_inside);
    }

    /**
     * The others methods
     */

    private ArrayList<File> getAlbumInside(String FOLDER_PATH) {
        // Get thumbnail list from Gallery
        File mFileFolder = new File(FOLDER_PATH);

        ArrayList<File> mAlFolders = new ArrayList<>();
        mAlFolders = Utils.getPhotoAndVideoFromSdCard(mAlFolders, mFileFolder);

        return mAlFolders;
    }

    private void onSelectItem(boolean IS_LONG_CLICK_MODE, View v, int pos, ArrayList<model.File> mAl) {
        /**
         * check if is long click show item checked, else show file detail
         */
        if (IS_LONG_CLICK_MODE | IS_LONG_CLICK_CONTINUE) {
            /**
             * Multi-choice selection
             */

            /**
             * Show Overlay image to check
             */
            if (mAl.get(pos).isChecked()) {
                /**
                 * If selected item already chose
                 */
                /**
                 * Hide checked image
                 */
                ((MarkableImageView) v).setChecked(false);
                mAl.get(pos).setChecked(false);

                /**
                 * Remove item out of array list was defined to store selected items
                 */
                mHmSelectedFiles.remove(pos);

                /**
                 * If selected item size return to 0,
                 * Hide option for selected item & show default bar again
                 */
                if (mHmSelectedFiles.size() == 0)
                    // item checked Item long click, reset
                    IS_LONG_CLICK_CONTINUE = false;
            } else {
                // item checked Item long click
                if (IS_LONG_CLICK_MODE) IS_LONG_CLICK_CONTINUE = true;

                /**
                 * If selected item has not already chose
                 */

                /**
                 * Show checked image
                 */
                ((MarkableImageView) v).setChecked(true);
//                ((MarkableImageView) v.findViewWithTag(pos)).setChecked(true);
                mAl.get(pos).setChecked(true);

                /**
                 * Add to array list was defined to store selected items
                 */
                mHmSelectedFiles.put(pos, mAl.get(pos));
            }

            /**
             * Always Update new selected items in Text View also
             */
            CustomGallery.mTvUpload.setText(getString(R.string.upload) + " " + mHmSelectedFiles.size());

            if (mHmSelectedFiles.size() == 0)
                CustomGallery.mTvUpload.setTextColor(getActivity().getResources().getColor(R.color.gray));
            else
                CustomGallery.mTvUpload.setTextColor(getActivity().getResources().getColor(R.color.orange_light));

            /**
             * check selected all item
             */
//            if (!IS_CONNECTED_TO_CARD_MODE) {
//                if (String.valueOf(mHmSelectedFiles.size()).equals(String.valueOf(mAl.size()))) {
//                    mBtnSelectAll.setText(getResources().getString(R.string.deselect_all_));
//                } else {
//                    mBtnSelectAll.setText(getResources().getString(R.string.select_all_));
//                }
//            }
        } else {
            // Set single ton for item before show transfer to Full Screen page
            // to get data follow selected item
//            Enterprise.item.setImage(
//                    BitmapFactory.decodeFile(mAlFiles.get(pos).getFilePath()));

            // Clear old hash map before continue

            /**todo
             * Transfer to Full Screen page after selected file
             * - Should pass File Path
             */
            ((FragmentActivity) getActivity())
                    .getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fl_in_activity_custom_gallery,
                            ReviewFragment.newInstance(mAl.get(pos).getFilePath()))
                    .commitAllowingStateLoss();
        }
    }

}
