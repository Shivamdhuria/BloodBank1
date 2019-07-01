package elixer.com.bloodbank.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import elixer.com.bloodbank.R;
import elixer.com.bloodbank.ui.main.MainActivity;
import elixer.com.bloodbank.util.LocalProperties;
import io.github.dreierf.materialintroscreen.MaterialIntroActivity;
import io.github.dreierf.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity {
    private static final String TAG = "IntroActivity";
    //Check if App is run first Time

    private SharedPreferences mSharedPreference;
    private LocalProperties localProperties;

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy:destroy ");
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        localProperties = new LocalProperties(mSharedPreference);
        checkIfFirstTime();

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorBackground)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.bllod256)
                .title(getString(R.string.intro1title))
                .description(getString(R.string.intro1description))
                .build());
        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorBackground)
                .buttonsColor(R.color.colorAccent)
                .neededPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE})
                .image(R.drawable.heart)
                .title(getString(R.string.intro2title))
                .description(getString(R.string.intro2description))
                .build());
        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorBackground)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.donate)
                .title(getString(R.string.intro3title))
                .description(getString(R.string.intro3description))
                .build());


    }

    @Override
    public void onFinish() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        localProperties.storeIfFirstTime();
        super.onFinish();

    }

    private void checkIfFirstTime() {
        if (!localProperties.retrieveIfFirstTime()) {
            Log.e(TAG, "checkIfFirstTime: " + localProperties.retrieveIfFirstTime());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}

