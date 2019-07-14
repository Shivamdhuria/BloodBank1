package elixer.com.bloodbank.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import elixer.com.bloodbank.models.User;
import elixer.com.bloodbank.repositories.BuildProfileRepository;

public class BuildProfileViewModel extends ViewModel {

    private static final String TAG = "BuildProfileViewModel";

    private User user;
    private BuildProfileRepository buildProfileRepository;
    private MediatorLiveData<Boolean> isSuccesfullySavedToDatabase;


    public BuildProfileViewModel() {
        buildProfileRepository = BuildProfileRepository.getInstance();
        isSuccesfullySavedToDatabase = new MediatorLiveData<>();
    }

    public void writeProfileToDatabase(final User user, double latitude, double longitude) {
        if (user != null) {
            final LiveData<Boolean> source = buildProfileRepository.AddUserAndLocation(user, latitude, longitude);

            isSuccesfullySavedToDatabase.addSource(source, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    isSuccesfullySavedToDatabase.setValue(aBoolean);
                    isSuccesfullySavedToDatabase.removeSource(source);
                }
            });
        }
    }



    public LiveData<Boolean> getIsSuccesfullySavedToDatabase() {
        return isSuccesfullySavedToDatabase;
    }


}
