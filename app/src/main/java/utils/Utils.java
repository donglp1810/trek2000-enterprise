package utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.trek2000.android.enterprise.R;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import custom_view.DialogWarning;
import define.Extension;
import ui.activity.CustomCamera;

public class Utils {

    /**
     * String section
     */
    public static boolean IS_SOFT_KEYBOARD_SHOWING = false;

    // type definition for rotate
    public static final int FLIP_VERTICAL = 1;
    public static final int FLIP_HORIZONTAL = 2;

    /**
     * View section
     */
    public static Dialog mDialogWaiting = null;
    public static Dialog mDialogWarning = null;

    private static ProgressDialog mPdWaiting = null;

    /**
     * invoke the system's media scanner to add your photo
     * to the Media Provider's database, making it available in the Android Gallery application and to other apps.
     */
    public static void addPictureToGallery(Context mContext, String realFilePathFromMedia) {
        Intent mIntentMediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File mFile = new File(realFilePathFromMedia);
        Uri mUriContent = Uri.fromFile(mFile);
        mIntentMediaScan.setData(mUriContent);
        ((Activity) mContext).sendBroadcast(mIntentMediaScan);
    }

    /**
     * Check if this device has a mCamera
     */
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a mCamera
            return true;
        } else {
            // no mCamera on this device
            return false;
        }
    }

    /**
     * Check fill blank
     */
    public static boolean checkFillBlank(EditText et) {
        if (et.getText().toString().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check fill password characters At least 8 characters
     */
    public static boolean checkValidatePassword(EditText et, int number) {
        if (et.getText().toString().length() >= number) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Validate password with regular expression
     *
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public static boolean checkValidatePassword(final String password) {
        String PASSWORD_PATTERN =
                "((?=.*\\d)(?=.*[a-z]).{6,20})";

        Matcher matcher = Pattern.compile(PASSWORD_PATTERN).matcher(password);
        return matcher.matches();

    }

    /**
     * Clear Cache
     *
     * @param mContext
     * @return
     */
    public static boolean clearCache(Context mContext) {
        PackageManager mPm = mContext.getPackageManager();
        // Get all methods on the PackageManager
        Method[] mMethods = mPm.getClass().getDeclaredMethods();
        for (Method mMethod : mMethods) {
            if (mMethod.getName().equals("freeStorage")) {
                // Found the method I want to use
                try {
                    long desired_free_storage = 8 * 1024 * 1024 * 1024; // Request for 8GB of free space
                    mMethod.invoke(mPm, desired_free_storage, null);

                    return true;
                } catch (Exception e) {
                    // Method invocation failed. Could be a permission problem
                    e.printStackTrace();
                }
                break;
            }
        }

        return false;
    }

    /**
     * Clear Data application
     *
     * @param mContext
     * @return
     */
    public static boolean clearData(Context mContext) {
        File dir = mContext.getCacheDir();

        // The directory is now empty so album it
        return dir.delete();
    }

    /**
     * Clear Data application
     *
     * @param mContext
     * @return
     */
    public static void clearOldBackStack(Context mContext) {
        // Try to clear old fragments stored in Back stack, avoid case
        // clicked Back hardware button, it will show overlay UI
        FragmentManager fm = ((FragmentActivity) mContext).getSupportFragmentManager();

        try {
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * compress image
     */
    public static Bitmap compressImage(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        return decoded;
    }

    public static int[] convertDateFromString(String[] SPLIT_PATH_DATE) {
        Date mDate = new Date();
        // 11 - 1 -> NOV
        // 1 - 1 -> Jan
        // 0 - 1 -> Dec
        int month = Integer.valueOf(SPLIT_PATH_DATE[1]) - 1;
        if (month == -1) month = Calendar.DECEMBER;
        mDate.setMonth(month);

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(mDate);
        int year = Integer.valueOf(SPLIT_PATH_DATE[0]);
        // Dec -> 11
        int month_of_year = mCalendar.get(Calendar.MONTH);
        int day_of_month = Integer.valueOf(SPLIT_PATH_DATE[2]);

        return new int[]{year, month_of_year, day_of_month};
    }

    /**
     * detected soft key board is showing
     */
    public static boolean detectedSoftKeyboardIsShowing(Context mContext, final int resID) {
        final View activityRootView = ((Activity) mContext).findViewById(resID);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight()
                        - activityRootView.getHeight();
                if (heightDiff > 100) {
                    IS_SOFT_KEYBOARD_SHOWING = true;
                }
            }
        });

        return IS_SOFT_KEYBOARD_SHOWING;
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
//	public static boolean checkPlayServices(Context mContext) {
//		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
//		if (resultCode != ConnectionResult.SUCCESS) {
//			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//				GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) mContext,
//						9000).show();
//			} else {
//				Log.e("", "This device is not supported.");
//				((Activity) mContext).finish();
//			}
//			return false;
//		}
//		return true;
//	}

    /**
     * Flip Bitmap
     * @param src
     * @param type
     * @return
     */
    public static Bitmap flipBitmap(Bitmap src, int type) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();
        // if vertical
        if (type == 1) {
            // y = y * -1
            matrix.preScale(1.0f, -1.0f);
        }
        // if horizontal
        else if (type == 2) {
            // x = x * -1
            matrix.preScale(-1.0f, 1.0f);
            // unknown type
        } else {
            return null;
        }

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    /**
     * Get image from Asset folder
     *
     * @param context
     * @param filename
     * @return
     * @throws IOException
     */
    public static Drawable getAssetImage(Context context, String filename) throws IOException {
        AssetManager mAm = context.getResources().getAssets();
        InputStream mIs = new BufferedInputStream((mAm.open("drawable-hdpi/" + filename + ".png")));
        Bitmap mBitmap = BitmapFactory.decodeStream(mIs);
        return new BitmapDrawable(context.getResources(), mBitmap);
    }

    public static BitmapFactory.Options getBitmapOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();

//        options.outWidth = 1920;
//        options.outHeight = 1080;
//        options.inScreenDensity = 480;
//        options.inTargetDensity = 480;

        options.inPreferQualityOverSpeed = true;
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inSampleSize = 4;
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        return options;
    }

    /**
     * Get current day
     *
     * @return
     */
    public static String getCurrentDate() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DAY_OF_MONTH, 0);

        /**
         * Add Date of 12 AM
         */
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);

        Date mDateBegin = mCalendar.getTime();

        /**
         * Add Date of 11 PM
         */
        mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);

        Date mDateEnd = mCalendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");

        return sdf.format(mDateBegin);
    }

    /**
     * Get current month
     *
     * @return
     */
    public static String getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        return sdf.format(cal.getTime());
    }

    public static String getCurrentWeek(Calendar mCalendar) {
        Date date = new Date();
        mCalendar.setTime(date);

        // 1 = Sunday, 2 = Monday, etc.
        int day_of_week = mCalendar.get(Calendar.DAY_OF_WEEK);

        int monday_offset;
        if (day_of_week == 1) {
            monday_offset = -6;
        } else
            monday_offset = (2 - day_of_week); // need to minus back
        mCalendar.add(Calendar.DAY_OF_YEAR, monday_offset);

        Date mDateMonday = mCalendar.getTime();

        // Save Monday Date into singleton

        // return 6 the next days of current day (object cal save current day)
        mCalendar.add(Calendar.DAY_OF_YEAR, 6);
        Date mDateSunday = mCalendar.getTime();

        // Save Sunday date into singleton

        // Get format date
        String strDateFormat = "dd MMM";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        String MONDAY = sdf.format(mDateMonday);
        String SUNDAY = sdf.format(mDateSunday);

        // Sub String
        if ((MONDAY.substring(3, 6)).equals(SUNDAY.substring(3, 6))) {
            MONDAY = MONDAY.substring(0, 2);
        }

        return MONDAY + " - " + SUNDAY;
    }

    /**
     * Get current year
     *
     * @return
     */
    public static String getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(cal.getTime());
    }

    /**
     * Return date in specified format.
     *
     * @param milliSeconds Date in milliseconds
     * @param dateFormat   Date format
     * @return String representing date in specified format
     */
    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified
        // format.
        DateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in
        // milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getDurationOfVideo(Context mContext, String FILE_PATH) {
        MediaPlayer mp = MediaPlayer.create(mContext, Uri.parse(FILE_PATH));
        int duration = mp.getDuration();
        mp.release();
        /*convert millis to appropriate time*/
        return String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
    }

    /**
     * Get Float format with 4 digits
     */
    public static String getFloatFormat(float value) {
        return String.format("%.2f", value);
    }

    /**
     * Get Uri from Image
     */
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIpAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    /**
     * Get yesterday
     *
     * @return
     */
    public static String getLastDate(int i) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -i);

        /**
         * Add Date of 12 AM
         */
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        Date mDateBegin = cal.getTime();

        /**
         * Add Date of 11 PM
         */
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        Date mDateEnd = cal.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");

        return sdf.format(mDateBegin);
    }

    /**
     * Get previous month
     *
     * @return
     */
    public static String getLast1Month(int i) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -i);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        return sdf.format(cal.getTime());
    }

    /**
     * Get previous 3 months ago
     *
     * @return
     */
    public static String getLast3Months(int i) {
        Calendar cal = Calendar.getInstance();

        if (i == 0) {
            cal.add(Calendar.MONTH, -3 * i);
        } else {
            cal.add(Calendar.MONTH, -3 * i + 1);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        return sdf.format(cal.getTime());
    }

    public static String getLast3MonthsFirstIndex(int i) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -3 * i);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        return sdf.format(cal.getTime());
    }

    public static String getLastWeek(Calendar mCalendar) {
        // Monday
        mCalendar.add(Calendar.DAY_OF_YEAR, -13);
        Date mDateMonday = mCalendar.getTime();

        // Save into singleton

        // Sunday
        mCalendar.add(Calendar.DAY_OF_YEAR, 6);
        Date mDateSunday = mCalendar.getTime();

        // Save into singleton

        // Date format
        String strDateFormat = "dd MMM";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        String MONDAY = sdf.format(mDateMonday);
        String SUNDAY = sdf.format(mDateSunday);

        // Substring
        if ((MONDAY.substring(3, 6)).equals(SUNDAY.substring(3, 6))) {
            MONDAY = MONDAY.substring(0, 2);
        }

        return MONDAY + " - " + SUNDAY;
    }

    /**
     * Get previous year ago
     *
     * @return
     */
    public static String getLastYear(int i) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -i);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(cal.getTime());
    }

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    public static String getMacAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    public static String getNextWeek(Calendar mCalendar) {
        // Monday
        mCalendar.add(Calendar.DAY_OF_YEAR, 1);
        Date mDateMonday = mCalendar.getTime();

        // Save into singleton

        // Sunday
        mCalendar.add(Calendar.DAY_OF_YEAR, 6);
        Date Week_Sunday_Date = mCalendar.getTime();

        // Save into singleton

        // Date format
        String strDateFormat = "dd MMM";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        String MONDAY = sdf.format(mDateMonday);
        String SUNDAY = sdf.format(Week_Sunday_Date);

        // Sub string
        if ((MONDAY.substring(3, 6)).equals(SUNDAY.substring(3, 6))) {
            MONDAY = MONDAY.substring(0, 2);
        }

        return MONDAY + " - " + SUNDAY;
    }

    /**
     * get Number (float or int) from String
     */
    public static String getNumberFromSpecifiedString(String str) {
        str = str.replaceAll("[^\\d.]", "");
        return str;
    }

    /**
     * Get all images from Sd card to load into Custom Gallery
     */
    public static ArrayList<File> getPhotoAndVideoFromSdCard(ArrayList<File> mAlFiles, File dir) {
        File mFileList[] = dir.listFiles();
        if (mFileList != null && mFileList.length > 0) {
            for (int i = 0; i < mFileList.length; i++) {
                if (mFileList[i].isDirectory() & !mFileList[i].isHidden()) {
                    // Add folder if need
                    if (!mAlFiles.contains(mFileList[i]))
                        mAlFiles.add(mFileList[i]);

                    // Should skip folder Android before add new files
                    getPhotoAndVideoFromSdCard(mAlFiles, mFileList[i]);
                } else if (mFileList[i].isFile() & !mFileList[i].isHidden()) {

                    /**
                     * Check to put only photo, video files to show in Custom Gallery
                     */
                    if (mFileList[i].getName().toLowerCase().endsWith(Extension.JPEG)
                            | mFileList[i].getName().toLowerCase().endsWith(Extension.JPG)
                            | mFileList[i].getName().toLowerCase().endsWith(Extension.PNG)
                            | mFileList[i].getName().toLowerCase().endsWith(Extension.MP4)) {
                        mAlFiles.add(mFileList[i]);
                    }
                }
            }
        }
        return mAlFiles;
    }

    /**
     * Get all images from Sd card to load into Custom Gallery
     */
    public static ArrayList<File> getPhotoAndVideoFromSdCard(
            boolean isFolderOrFile, ArrayList<File> mAlFiles, File dir) {
        File mFileList[] = dir.listFiles();
        if (mFileList != null && mFileList.length > 0) {
            for (int i = 0; i < mFileList.length; i++) {
                if (mFileList[i].isDirectory() & !mFileList[i].isHidden()) {
                    // Add folder if need
                    if (isFolderOrFile & !mAlFiles.contains(mFileList[i]))
                        mAlFiles.add(mFileList[i]);

//                    Log.i("", "mFileList[i] " + mFileList[i].getAbsolutePath());

                    // Should skip folder Android before add new files
                    getPhotoAndVideoFromSdCard(true, mAlFiles, mFileList[i]);
                } else if (mFileList[i].isFile() & !mFileList[i].isHidden()) {

                    /**
                     * Check to put only photo, video files to show in Custom Gallery
                     */
                    if (!isFolderOrFile) {
//                        Log.i("", "getName() " + mFileList[i].getName());

                        if (mFileList[i].getName().toLowerCase().endsWith(Extension.JPEG)
                                | mFileList[i].getName().toLowerCase().endsWith(Extension.JPG)
                                | mFileList[i].getName().toLowerCase().endsWith(Extension.PNG)
                                | mFileList[i].getName().toLowerCase().endsWith(Extension.MP4)) {
                            mAlFiles.add(mFileList[i]);
                        }
                    }
                }
            }
        }
        return mAlFiles;
    }

    /**
     *
     * @param isPhotoOrVideo
     * @param context
     * @param imageFile
     * @return
     */
    public static Uri getImagePreviewOfUri(boolean isPhotoOrVideo, Context context, File imageFile) {
        String FILE_PATH = imageFile.getAbsolutePath();

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        Cursor mCursor = null;
        if (isPhotoOrVideo)
            mCursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media._ID},
                    MediaStore.Images.Media.DATA + "=? ",
                    new String[]{FILE_PATH}, null);
        else
            mCursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Video.Media._ID},
                    MediaStore.Video.Media.DATA + "=? ",
                    new String[]{FILE_PATH}, null);

        if (mCursor != null && mCursor.moveToFirst()) {
            int id = mCursor.getInt(mCursor.getColumnIndex(MediaStore.MediaColumns._ID));

            if (isPhotoOrVideo)
                return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
            else
                return Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();

                if (isPhotoOrVideo) {
                    values.put(MediaStore.Images.Media.DATA, FILE_PATH);
                    return context.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                } else {
                    values.put(MediaStore.Video.Media.DATA, FILE_PATH);
                    return context.getContentResolver().insert(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                }
            } else {
                return null;
            }
        }
    }

    /**
     * Get real path from Uri
     *
     * @param context
     * @return
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaColumns.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);

            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else
                return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @author paulburke
     */
//    public static String getPath(final Context context, final Uri uri) {
//
//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//
//        // DocumentProvider
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(uri)) {
//
//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
//            }
//            // MediaProvider
//            else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[] {
//                        split[1]
//                };
//
//                return getDataColumn(context, contentUri, selection, selectionArgs);
//            }
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            return getDataColumn(context, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }
//
//    /**
//     * Get the value of the data column for this Uri. This is useful for
//     * MediaStore Uris, and other file-based ContentProviders.
//     *
//     * @param context The context.
//     * @param uri The Uri to query.
//     * @param selection (Optional) Filter used in the query.
//     * @param selectionArgs (Optional) Selection arguments used in the query.
//     * @return The value of the _data column, which is typically a file path.
//     */
//    public static String getDataColumn(Context context, Uri uri, String selection,
//                                       String[] selectionArgs) {
//
//        Cursor cursor = null;
//        final String column = "_data";
//        final String[] projection = {
//                column
//        };
//
//        try {
//            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
//                                                        null);
//            if (cursor != null && cursor.moveToFirst()) {
//                final int column_index = cursor.getColumnIndexOrThrow(column);
//                return cursor.getString(column_index);
//            }
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//        return null;
//    }
//
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is ExternalStorageProvider.
//     */
//    public static boolean isExternalStorageDocument(Uri uri) {
//        return "com.android.externalstorage.documents".equals(uri.getAuthority());
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is DownloadsProvider.
//     */
//    public static boolean isDownloadsDocument(Uri uri) {
//        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is MediaProvider.
//     */
//    public static boolean isMediaDocument(Uri uri) {
//        return "com.android.providers.media.documents".equals(uri.getAuthority());
//    }

    /**
     * @param mContext
     * @return
     */
    public static int[] getSizeOfScreen(Context mContext) {
        Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();

        int[] SIZE = new int[2];
        // Width
        SIZE[0] = display.getWidth();
        // Height
        SIZE[1] = display.getHeight();

        return SIZE;
    }

    /**
     *
     * @param mContext
     * @param media_type_of_latest_bitmap_folder_thumbnail
     * @param mBitmap
     * @param THUMBNAIL_PATH
     * @return
     */
    public static Bitmap getThumbnail(
            Context mContext,
            boolean media_type_of_latest_bitmap_folder_thumbnail,
            Bitmap mBitmap, String THUMBNAIL_PATH) {
        if (media_type_of_latest_bitmap_folder_thumbnail) {
            // Photos
            Cursor ca = mContext.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.MediaColumns._ID},
                    MediaStore.MediaColumns.DATA + "=?", new String[]{THUMBNAIL_PATH}, null);
            if (ca != null && ca.moveToFirst()) {
                int id = ca.getInt(ca.getColumnIndex(MediaStore.MediaColumns._ID));
                ca.close();
                return MediaStore.Images.Thumbnails.getThumbnail(
                        mContext.getContentResolver(),
                        id,
                        MediaStore.Images.Thumbnails.MICRO_KIND,
                        null);
            }

            ca.close();
        } else {
            // Videos
//            mBitmap = ThumbnailUtils.createVideoThumbnail(
//                    THUMBNAIL_PATH, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//            mBitmap = ThumbnailUtils.extractThumbnail(
//                    mBitmap,
//                    mContext.getResources().getInteger(R.integer.layout_height_item_in_custom_gallery),
//                    mContext.getResources().getInteger(R.integer.layout_width_item_in_custom_gallery));

            mBitmap = ThumbnailUtils.createVideoThumbnail(THUMBNAIL_PATH,
                    MediaStore.Images.Thumbnails.MINI_KIND);

        }
        return mBitmap;
    }

    public static String getWifiApIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("wlan")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                            .hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()
                                && (inetAddress.getAddress().length == 4)) {
                            Log.d("", inetAddress.getHostAddress());
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("", ex.toString());
        }
        return null;
    }

    /**
     * Initial data for full screen dialog
     */
    public void initialFullScreenDialog(Dialog dialog) {
        // Initial layout for dialog
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.gravity = Gravity.CENTER;
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;

        dialog.getWindow().setAttributes(lp);
    }

    /**
     * Initial data for full screen dialog
     */
    public void initialFullWidthScreenWithFixedSizeDialog(Context mContext, Dialog dialog) {
        // Initial layout for dialog
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.gravity = Gravity.BOTTOM;
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = Utils.getSizeOfScreen(mContext)[1] * 6 / 7;

        dialog.getWindow().setAttributes(lp);
    }

    /**
     * Initial data for full screen dialog
     */
    public static void initialFullWidthScreenDialog(Dialog dialog, int gravity) {
        // Initial layout for dialog
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.gravity = gravity;
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);
    }

    /**
     * Check network connection
     */
    public static boolean isNetworkOnline(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mWifi.isConnected();
    }

    /**
     * Get media type
     *
     * @param FILE_PATH
     * @return
     */
    public static int isPhotoOrVideo(String FILE_PATH) {
        int is_photo_or_video = -1;
        // Media type
        if (FILE_PATH.toLowerCase().contains(Extension.JPEG)
                || FILE_PATH.toLowerCase().contains(Extension.JPG)
                || FILE_PATH.toLowerCase().contains(Extension.PNG)) {
            // Set type : Photo - true : 0
            is_photo_or_video = 0;
        } else if (FILE_PATH.toLowerCase().contains(Extension.MP4)) {
            // Set type : Video - false : 1
            is_photo_or_video = 1;
        }

        return is_photo_or_video;
    }

    /**
     * Check is router network or something else, like hot spot ...
     */
    public static boolean isRouterNetwork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mWifi.isConnected();
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param mEtEmail
     * @return boolean true for valid false for invalid
     */
    public static boolean isValidEmail(EditText mEtEmail) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = mEtEmail.getText().toString();

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isWifiTurnedOn(Context mContext) {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }

    /**
     * Hide progress dialog
     */
    public static void hideWaitingDialog() {
        if (mDialogWaiting != null) {
            mDialogWaiting.dismiss();
            mDialogWaiting = null;
        }
    }

    /**
     * Hide progress dialog
     */
    public static void hideProgressDialog() {
        if (mPdWaiting != null) {
            mPdWaiting.dismiss();
            mPdWaiting = null;
        }
    }

    /**
     * Hidden keyboard of edit text
     */
    public static void hideSoftKeyboard(Context mContext, EditText et) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getApplicationWindowToken(), 0);
    }

    /**
     * Hidden keyboard of edit text
     */
    public static void hideSoftKeyboard(Context mContext) {
        ((Activity) mContext).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    /**
     * remove String Duplicates
     */
    public static String removeDuplicatesInString(String str) {
        str = str.trim();
        List<String> mAlResult = new ArrayList<String>();
        String[] mASplitString = str.split(",");
        StringBuilder mSbResult = new StringBuilder();
        for (int i = 0; i < mASplitString.length; ++i) {
            if (!mAlResult.contains(mASplitString[i])) {
                mAlResult.add(mASplitString[i]);
                mSbResult.append(',');
                mSbResult.append(mASplitString[i]);
            }
        }
        return mSbResult.substring(1);

    }

    /**
     * Remove view parent
     */
    public static void removeViewParent(View v) {
        ((ViewGroup) v.getParent()).removeView(v);
    }

    /**
     *
     * @param mBitmap
     * @return
     */
    public static Bitmap rotateBackImage(Bitmap mBitmap) {
        // set rotate in here
        Matrix matrix = new Matrix();

        /**
         * Define Orientation of image in here,
         * if in portrait mode, use value = 90,
         * if in landscape mode, use value = 0
         */
        switch (CustomCamera.current_orientation) {
            case 0:
                matrix.postRotate(90);
                break;
            case 90:
                matrix.postRotate(180);
                break;
            case 180:
                matrix.postRotate(270);
                break;
            case 270:
                matrix.postRotate(0);
                break;
        }

        return Bitmap.createBitmap(
                mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
    }

    /**
     *
     * @param mContext
     * @param rotated
     * @return
     */
    public static Bitmap rotateFrontImage(Context mContext, Bitmap rotated) {
        Bitmap scale = null;
        switch (CustomCamera.current_orientation) {
            case 0:
                // Portrait mode
                // Should scale image more bigger
                scale = Bitmap.createScaledBitmap(
                        rotated,
                        getSizeOfScreen(mContext)[1] * rotated.getWidth() / rotated.getHeight(),
                        getSizeOfScreen(mContext)[1],
                        true);

                scale = flipBitmap(scale, FLIP_VERTICAL);
                break;
            case 90:
                // Landscape mode
                // Should scale image more bigger
                scale = Bitmap.createScaledBitmap(
                        rotated,
                        getSizeOfScreen(mContext)[0],
                        getSizeOfScreen(mContext)[0] * rotated.getHeight() / rotated.getWidth(),
                        true);

                scale = flipBitmap(scale, FLIP_HORIZONTAL);
                break;
            case 180:
                // Portrait mode
                // Should scale image more bigger
                scale = Bitmap.createScaledBitmap(
                        rotated,
                        getSizeOfScreen(mContext)[1] * rotated.getWidth() / rotated.getHeight(),
                        getSizeOfScreen(mContext)[1],
                        true);
                scale = flipBitmap(scale, FLIP_VERTICAL);
                break;
            case 270:
                // Landscape mode
                // Should scale image more bigger
                scale = Bitmap.createScaledBitmap(
                        rotated,
                        Utils.getSizeOfScreen(mContext)[0],
                        Utils.getSizeOfScreen(mContext)[0] * rotated.getHeight() / rotated.getWidth(),
                        true);

                scale = flipBitmap(scale, FLIP_HORIZONTAL);
                break;
        }

        return scale;
    }

    /**
     *
     * @param mMediaRecorder
     * @return
     */
    public static MediaRecorder rotateBackVideo(MediaRecorder mMediaRecorder) {
        /**
         * Define Orientation of video in here,
         * if in portrait mode, use value = 90,
         * if in landscape mode, use value = 0
         */
        switch (CustomCamera.current_orientation) {
            case 0:
                mMediaRecorder.setOrientationHint(90);
                break;
            case 90:
                mMediaRecorder.setOrientationHint(180);
                break;
            case 180:
                mMediaRecorder.setOrientationHint(270);
                break;
            case 270:
                mMediaRecorder.setOrientationHint(0);
                break;
        }

        return mMediaRecorder;
    }

    /**
     *
     * @param mMediaRecorder
     * @return
     */
    public static MediaRecorder rotateFrontVideo(MediaRecorder mMediaRecorder) {
        /**
         * Define Orientation of video in here,
         * if in portrait mode, use value = 90,
         * if in landscape mode, use value = 0
         */
        switch (CustomCamera.current_orientation) {
            case 0:
                mMediaRecorder.setOrientationHint(270);
                break;
            case 90:
                mMediaRecorder.setOrientationHint(180);
                break;
            case 180:
                mMediaRecorder.setOrientationHint(90);
                break;
            case 270:
                mMediaRecorder.setOrientationHint(0);
                break;
        }

        return mMediaRecorder;
    }

    /**
     * Set only default language
     *
     * @param context
     * @param lang
     */
    public static void setDefaultLanguage(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

    /**
     * Show action sheet dialog
     */
    public static void showActionSheetDialog(Dialog mDialog) {
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.height = LayoutParams.WRAP_CONTENT;
        wlp.width = LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
    }

    /**
     * Show action sheet popup dialog
     */
    public static PopupWindow showActionSheetDialog(Context context, View v) {
        // Get display of the screen
        // Creating the PopupWindow
        final PopupWindow mPw = new PopupWindow(context);
        mPw.setContentView(v);
        mPw.setWidth(LayoutParams.MATCH_PARENT);
        mPw.setHeight(LayoutParams.WRAP_CONTENT);
        mPw.setFocusable(true);

        // Displaying the pop up at the specified location.
        try {
            mPw.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mPw;
    }

    /**
     * Show progress dialog
     */
    public static void showProgressDialog(Context mContext) {
        if (mPdWaiting == null) {
            mPdWaiting = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_DARK);
            mPdWaiting.setCancelable(false);
            mPdWaiting.show();
        }
    }

    /**
     * Show progress dialog
     */
    public static ProgressDialog showProgressDialog(Context mContext, String title, String msg) {
        if (mPdWaiting == null) {
            mPdWaiting = new ProgressDialog(mContext);
            mPdWaiting.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mPdWaiting.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mPdWaiting.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mPdWaiting.setMessage(msg);
            mPdWaiting.setTitle(title);

            mPdWaiting.setCancelable(false);
            mPdWaiting.show();
        }

        return mPdWaiting;
    }

    /**
     * Show soft keyboard
     */
    public static void showSoftKeyBoard(Context mContext, EditText mEt) {
        InputMethodManager imm = (InputMethodManager)
                mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEt, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Show progress dialog
     */
    public static void showWaitingDialog(Context mContext, int resid) {
        if (mDialogWaiting == null) {
            mDialogWaiting = new Dialog(mContext);
            mDialogWaiting.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialogWaiting.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialogWaiting.setContentView(R.layout.simple_image_view_gif);

            ImageView mIv = (ImageView) mDialogWaiting.findViewById(R.id.gif_iv_);
            mIv.setImageResource(resid);

            mDialogWaiting.setCancelable(false);

            if (!mDialogWaiting.isShowing())
                mDialogWaiting.show();
        }
    }

    /**
     * Show Warning Dialog
     *
     * @param mContext
     * @param title
     * @param content
     */
    public static void showWarningDialog(
            boolean IS_SHOW_BOTH_DIALOG_INTERNET_AND_CARD,
            Context mContext, String title, String content) {
        try {
            // Dismiss current dialog
            if (mDialogWarning != null) {
                mDialogWarning.dismiss();

                mDialogWarning = null;
            }

            if (IS_SHOW_BOTH_DIALOG_INTERNET_AND_CARD) {
                /**
                 * In case show both Dialog No Internet & No Connection To Card
                 */
                new DialogWarning(
                        mContext,
                        ((Activity) mContext).getLayoutInflater().inflate(R.layout.dialog_warning, null),
                        content, title);
            } else {
                // In case show only 1 dialog : No Internet or No Connection To Card

                // Initialize and show dialog
                if (mDialogWarning == null) {
                    mDialogWarning = new DialogWarning(
                            mContext,
                            ((Activity) mContext).getLayoutInflater().inflate(R.layout.dialog_warning, null),
                            content, title);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
