package elixer.com.bloodbank.ui;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;


import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import elixer.com.bloodbank.R;

public class IntroActivity extends MaterialIntroActivity {
    private static final String TAG = "IntroActivity";

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy:destroy " );
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        super.onFinish();

    }
}

