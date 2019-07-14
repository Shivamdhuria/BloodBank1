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

import elixer.com.bloodbank.models.User;

public class BuildProfileRepository {

    private static final String TAG = "BuildProfileRepository";

    private FirebaseUser currentFirebaseUser;
    private static BuildProfileRepository instance;

    public BuildProfileRepository() {

    }

    public static BuildProfileRepository getInstance() {
        if (instance == null) {
            instance = new BuildProfileRepository();
        }
        return instance;
    }

    public LiveData<Boolean> AddUserAndLocation(User user, double lat, double longi) {
        final MutableLiveData<Boolean> status = new MutableLiveData<>();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentFirebaseUser.getUid();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, "USer Id  " + userID);

        GeoHash geoHash = new GeoHash(new GeoLocation(lat, longi));
        Map<String, Object> updates = new HashMap<>();

        updates.put("users/" + userID, user);
        updates.put(user.getBloodGroup() + "/" + userID + "/g", geoHash.getGeoHashString());
        updates.put(user.getBloodGroup() + "/" + userID + "/l", Arrays.asList(lat, longi));
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
