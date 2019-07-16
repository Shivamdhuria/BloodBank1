package elixer.com.bloodbank.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import elixer.com.bloodbank.models.Request;

public class CampaignRepository {
    private static final String TAG = "CampaignRepository";

    private static CampaignRepository instance;
    private DatabaseReference mDatabase;
    private static FirebaseAuth mAuth;
    private List<String> donorKeyList;
    private MutableLiveData<List<String>> donorList;


    public static CampaignRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CampaignRepository(context);
        }
        return instance;
    }

    private CampaignRepository(Context context) {

        donorKeyList = new ArrayList<>();

    }


    public LiveData<List<String>> SearchForDonorsByLocation(String bloodgroup, Double latitude, Double longitude, int radius) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        donorKeyList.clear();
        if (donorList == null) {
            donorList = new MutableLiveData<>();
        }

        final DatabaseReference ref = mDatabase.child(bloodgroup);
        GeoFire geoFire = new GeoFire(ref);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), radius);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, GeoLocation location) {
                //Any location key which is within "radius" from the user's location will show up here as the key parameter in this method
                //If there's need to fetch users name and other info to display

                if (!key.equals(mAuth.getUid())) {
                    donorKeyList.add(key);

                }
            }

            @Override
            public void onKeyExited(String key) {
//                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
            }

            @Override
            public void onGeoQueryReady() {
                //This method will be called when all the locations which are within 3km from the user's location has been loaded Now you can do what you wish with this data
                donorList.setValue(donorKeyList);

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

                //In case of error , set value to -1
                donorKeyList.clear();
                donorKeyList.add("-1");
                donorList.setValue(donorKeyList);

            }
        });

        return donorList;
    }


    public LiveData<Boolean> sendRequests(Request request) {
        final MutableLiveData<Boolean> val = new MutableLiveData<>();
        Map<String, Object> updates = new HashMap<>();
        for (int i = 0; i < donorKeyList.size(); i++) {
            updates.put("/requests/" + donorKeyList.get(i) + "/" + mAuth.getUid() + "/", request);
        }
        mDatabase.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    val.setValue(true);
                } else {
                    val.setValue(false);
                }
            }
        });
        return val;
    }


}