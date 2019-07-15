package elixer.com.bloodbank.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;
import java.util.List;

import elixer.com.bloodbank.BaseActivity;
import elixer.com.bloodbank.R;
import elixer.com.bloodbank.adapters.SectionsPageAdapter;
import elixer.com.bloodbank.models.User;
import elixer.com.bloodbank.ui.campaign.NewCampaignActivity;
import elixer.com.bloodbank.ui.profile.BuildProfile;
import elixer.com.bloodbank.ui.reponses.ResponsesFragment;
import elixer.com.bloodbank.ui.requests.RequestsFragment;
import elixer.com.bloodbank.util.LocalProperties;
import elixer.com.bloodbank.util.Resource;

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
    private TextView textViewName;
    private TextView textViewLevel;
    LocalProperties localProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Created...");


        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        localProperties = new LocalProperties(mSharedPreference);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        textViewName = findViewById(R.id.textView_name);
        textViewLevel = findViewById(R.id.textView_level);
        updateTextView();



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

    @Override
    public void showProgressBar(boolean visible) {
        super.showProgressBar(visible);
    }

    private void subscribeObservers() {
        //For observing Sign In State
        mMainViewModel.getIsSignedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    //Start Sign In with AuthUI
                    if (!aBoolean) {
                        startSignIn();
                    } else {
                        setupViewPager(mViewPager);
                    }
                }
            }
        });
        //For Observing if Uder isn't non null
        mMainViewModel.getUser().observe(this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> userResource) {
                if (userResource != null) {
                    switch (userResource.status) {
                        case LOADING:
                            //TODO: Show progress bar
                            showProgressBar(true);
                            break;
                        case ERROR:
                            //Launch BuildProfile Activity
                            showProgressBar(false);
                            Log.d(TAG, "onChanged: ERROR");
                            startActivity(new Intent(getApplicationContext(), BuildProfile.class));
                            finish();
                            break;
                        case SUCCESS:
                            Log.d(TAG, "onChanged: SUCEESS" + userResource.data.getName());
                            showProgressBar(false);
                            //TODO: Update TextView and Shared Preferences
                            updateSharedPref(userResource.data);
                            //Relaunching Activity as Fragments are hard to refresh for some reason
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();


                            break;


                    }
                }
            }
        });
    }

    private void updateSharedPref(User data) {
        localProperties.saveUserObject(data);

    }

    private void updateTextView() {
        if (localProperties.retrieveUserObject() != null) {
        textViewName.setText(localProperties.retrieveUserObject().getName());
            textViewLevel.setText(String.valueOf(localProperties.retrieveUserObject().getLevel()));
        }
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
        mSectionsPageAdapter.addFragment(new RequestsFragment(), "Requests");
        mSectionsPageAdapter.addFragment(new ResponsesFragment(), "Response");
        viewPager.setAdapter(mSectionsPageAdapter);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                showProgressBar(true);
                mMainViewModel.fetchUserDetailsFromDatabase();


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
