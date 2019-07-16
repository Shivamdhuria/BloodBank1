package elixer.com.bloodbank.ui.campaign;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import java.util.List;

import elixer.com.bloodbank.models.Request;
import elixer.com.bloodbank.repositories.CampaignRepository;
import elixer.com.bloodbank.util.Resource;

public class NewCampaignViewModel extends AndroidViewModel {

    private Request request;
    private int bloodGroupIndex;
    private double latitude;
    private double longitude;
    private int radius;
    private CampaignRepository campaignRepository;
    private MediatorLiveData<Resource<List<String>>> donors = new MediatorLiveData<>();
    private MediatorLiveData<Boolean> isRequestSuccessful = new MediatorLiveData<>();


    private static final String TAG = "NewCampaignViewModel";
    public NewCampaignViewModel(@NonNull Application application) {
        super(application);
        request = new Request();
        campaignRepository = CampaignRepository.getInstance(application);
        bloodGroupIndex = -1;
        radius = 20;

    }

    //For Query Fragment
    public int getBloodGroupIndex() {
        return bloodGroupIndex;
    }
    public void setBloodGroupIndex(int bloodGroupIndex) {
        this.bloodGroupIndex = bloodGroupIndex;
    }
    public void setRequest(Request request) {

        this.request = request;
    }
    public Request getRequest() {
        return request;
    }


    //For Donor List Fragment

    public void fetchDonors() {
        //Set up Loading
        donors.setValue(Resource.loading((List<String>) null));

        final LiveData<Resource<List<String>>> source = Transformations.map(campaignRepository.SearchForDonorsByLocation
                (request.getBloodRequired(), latitude, longitude, radius), new Function<List<String>, Resource<List<String>>>() {
            @Override
            public Resource<List<String>> apply(List<String> input) {
                if (input.size() == 1 && input.get(0).equals("-1")) {

                    return Resource.error("Geo Key Failed", input);

                } else {
                    return Resource.success(input);
                }
            }
        });

        donors.addSource(source, new Observer<Resource<List<String>>>() {
            @Override
            public void onChanged(Resource<List<String>> listResource) {
                donors.setValue(listResource);
//                donors.removeSource(source);
            }
        });

    }

    public LiveData<Resource<List<String>>> observeDonors() {
        return donors;
    }


    public MediatorLiveData<Boolean> observeIssRequestSuccessful() {
        return isRequestSuccessful;
    }

    public void setIsRequestSuccessful(MediatorLiveData<Boolean> isRequestSuccessful) {
        this.isRequestSuccessful = isRequestSuccessful;
    }

    public void sendRequestToDonors() {
        if (request != null) {
            Log.e(TAG, "sendRequestToDonors: " );
            isRequestSuccessful.setValue(false);

            final LiveData<Boolean> source = campaignRepository.sendRequests(request);

            isRequestSuccessful.addSource(source, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean val) {
                    isRequestSuccessful.setValue(val);
                    isRequestSuccessful.removeSource(source);
                }
            });
        }




    }


    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}
