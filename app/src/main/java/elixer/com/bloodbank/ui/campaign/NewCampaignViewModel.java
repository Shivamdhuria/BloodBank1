package elixer.com.bloodbank.ui.campaign;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewCampaignViewModel extends ViewModel {

    private final MutableLiveData message = new MutableLiveData();

    public void setMessage(String msg) {
        message.setValue(msg);
    }

    public MutableLiveData getMessage() {
        return message;
    }
}
