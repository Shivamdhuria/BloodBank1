package elixer.com.bloodbank.ui.campaign;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import elixer.com.bloodbank.R;

public class SearchRadiusFragment extends Fragment {

    private GoogleMap mMap;

    private String place;
    private Double latitude;
    private Double longitude;
    private SeekBar seekBar;
    private int radius = 10;
    private LatLng currentLocation;
    private Circle circle;
    private Button button_search;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_radius_fragmemt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        seekBar = view.findViewById(R.id.seekBar);
        button_search = view.findViewById(R.id.button_search);
        seekBar.setMax(70);
        seekBar.setProgress(radius);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                latitude = 12.9715987;
                longitude = 77.5945627;
                // Add a marker in Sydney and move the camera
                currentLocation = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(currentLocation).title(place));
                // mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLocation));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
                circle = mMap.addCircle(new CircleOptions()
                        .center(currentLocation)
                        .radius(radius * 1000)
                        .strokeWidth(0)
                        .strokeColor(Color.parseColor("#2271cce7"))
                        .fillColor(Color.parseColor("#2271cce7")));
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                radius = i;
                circle.setRadius(radius * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


    }


}
