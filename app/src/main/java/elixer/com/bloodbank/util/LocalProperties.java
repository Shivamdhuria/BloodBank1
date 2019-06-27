package elixer.com.bloodbank.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import elixer.com.bloodbank.BuildConfig;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

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


}
