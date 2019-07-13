package elixer.com.bloodbank.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import elixer.com.bloodbank.models.Profile;
import elixer.com.bloodbank.repositories.BuildProfileRepository;

public class BuildProfileViewModel extends ViewModel {

    private static final String TAG = "BuildProfileViewModel";

    private Profile profile;
    private BuildProfileRepository buildProfileRepository;
    private MediatorLiveData<Boolean> isSuccesfullySavedToDatabase;


    public BuildProfileViewModel() {
        profile = new Profile();
        buildProfileRepository = BuildProfileRepository.getInstance();
        isSuccesfullySavedToDatabase = new MediatorLiveData<>();
    }

    public void writeProfileToDatabase() {
        if (profile != null) {
            final LiveData<Boolean> source = buildProfileRepository.AddProfileAndLocation(profile);

            isSuccesfullySavedToDatabase.addSource(source, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    isSuccesfullySavedToDatabase.setValue(aBoolean);
                    isSuccesfullySavedToDatabase.removeSource(source);
                }
            });
        }
    }


    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public LiveData<Boolean> getIsSuccesfullySavedToDatabase() {
        return isSuccesfullySavedToDatabase;
    }
}
