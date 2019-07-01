package elixer.com.bloodbank.ui.main;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private final LiveData<Boolean> isSignedIn;

    public MainViewModel() {
        this.isSignedIn = new LiveData<Boolean>() {
            @Override
            protected void onActive() {
                super.onActive();
                setValue(FirebaseAuth.getInstance().getCurrentUser() != null);
            }
        };
    }

    public LiveData<Boolean> getIsSignedIn() {
        return isSignedIn;
    }
}
