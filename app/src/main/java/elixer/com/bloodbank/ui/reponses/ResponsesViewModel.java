package elixer.com.bloodbank.ui.reponses;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import elixer.com.bloodbank.models.User;
import elixer.com.bloodbank.repositories.ResponsesRepository;
import elixer.com.bloodbank.util.Resource;

public class ResponsesViewModel extends AndroidViewModel {

    private static final String TAG = "RequestsViewModel";
    private MediatorLiveData<Resource<List<User>>> posts;
    private ResponsesRepository responsesRepository;

    public ResponsesViewModel(@NonNull Application application) {
        super(application);
        responsesRepository = ResponsesRepository.getInstance(application);
    }


    public LiveData<Resource<List<User>>> observePosts() {
        if (posts == null) {
            posts = new MediatorLiveData<>();
            posts.setValue(Resource.loading((List<User>) null));

            final LiveData<Resource<List<User>>> source = responsesRepository.getUserLiveData();

            posts.addSource(source, new Observer<Resource<List<User>>>() {
                @Override
                public void onChanged(Resource<List<User>> listResource) {
                    posts.setValue(listResource);
                    //Remove source for real time databse
//                   posts.removeSource(source);
                }
            });
        }
        return posts;
    }
}
