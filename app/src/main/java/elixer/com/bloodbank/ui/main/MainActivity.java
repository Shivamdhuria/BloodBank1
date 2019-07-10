package elixer.com.bloodbank.ui.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

import elixer.com.bloodbank.BaseActivity;
import elixer.com.bloodbank.R;
import elixer.com.bloodbank.adapters.SectionsPageAdapter;
import elixer.com.bloodbank.ui.IntroActivity;
import elixer.com.bloodbank.ui.campaign.NewCampaignActivity;
import elixer.com.bloodbank.ui.profile.BuildProfile;
import elixer.com.bloodbank.ui.reponses.ResponsesFragment;
import elixer.com.bloodbank.ui.requests.RequestsFragment;
import elixer.com.bloodbank.util.LocalProperties;

import static elixer.com.bloodbank.util.Constants.RC_SIGN_IN;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private SharedPreferences mSharedPreference;

    private MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Created...");


        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        //Check if App is run first Time
        checkIfFirstTime();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        subscribeObservers();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(view.getContext(), NewCampaignActivity.class));
            }
        });


    }

    private void subscribeObservers() {
        mMainViewModel.getIsSignedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Log.e(TAG, "onChanged:...... "+ aBoolean.toString() );
                if (aBoolean != null) {
                    //Start Sign In with AuthUI
                    if (!aBoolean) startSignIn();
                }
            }
        });
    }

    private void startSignIn() {

        // Choose authentication providers
        //Only Phone number for now
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }

    private void checkIfFirstTime() {
        LocalProperties localProperties = new LocalProperties(mSharedPreference);
        if (localProperties.retrieveIfFirstTime()) {
            Log.e(TAG, "checkIfFirstTime: " + localProperties.retrieveIfFirstTime());
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
            localProperties.storeIfFirstTime();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sign_out) {
//            AuthUI.getInstance().signOut(this);
//            mMainViewModel.setIsSignIn();
            mMainViewModel.setIsSignOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupViewPager(ViewPager viewPager) {
        mSectionsPageAdapter.addFragment(new ResponsesFragment(), "Response");
        mSectionsPageAdapter.addFragment(new RequestsFragment(), "Requests");
        viewPager.setAdapter(mSectionsPageAdapter);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(MainActivity.this, BuildProfile.class));

//                FirebaseUserMetadata metadata = FirebaseAuth.getInstance().getCurrentUser().getMetadata();
//                if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
//                    // The user is new, show them a fancy intro screen!
//                } else {
//                    // This is an existing user, show them a welcome back screen.
//                }
                finish();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
//                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
//                    showSnackbar(R.string.no_internet_connection);
                    return;
                }
//                showSnackbar(R.string.unknown_error);
//                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

}
