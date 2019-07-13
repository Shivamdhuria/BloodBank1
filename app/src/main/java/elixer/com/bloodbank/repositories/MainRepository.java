package elixer.com.bloodbank.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import elixer.com.bloodbank.models.User;
import elixer.com.bloodbank.util.FirebaseQueryLiveData;
import elixer.com.bloodbank.util.Resource;

public class MainRepository {

    private static final String TAG = "Main Profile Repository";

    private FirebaseUser currentFirebaseUser;
    private static MainRepository instance;
    private DatabaseReference mDatabase;

    public MainRepository() {


    }

    public static MainRepository getInstance() {
        if (instance == null) {
            instance = new MainRepository();
        }
        return instance;
    }

    @NonNull
    public LiveData<Resource<User>> getUserFromDatabase() {
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentFirebaseUser.getUid());
        FirebaseQueryLiveData mLiveData = new FirebaseQueryLiveData(mDatabase);
        return Transformations.map(mLiveData, new DeserializerUser());


    }

    private class DeserializerUser implements Function<DataSnapshot, Resource<User>> {

        @Override
        public Resource<User> apply(DataSnapshot dataSnapshot) {
            Resource res = null;

            if (dataSnapshot.getValue(User.class) != null) {
                res = Resource.success(dataSnapshot.getValue(User.class));
            } else {
                res = Resource.error("Not Found", null);
            }

            return res;
        }
    }
}
