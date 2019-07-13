package elixer.com.bloodbank.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.core.GeoHash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import elixer.com.bloodbank.models.Profile;

public class BuildProfileRepository {

    private static final String TAG = "BuildProfileRepository";

    private FirebaseUser currentFirebaseUser;
    private static BuildProfileRepository instance;

    public BuildProfileRepository() {
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public static BuildProfileRepository getInstance() {
        if (instance == null) {
            instance = new BuildProfileRepository();
        }
        return instance;
    }

    public LiveData<Boolean> AddProfileAndLocation(Profile profile) {
        final MutableLiveData<Boolean> status = new MutableLiveData<>();

        String userID = currentFirebaseUser.getUid();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, "USer Id  " + userID);

        GeoHash geoHash = new GeoHash(new GeoLocation(profile.getLatitude(), profile.getLongitude()));
        Map<String, Object> updates = new HashMap<>();

        updates.put("users/" + userID, profile.getUser());
        updates.put(profile.getBloodGroup() + "/" + userID + "/g", geoHash.getGeoHashString());
        updates.put(profile.getBloodGroup() + "/" + userID + "/l", Arrays.asList(profile.getLatitude(), profile.getLongitude()));
        mDatabase.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    //Success
                    Log.d(TAG, "onComplete: Profile Successfully Written to Database");
                    status.setValue(true);

                } else {
                    //Database Push failed
                    status.setValue(false);

                }
            }
        });
        return status;
    }
}
