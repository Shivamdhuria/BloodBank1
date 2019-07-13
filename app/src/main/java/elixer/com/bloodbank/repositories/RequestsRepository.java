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

import elixer.com.bloodbank.models.Request;
import elixer.com.bloodbank.models.User;
import elixer.com.bloodbank.util.FirebaseQueryLiveData;
import elixer.com.bloodbank.util.Resource;

public class RequestsRepository {

    private static RequestsRepository instance;
    private DatabaseReference mDatabase;
    private static FirebaseAuth mAuth;
    private List<Request> uList = new ArrayList<Request>();
    private static final String TAG = "RequestsRepository";


    public static RequestsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new RequestsRepository(context);
        }
        return instance;
    }

    private RequestsRepository(Context context) {


    }


    @NonNull
    public LiveData<Resource<List<Request>>> getUserLiveData() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("requests").child(mAuth.getUid());
        FirebaseQueryLiveData mLiveData = new FirebaseQueryLiveData(mDatabase);
        return Transformations.map(mLiveData, new DeserializerUserList());


    }

    private class DeserializerUserList implements Function<DataSnapshot, Resource<List<Request>>> {

        @Override
        public Resource<List<Request>> apply(DataSnapshot dataSnapshot) {
            uList.clear();
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                //Log.d("TAG","Peeru Value"+snap.getValue().toString());
                Request request = snap.getValue(Request.class);
                uList.add(request);
            }
            Log.e(TAG, "apply.....: "+ uList.size());
            return Resource.success(uList);
            //TODO: Define Resource.error for errror case or unable to fetch
        }
    }


}
