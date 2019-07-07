package elixer.com.bloodbank.repositories;

import android.util.Log;

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

import androidx.annotation.NonNull;
import elixer.com.bloodbank.models.User;

public class BuildProfileRepository {

    private static final String TAG = "BuildProfileRepository";
    FirebaseUser currentFirebaseUser;

    public BuildProfileRepository() {
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void AddProfileAndLocation(String phoneNumber, String bloodGroup, String name, String city, int level, int age, Double latitude, Double longitude) {
        String userID = currentFirebaseUser.getUid();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, "USer Id  " + userID);

        final User user = new User(phoneNumber, bloodGroup, name, city, level, age);
        GeoHash geoHash = new GeoHash(new GeoLocation(latitude, longitude));
        Map<String, Object> updates = new HashMap<>();

        updates.put("users/" + userID, user);
        updates.put(bloodGroup + "/" + userID + "/g", geoHash.getGeoHashString());
        updates.put(bloodGroup + "/" + userID + "/l", Arrays.asList(latitude, longitude));
        mDatabase.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task == null) {
                    //Database Push failed

                } else {
                    //Success
                    Log.d(TAG, "onComplete: SUCESSFULLY WROTE");
                }


            }
        });


    }
}
