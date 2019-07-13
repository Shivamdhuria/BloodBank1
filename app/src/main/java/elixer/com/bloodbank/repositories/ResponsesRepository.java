package elixer.com.bloodbank.repositories;

import android.content.Context;

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

public class ResponsesRepository {
    private static ResponsesRepository instance;
    private DatabaseReference mDatabase;
    private static FirebaseAuth mAuth;
    private List<User> uList = new ArrayList<User>();
    private static final String TAG = "ResponsesRepository";


    public static ResponsesRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ResponsesRepository(context);
        }
        return instance;
    }

    private ResponsesRepository(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
    }


    @NonNull
    public LiveData<Resource<List<User>>> getUserLiveData() {
        FirebaseQueryLiveData mLiveData = new FirebaseQueryLiveData(mDatabase);
        return Transformations.map(mLiveData, new ResponsesRepository.DeserializerUserList());


    }

    private class DeserializerUserList implements Function<DataSnapshot, Resource<List<User>>> {

        @Override
        public Resource<List<User>> apply(DataSnapshot dataSnapshot) {
            uList.clear();
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                User user = snap.getValue(User.class);
                uList.add(user);
            }

            return Resource.success(uList);
            //TODO: Define Resource.error for errror case or unable to fetch
        }
    }


}

