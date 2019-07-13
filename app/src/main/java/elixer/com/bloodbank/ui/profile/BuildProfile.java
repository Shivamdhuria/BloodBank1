package elixer.com.bloodbank.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Arrays;
import java.util.List;

import elixer.com.bloodbank.BuildConfig;
import elixer.com.bloodbank.R;
import elixer.com.bloodbank.models.Profile;
import elixer.com.bloodbank.models.User;
import elixer.com.bloodbank.ui.main.MainActivity;
import elixer.com.bloodbank.util.LocalProperties;

public class BuildProfile extends AppCompatActivity {

    private static List<String> BLOOD_GROUPS;


    private Toolbar toolbar;
    int AUTOCOMPLETE_REQUEST_CODE = 1;

    Double latitude, longitude;
    String city;
    EditText name, age, phoneNumber;
    private Button submitButton;
    // Set the fields to specify which types of place data to
// return after the user has made a selection.
    List<Place.Field> fields;


    MaterialSpinner spinner;

    private SharedPreferences mSharedPreference;
    private BuildProfileViewModel mBuildProfileViewModel;
    private static final String TAG = "BuildProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_profile);
        submitButton = findViewById(R.id.btn_submit);
        name = (EditText) findViewById(R.id.editText_name);
        age = findViewById(R.id.editText_age);
        phoneNumber = findViewById(R.id.phone_edit);


        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        phoneNumber.setText(user.getPhoneNumber());




        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);


        setUpSpinner();
        mBuildProfileViewModel = ViewModelProviders.of(this).get(BuildProfileViewModel.class);
        subscribeObservers();

        // Initialize Places.
        Places.initialize(getApplicationContext(), BuildConfig.PlacesApiKey);

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                city = place.getName();
                latitude = (place.getLatLng().latitude);
                longitude = (place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToDatabase();
            }
        });

    }

    private void subscribeObservers() {
        mBuildProfileViewModel.getIsSuccesfullySavedToDatabase().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {

                if (aBoolean != null) {
                    if (aBoolean) {


                        //Successfully wrote
                        Log.e(TAG, "Successfully Saved to Database " + aBoolean.toString());
                        //TODO Use live data for shared preference
                        saveUserToSharedPref();
                        startActivity(new Intent(getApplication(), MainActivity.class));


                    } else {

                        //Failed to Write Profile
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    private void saveUserToSharedPref() {
        LocalProperties localProperties = new LocalProperties(mSharedPreference);
        localProperties.saveUserObject(mBuildProfileViewModel.getProfile().getUser());
    }

    private void writeToDatabase() {
        User user = new User(name.getText().toString(), phoneNumber.getText().toString(), BLOOD_GROUPS.get(spinner.getSelectedIndex()),
                city, 0, Integer.parseInt(age.getText().toString()));
        Profile profile = new Profile(user, latitude, longitude);
        mBuildProfileViewModel.setProfile(profile);
        mBuildProfileViewModel.writeProfileToDatabase();
    }

    private void setUpSpinner() {
        BLOOD_GROUPS = Arrays.asList(getResources().getStringArray(R.array.blood_groups));
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setItems(BLOOD_GROUPS);

            }
        });
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });
    }


}
