package elixer.com.bloodbank.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import elixer.com.bloodbank.BuildConfig;
import elixer.com.bloodbank.R;
import elixer.com.bloodbank.repositories.BuildProfileRepository;

public class BuildProfile extends AppCompatActivity {

    private static List<String> BLOOD_GROUPS;

    private Button submitButton;
    private Toolbar toolbar;
    int AUTOCOMPLETE_REQUEST_CODE = 1;

    Double latitude, longitude;
    String city;
    EditText name, age, phoneNumber;
    // Set the fields to specify which types of place data to
// return after the user has made a selection.
    List<Place.Field> fields;

    BuildProfileRepository buildProfileRepository;
    MaterialSpinner spinner;
    private static final String TAG = "BuildProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_profile);
        submitButton = findViewById(R.id.btn_submit);
        name = (EditText) findViewById(R.id.name);
        age = findViewById(R.id.editText_age);
        phoneNumber = findViewById(R.id.phone_edit);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        buildProfileRepository = new BuildProfileRepository();
        setUpSpinner();
//        Log.e(TAG, "onCreate:"+  BuildConfig.API_KEY );

        // Initialize Places.
        Places.initialize(getApplicationContext(), BuildConfig.PLACES_API_KEY);

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
                //TODO: Remove all repository reference and use ViewModel
                buildProfileRepository.AddProfileAndLocation(phoneNumber.getText().toString(), BLOOD_GROUPS.get(spinner.getSelectedIndex()),
                        "shivam", city, 0, 23, latitude, longitude);
            }
        });

    }

    private void writeToDatabase(String toString, String s, EditText name, String city, int i, EditText age, Double latitude, Double longitude) {
        // buildProfileRepository.AddProfileAndLocation();
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
