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

public class DonorListRepository {
    private static final String TAG = "DonorListRepository";

    private static DonorListRepository instance;
    private DatabaseReference mDatabase;
    private static FirebaseAuth mAuth;
    private List<String> donorKeyList;
    private MutableLiveData<List<String>> donorList;
    private MutableLiveData<Boolean> isRequestSuccessful;


    public static DonorListRepository getInstance(Context context) {
        if (instance == null) {
            instance = new DonorListRepository(context);
        }
        return instance;
    }

    private DonorListRepository(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        donorKeyList = new ArrayList<>();
        isRequestSuccessful = new MutableLiveData<>();


    }


    public void SearchForDonorsByLocation(String bloodgroup, Double latitude, Double longitude, int radius) {
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
                Log.d(TAG, "onKeyEntered:.... " + key);

                //If there's need to fetch users name and other info to display
//                getUserByKey(key);

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
    }

    public LiveData<List<String>> getDonorLiveData() {
        return donorList;
    }

    public void sendRequests(Request request) {

        isRequestSuccessful.setValue(false);
        Map<String, Object> updates = new HashMap<>();
        for (int i = 0; i < donorKeyList.size(); i++) {
            updates.put("/requests/" + donorKeyList.get(i) + "/" + mAuth.getUid() + "/", request);
        }
        mDatabase.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    isRequestSuccessful.setValue(true);
                }
            }
        });

    }

    public LiveData<Boolean> getIsRequestSuccessful() {
        return isRequestSuccessful;
    }

    public void setIsRequestSuccessful(MutableLiveData<Boolean> isRequestSuccessful) {
        this.isRequestSuccessful = isRequestSuccessful;
    }
    //        for (int i = 0; i < donorKeyList.size(); i++) {
//            mDatabase.child(donorKeyList.get(i)).child(Objects.requireNonNull(mAuth.getUid())).setValue(request)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                        isRequestSuccessful
//                            } else {
//
//                            }
//                        }
//                    });
//
//        }



//    private void getUserByKey(final String key) {
//        Query donorQuery = mDatabase.child("users").child(key);
//        donorQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                //The dataSnapshot should hold the actual data about the location
//                // dataSnapshot.getChild("name").getValue(String.class);
//                //should return the name of the location and dataSnapshot.getChild("description").getValue(String.class);
//                // should return the description of the locations
//                User userModel = dataSnapshot.getValue(User.class);
//                String name = userModel.getName();
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                if (!key.equals(FirebaseAuth.getInstance().getUid())) {
//                            userList.add(userModel);
//                            //Adding donorId to the list for requests database
//                            donorList.add(key);
//                }
//                        Log.e("USERList", userList.toString());
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//
//        });
//
//    }
}

//    @NonNull
//    public LiveData<Resource<List<User>>> getDonorLiveData() {
//        FirebaseQueryLiveData mLiveData = new FirebaseQueryLiveData(mDatabase);
//        return Transformations.map(mLiveData, new RequestsRepository.DeserializerUserList());
//
//
//    }

//    public class DeserializerUserList implements Function<DataSnapshot, Resource<List<User>>> {
//
//        @Override
//        public Resource<List<User>> apply(DataSnapshot dataSnapshot) {
//            uList.clear();
//            for (DataSnapshot snap : dataSnapshot.getChildren()) {
//                //Log.d("TAG","Peeru Value"+snap.getValue().toString());
//                User user = snap.getValue(User.class);
//                uList.add(user);
//            }
//
//            return Resource.success(uList);
//            //TODO: Define Resource.error for errror case or unable to fetch
//        }
//    }
//        return Transformations.map(mLiveData, new DeserializerUserList());
//}
