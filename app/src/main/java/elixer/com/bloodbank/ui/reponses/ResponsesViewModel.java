package elixer.com.bloodbank.ui.reponses;

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

public class ResponsesViewModel extends AndroidViewModel {

    private static final String TAG = "ResponseViewModel";
    private MediatorLiveData<Resource<List<Request>>> responses;
    private RequestsRepository responseRepository;


    public ResponsesViewModel(@NonNull Application application) {
        super(application);
        responseRepository = RequestsRepository.getInstance(application);
    }


    public LiveData<Resource<List<Request>>> observeResponses() {
        if (responses == null) {
            responses = new MediatorLiveData<>();
            responses.setValue(Resource.loading((List<Request>) null));

            final LiveData<Resource<List<Request>>> source = responseRepository.getUserLiveData();

            responses.addSource(source, new Observer<Resource<List<Request>>>() {
                @Override
                public void onChanged(Resource<List<Request>> listResource) {
                    responses.setValue(listResource);
//                    //Remove source for real time databse
//                   requests.removeSource(source);
                }
            });
        }
        return responses;
    }
}
