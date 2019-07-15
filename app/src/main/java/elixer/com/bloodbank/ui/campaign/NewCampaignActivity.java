package elixer.com.bloodbank.ui.campaign;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import elixer.com.bloodbank.R;

public class NewCampaignActivity extends AppCompatActivity {

    private NewCampaignViewModel mNewCampaighViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_campaign);
        mNewCampaighViewModel = ViewModelProviders.of(this).get(NewCampaignViewModel.class);

        // Create new fragment and transaction
        Fragment newFragment = new QueryFragment();
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.fragment_container, newFragment);
            // Commit the transaction
            transaction.commit();
        }



    }

}
