package elixer.com.bloodbank.ui.reponses;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import elixer.com.bloodbank.models.User;
import elixer.com.bloodbank.repositories.ResponseRepository;
import elixer.com.bloodbank.util.Resource;

public class ResponsesViewModel extends AndroidViewModel {

    private static final String TAG = "RequestsViewModel";
    private MediatorLiveData<Resource<List<User>>> responses;
    private ResponseRepository responseRepository;


    public ResponsesViewModel(@NonNull Application application) {
        super(application);
        responseRepository = ResponseRepository.getInstance(application);
    }


    public LiveData<Resource<List<User>>> observeResponses() {
        if (responses == null) {
            responses = new MediatorLiveData<>();
            responses.setValue(Resource.loading((List<User>) null));

            final LiveData<Resource<List<User>>> source = responseRepository.getUserLiveData();

            responses.addSource(source, new Observer<Resource<List<User>>>() {
                @Override
                public void onChanged(Resource<List<User>> listResource) {
                    responses.setValue(listResource);
//                    //Remove source for real time databse
//                   requests.removeSource(source);
                }
            });
        }
        return responses;
    }
}
