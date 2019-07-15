package elixer.com.bloodbank.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import elixer.com.bloodbank.models.User;
import elixer.com.bloodbank.util.FirebaseQueryLiveData;
import elixer.com.bloodbank.util.Resource;

public class ResponseRepository {

    private static ResponseRepository instance;
    private DatabaseReference mDatabase;
    private static FirebaseAuth mAuth;
    private List<User> responseList = new ArrayList<>();
    private static final String TAG = "ResponseRepository";


    public static ResponseRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ResponseRepository(context);
        }
        return instance;
    }

    private ResponseRepository(Context context) {

    }


    @NonNull
    public LiveData<Resource<List<User>>> getUserLiveData() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("responses").child(mAuth.getUid());
        FirebaseQueryLiveData mLiveData = new FirebaseQueryLiveData(mDatabase);
        return Transformations.map(mLiveData, new DeserializerUserList());


    }

    private class DeserializerUserList implements Function<DataSnapshot, Resource<List<User>>> {

        @Override
        public Resource<List<User>> apply(DataSnapshot dataSnapshot) {
            responseList.clear();
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                //Log.d("TAG","Peeru Value"+snap.getValue().toString());
                User user = snap.getValue(User.class);
                responseList.add(user);
            }
            return Resource.success(responseList);
            //TODO: Define Resource.error for errror case or unable to fetch
        }
    }




}
