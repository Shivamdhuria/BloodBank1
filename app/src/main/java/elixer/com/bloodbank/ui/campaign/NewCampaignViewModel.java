package elixer.com.bloodbank.ui.campaign;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import java.util.List;

import elixer.com.bloodbank.models.Request;
import elixer.com.bloodbank.repositories.DonorListRepository;
import elixer.com.bloodbank.util.Resource;

public class NewCampaignViewModel extends AndroidViewModel {

    private Request request;
    private int bloodGroupIndex;
    private double latitude;
    private double longitude;
    private int radius;
    private DonorListRepository donorListRepository;
    private MediatorLiveData<Resource<List<String>>> donors;


    private static final String TAG = "NewCampaignViewModel";
    public NewCampaignViewModel(@NonNull Application application) {
        super(application);
        request = new Request();
        donorListRepository = DonorListRepository.getInstance(application);
        bloodGroupIndex = -1;
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

    public LiveData<Resource<List<String>>> observeDonors() {
        if (donors == null) {
            donors = new MediatorLiveData<>();
        }
        //Set up Loading
        donors.setValue(Resource.loading((List<String>) null));

        final LiveData<Resource<List<String>>> source = Transformations.map(donorListRepository.getDonorLiveData(), new Function<List<String>, Resource<List<String>>>() {
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
                donors.removeSource(source);
            }
        });

        return donors;
    }


    public LiveData<Boolean> observeRequestsStatus() {
        return donorListRepository.getIsRequestSuccessful();
    }

    public void searchDonors() {
        Log.e(TAG, "searchDonors: ");

        Log.e(TAG, "searchingDonors:..... query not same ");
        donorListRepository.SearchForDonorsByLocation(request.getBloodRequired(), latitude, longitude, radius);

    }

    public void sendRequestToDonors() {
        if (request != null) {
            donorListRepository.sendRequests(request);
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
