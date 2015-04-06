package utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Date;

import define.API;

/**
 * Created by trek2000 on 9/9/2014.
 */
public class DownloadFileAsync extends AsyncTask<String, Void, Boolean> {

    /**
     * Data section
     */

    private static int completed_item_index = 0;
    /**
     * View section
     */
    private static ProgressDialog mPd;
    /**
     * String section
     */
    private boolean IS_CONNECTED_CARD = false;
    private boolean IS_FINISHED_CURRENT_ACTIVITY = false;
    private String JPG = ".JPG";
    private String MTS = ".MTS";
    private String MOV = ".MOV";
    private String MP4 = ".MP4";
    private String WMV = ".WMV";
    /**
     * The other sections
     */
    private Context mContext;
    // declare the dialog as a member field of your activity

    public DownloadFileAsync(Context mContext) {
        this.mContext = mContext;
    }

    public DownloadFileAsync(boolean IS_CONNECTED_CARD, Context mContext) {
        this.IS_CONNECTED_CARD = IS_CONNECTED_CARD;

        this.mContext = mContext;
    }

    public DownloadFileAsync(Context mContext, boolean IS_FINISHED_CURRENT_ACTIVITY) {
        this.mContext = mContext;

        this.IS_FINISHED_CURRENT_ACTIVITY = IS_FINISHED_CURRENT_ACTIVITY;

    }

    @Override
    protected Boolean doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            String URL = null;

            /**
             * Check if connected card or not
             */
            if (!API.IS_DEV_SITE_OR_PRODUCT_SITE & !IS_CONNECTED_CARD) {
                // Case - not connected card
                URL = sUrl[0].replace("https://", "http://");
            } else {
                // Connected card
                URL = sUrl[0];
            }

            Log.i("", "URL " + URL);

            java.net.URL url = new java.net.URL(URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();

            // New date - Filename
            // png
            // IMG
            // VIDEO
            Date date = new Date();
            File mFile = new File(Environment.getExternalStorageDirectory() + "/Cloudstringers");

            // Constants.STORED_FOLDER + "") + "");
            if (!mFile.exists()) mFile.mkdir();

            String path = mFile.getPath() + "/" + String.valueOf(date.getTime());
            File mFileSpecify = null;
            if (URL.toUpperCase().contains(JPG)) {
                mFileSpecify = new File(path + JPG);
            } else if (URL.toUpperCase().contains(MOV)) {
                mFileSpecify = new File(path + MOV);
            } else if (URL.toUpperCase().contains(MP4)) {
                mFileSpecify = new File(path + MP4);
            } else if (URL.toUpperCase().contains(MTS)) {
                mFileSpecify = new File(path + MTS);
            } else if (URL.toUpperCase().contains(WMV)) {
                mFileSpecify = new File(path + WMV);
            }

            Log.i("", "can Write - " + mFile.canWrite()
                    + " mFileSpecify.getPath() " + mFileSpecify.getPath());

            output = new FileOutputStream(mFileSpecify.getPath());

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;

                if ((total * 100 / fileLength) == 100)
                    Log.i("", total * 100 / fileLength + "");

                output.write(data, 0, count);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }

            if (connection != null)
                connection.disconnect();
        }
        return false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // show progress dialog
//        mPd = Utils.showProgressDialog(
//                mContext,
//                mContext.getString(R.string.title_downloading),
//                completed_item_index + " / " + Cloudstringers.mHmSelectedItems.size() + "");
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        // Hide progress dialog
        Utils.hideProgressDialog();

        /**todo
         * Refresh Gallery
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            mContext.sendBroadcast(mediaScanIntent);
        } else {
            mContext.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }

        if (!result) {
        } else {
            /**
             * Case : not connected to card
             */
//                        completedDownloadingProgress();
//                    continueDownloadFile();
        }
    }

//    private void continueDownloadFile() {
//        if (!Cloudstringers.mAlIndex.isEmpty()) {
//            // Remove item already downloaded
//            Cloudstringers.mAlIndex.remove(0);
//            completed_item_index++;
//
//            mPd.setMessage(completed_item_index + " / "
//                                   + Cloudstringers.mHmSelectedItems.size() + "");
//
//            // Continue download new item
//            if (!Cloudstringers.mAlIndex.isEmpty()) {
//                if (!IS_CONNECTED_CARD) {
//                    /**
//                     * Case : not connected to card
//                     */
//                    new DownloadFileAsync(mContext).execute(
//                            Cloudstringers.mHmSelectedItems.get(
//                                    Cloudstringers.mAlIndex.get(0)).getOriginalClip());
//                } else {
//                    /**
//                     * Case : connected to card
//                     */
//                    new DownloadFileAsync(true, mContext).execute(Cloudstringers.mHmSelectedItems.get(
//                            Cloudstringers.mAlIndex.get(0)).getDownloadURL());
////                    new DownloadFileAsync(true, mContext).executeOnExecutor(
////                            AsyncTask.THREAD_POOL_EXECUTOR, Cloudstringers.mHmSelectedItems.get(
////                                    Cloudstringers.mAlIndex.get(0)).getDownloadURL());
//                }
//            } else {
//                completedDownloadingProgress();
//            }
//        } else {
//            completed_item_index++;
//            completedDownloadingProgress();
//        }
//    }
//
//    private void completedDownloadingProgress() {
//        Log.i("", "completedDownloadingProgress ");
//        // Reset download counter
//        completed_item_index = 0;
//
//        // Hide progress dialog
//        Utils.hideWaitingDialog();
//
//        // Refresh views
//        if (!IS_CONNECTED_CARD) {
//            /**
//             * Case : not connected to card
//             */
//            Cloudstringers.onCallbackResetViewsListener
//                    .onResetViews(false, Cloudstringers.mGvFileView, FileViewAsync.mAlCompletedItems);
//        } else {
//            /**
//             * Case : connected to card
//             */
//            Cloudstringers.onCallbackResetViewsListener
//                    .onResetViews(true, HomeRealCardFragment.mGv, GetPreviewDownloadThumbnailAsync.mAlCompletedItems);
//        }
//
//        // Finish current activity
//        if (IS_FINISHED_CURRENT_ACTIVITY) {
//            IS_FINISHED_CURRENT_ACTIVITY = false;
//
//            ((Activity) mContext).finish();
//        }
//    }
}
