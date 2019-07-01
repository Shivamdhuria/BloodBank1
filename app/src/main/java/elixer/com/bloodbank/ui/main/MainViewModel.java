package elixer.com.bloodbank.ui.main;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Boolean> isSignedIn;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "MainViewModel";

    public MainViewModel() {
        isSignedIn = new MutableLiveData<Boolean>();
        isSignedIn.setValue(FirebaseAuth.getInstance().getCurrentUser() != null);
    }

    public LiveData<Boolean> getIsSignedIn() {
        return isSignedIn;
    }

    public void setIsSignOut() {

        FirebaseAuth.getInstance().signOut();
        isSignedIn.setValue(FirebaseAuth.getInstance().getCurrentUser() != null);

    }
}
