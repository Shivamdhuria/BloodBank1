package elixer.com.bloodbank.ui.requests;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import elixer.com.bloodbank.models.Request;
import elixer.com.bloodbank.repositories.RequestsRepository;
import elixer.com.bloodbank.util.Resource;

public class RequestsViewModel extends AndroidViewModel {

    private static final String TAG = "RequestsViewModel";
    private MediatorLiveData<Resource<List<Request>>> requests;
    private RequestsRepository requestsRepository;

    public RequestsViewModel(@NonNull Application application) {
        super(application);
        requestsRepository = RequestsRepository.getInstance(application);
    }


    public LiveData<Resource<List<Request>>> observeRequests() {
        if (requests == null) {
            requests = new MediatorLiveData<>();
            requests.setValue(Resource.loading((List<Request>) null));

            final LiveData<Resource<List<Request>>> source = requestsRepository.getUserLiveData();

            requests.addSource(source, new Observer<Resource<List<Request>>>() {
                @Override
                public void onChanged(Resource<List<Request>> listResource) {
                    requests.setValue(listResource);
//                    //Remove source for real time databse
                   requests.removeSource(source);
                }
            });
        }
        return requests;
    }
}
