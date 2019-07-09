package elixer.com.bloodbank.ui.requests;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import elixer.com.bloodbank.models.User;
import elixer.com.bloodbank.repositories.RequestsRepository;
import elixer.com.bloodbank.util.Resource;

public class RequestsViewModel extends AndroidViewModel {

    private static final String TAG = "RequestsViewModel";
    private MediatorLiveData<Resource<List<User>>> posts;
    private RequestsRepository requestsRepository;

    public RequestsViewModel(@NonNull Application application) {
        super(application);
        requestsRepository = RequestsRepository.getInstance(application);
    }


    public LiveData<Resource<List<User>>> observePosts() {
        if (posts == null) {
            posts = new MediatorLiveData<>();
            posts.setValue(Resource.loading((List<User>) null));

            final LiveData<Resource<List<User>>> source = requestsRepository.getUserLiveData();

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
