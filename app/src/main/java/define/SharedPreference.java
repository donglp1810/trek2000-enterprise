package define;

import android.content.SharedPreferences;

public class SharedPreference {
    /**
     * Shared preferences
     */
    public static final String BUYSELL = "sell";
    public static final String FIRMWARE_VERSION = "firmware_version";
    public static final String PASSWORD = "password";

    public static final String PREFS = "cloudstringers";

    public static final String REGISTRATION_ID = "registration_id";
    public static final String USER_GLOBAL_ID = "globalID";
    public static final String USER_EMAIL = "user_email";

    public static SharedPreferences mSp;
    public static SharedPreferences.Editor mSpEditor;
}
