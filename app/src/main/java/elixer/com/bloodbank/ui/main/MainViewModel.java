package elixer.com.bloodbank.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import elixer.com.bloodbank.models.User;
import elixer.com.bloodbank.repositories.MainRepository;
import elixer.com.bloodbank.util.Resource;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Boolean> isSignedIn;
    private MediatorLiveData<Resource<User>> user;

    MainRepository mainRepository;
    private static final String TAG = "MainViewModel";

    public MainViewModel() {
        isSignedIn = new MutableLiveData<Boolean>();
        isSignedIn.setValue(FirebaseAuth.getInstance().getCurrentUser() != null);
        user = new MediatorLiveData<>();
        mainRepository = MainRepository.getInstance();
    }

    public LiveData<Boolean> getIsSignedIn()  {
        return isSignedIn;
    }

    public void setIsSignOut() {

        FirebaseAuth.getInstance().signOut();
        isSignedIn.setValue(FirebaseAuth.getInstance().getCurrentUser() != null);

    }

    public MediatorLiveData<Resource<User>> getUser() {
        return user;
    }


    public void fetchUserDetailsFromDatabase() {

        user.setValue(Resource.loading((User) null));

        final LiveData<Resource<User>> source = mainRepository.getUserFromDatabase();

        user.addSource(source, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> userResource) {
                user.setValue(userResource);
                user.removeSource(source);
            }
        });
    }

}
