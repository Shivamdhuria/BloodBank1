package elixer.com.bloodbank.ui.campaign;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;
import java.util.Objects;

import elixer.com.bloodbank.R;
import elixer.com.bloodbank.util.Resource;

public class DonorListFragment extends Fragment {

    private NewCampaignViewModel viewModel;
    private static final String TAG = "DonorListFragment";
    private ProgressBar progressBar;
    private TextView textView;
    private Button button;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donor_list, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBarDonor);
        button = view.findViewById(R.id.button_request);
        button.setEnabled(false);

        textView = view.findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.sendRequestToDonors();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(NewCampaignViewModel.class);
        subscribeObservers();
        viewModel.fetchDonors();
    }

    private void subscribeObservers() {
        viewModel.observeDonors().observe(getViewLifecycleOwner(), new Observer<Resource<List<String>>>() {
            @Override
            public void onChanged(Resource<List<String>> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {

                        case SUCCESS:
                            Log.d(TAG, "onChanged: SUCCESS " + listResource.data.size());
                            showProgressBar(false);
                            button.setEnabled(true);
                            setUpTextView(true, listResource.data.size() + " Donors Found");
                            break;
                        case LOADING:
                            Log.d(TAG, "onChanged: LOADING");
                            showProgressBar(true);
                            setUpTextView(false, "");
                            break;
                        case ERROR:
                            Log.d(TAG, "onChanged: ERROR");
                            showProgressBar(false);
                            setUpTextView(true, "Error ");

                    }
                }
            }
        });

        viewModel.observeIssRequestSuccessful().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(getContext(), "Request Successully", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
//                    Toast.makeText(getContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setUpTextView(boolean visibility, String message) {
        textView.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        textView.setText(message);
    }

    public void showProgressBar(boolean visibility) {
        progressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

}



