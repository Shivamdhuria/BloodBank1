package elixer.com.bloodbank.ui.reponses;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import elixer.com.bloodbank.R;
import elixer.com.bloodbank.models.User;
import elixer.com.bloodbank.ui.main.MainActivity;
import elixer.com.bloodbank.util.Resource;

public class ResponsesFragment extends Fragment {

    private ResponsesViewModel viewModel;
    private static final String TAG = "ResponsesFragment";

    private RecyclerView recyclerView;

    public static ResponsesFragment newInstance() {
        return new ResponsesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.responses_fragment, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ResponsesViewModel.class);
        subscribeObservers();


    }

    private void initRecycler() {


    }

    private void subscribeObservers() {
        viewModel.observePosts().removeObservers(getViewLifecycleOwner());
        viewModel.observePosts().observe(getViewLifecycleOwner(), new Observer<Resource<List<User>>>() {
            @Override
            public void onChanged(Resource<List<User>> listResource) {
                if (listResource != null) {
                    Log.e(TAG, "onChanged: ...........");


                    switch (listResource.status) {
                        case LOADING: {

                            Log.d(TAG, "onChanged: ResponseFragment: LOADING...");
                            break;
                        }

                        case SUCCESS: {
                            Log.d(TAG, "onChanged:ResponsFragment: got posts.");
                            //  adapter.setPosts(listResource.data);
                            Log.d(TAG, "onChanged: " + listResource.data.size());


                            break;
                        }

                        case ERROR: {
                            Log.d(TAG, "onChanged: PostsFragment: ERROR... " + listResource.message);
                            break;
                        }
                    }
                }
            }
        });
    }

}