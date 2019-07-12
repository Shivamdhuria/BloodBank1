package elixer.com.bloodbank.ui.campaign;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import elixer.com.bloodbank.BuildConfig;
import elixer.com.bloodbank.R;
import elixer.com.bloodbank.models.Request;

public class QueryFragments extends Fragment {

    private static List<String> BLOOD_GROUPS;
    MaterialSpinner spinner;
    Double latitude, longitude;
    String city;
    private Request request;
    private NewCampaignViewModel viewModel;
    Button button;
    private static final String TAG = "QueryFragments";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.query_fragment, container, false);




    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSpinner(view);
        button =view.findViewById(R.id.button_radius);
        // Initialize Places.
        Places.initialize(view.getContext(), BuildConfig.PlacesApiKey);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

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
                Log.d(TAG, "onPlaceSelected: " + latitude + "    long" + longitude);
                //Save request Model
                saveInViewModel();
                //Launch search radius Fragment
                //  launchSearchRadiusFragment();


            }


            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSearchRadiusFragment();
            }
        });
    }

    private void saveInViewModel() {
        //Add name in shared Pref
        request = new Request("example", BLOOD_GROUPS.get(spinner.getSelectedIndex()), System.currentTimeMillis(), city, false);
        viewModel.setRequest(request);
        viewModel.setLatitude(latitude);
        viewModel.setLongitude(longitude);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(NewCampaignViewModel.class);

    }



    private void launchSearchRadiusFragment() {
        SearchRadiusFragment searchRadiusFragment = new SearchRadiusFragment();
        this.getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, searchRadiusFragment, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

    private void setUpSpinner(View view) {
        BLOOD_GROUPS = Arrays.asList(getResources().getStringArray(R.array.blood_groups));
        spinner = (MaterialSpinner) view.findViewById(R.id.spinner);
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