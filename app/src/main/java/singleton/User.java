package singleton;

import define.Page;

/**
 * Created by trek2000 on 18/8/2014.
 */
public class User {

    /**
     * Single Ton method
     *
     * @return
     */
    private static User user = null;
    /**
     * Profile Information
     */
    private String FIRST_NAME = null;
    private int gender = 0;
    private String LAST_NAME = null;
    private long mobile_phone_number = 0;
    private int page = Page.all_company_feed;
    private String PASSWORD = null;
    /**
     * Google Cloud Messaging
     */
    private String RegistrationID = null;
    private String TOKEN = null;
    private long user_global_id = 0;
    private String USER_EMAIl = null;

    public static User getInstance() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public String getFirstName() {
        return FIRST_NAME;
    }

    public void setFirstName(String firstName) {
        FIRST_NAME = firstName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return LAST_NAME;
    }

    public void setLastName(String lastName) {
        LAST_NAME = lastName;
    }

    public long getMobilePhoneNumber() {
        return mobile_phone_number;
    }

    public void setMobilePhoneNumber(long MOBILE_PHONE_NUMBER) {
        this.mobile_phone_number = MOBILE_PHONE_NUMBER;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getPassword() {
        return PASSWORD;
    }

    public void setPassword(String password) {
        this.PASSWORD = password;
    }

    public String getRegistrationID() {
        return RegistrationID;
    }

    public void setRegistrationID(String registrationID) {
        RegistrationID = registrationID;
    }

    public String getToken() {
        return TOKEN;
    }

    public void setToken(String tokenString) {
        TOKEN = tokenString;
    }

    public long getUserGlobalID() {
        return user_global_id;
    }

    public void setUserGlobalID(long globalID) {
        user_global_id = globalID;
    }

    public String getUserEmail() {
        return USER_EMAIl;
    }

    public void setUserEmail(String email) {
        USER_EMAIl = email;
    }
}
