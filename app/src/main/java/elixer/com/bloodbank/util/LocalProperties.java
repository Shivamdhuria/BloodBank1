package elixer.com.bloodbank.util;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import elixer.com.bloodbank.models.User;

public class LocalProperties {

    private static final String TAG = "LocalProperties";
    private final SharedPreferences mSharedPreferences;
    public static final String TOKEN ="FIRST";

    public LocalProperties(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    public void storeIfFirstTime() {
        Log.e(TAG, "storeIfFirstTime: storing.." );
        mSharedPreferences.edit().putBoolean(TOKEN, false).apply();
    }

    public Boolean retrieveIfFirstTime() {
        return mSharedPreferences.getBoolean(TOKEN, true);

    }

    public void saveUserObject(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        mSharedPreferences.edit().putString("User", json).apply();

    }

    public User retrieveUserObject() {
        Gson gson = new Gson();
        String json = mSharedPreferences.getString("User", "");
        User user = gson.fromJson(json, User.class);
        return user;
    }



}
