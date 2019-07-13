package elixer.com.bloodbank.ui.requests;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import elixer.com.bloodbank.R;
import elixer.com.bloodbank.adapters.RequestRecyclerAdapter;
import elixer.com.bloodbank.models.Request;
import elixer.com.bloodbank.util.Resource;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class RequestsFragment extends Fragment {

    private RequestsViewModel viewModel;
    private static final String TAG = "RequestsFragment";
    RecyclerView recyclerView;
    RequestRecyclerAdapter mAdapter;
    ProgressBar progressBar;
    List<Request> popo = new ArrayList<>();


    public static RequestsFragment newInstance() {
        return new RequestsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.requests_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(RequestsViewModel.class);
        subscribeObservers();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.requests_recycler_view);
        progressBar = view.findViewById(R.id.progressBar);
        initRecycler(view.getContext());

    }

    private void subscribeObservers() {
        viewModel.observeRequests().removeObservers(getViewLifecycleOwner());
        viewModel.observeRequests().observe(getViewLifecycleOwner(), new Observer<Resource<List<Request>>>() {
            @Override
            public void onChanged(Resource<List<Request>> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING: {
                            Log.d(TAG, "onChanged: REQUESTSFragment: LOADING...");
                            showProgressBar(true);
                            break;
                        }
                        case SUCCESS: {
                            //  adapter.setPosts(listResource.data);
                            showProgressBar(false);
//                            Log.d(TAG, "Request Frag onChanged: name " + listResource.data.get(0).getName());
                            mAdapter.setRequests(listResource.data);
                            break;
                        }

                        case ERROR: {
                            showProgressBar(false);
                            Log.d(TAG, "onChanged: REQsFragment: EROR... " + listResource.message);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void initRecycler(Context context) {

        mAdapter = new RequestRecyclerAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        DividerItemDecoration itemDecor = new DividerItemDecoration(context, HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    public void showProgressBar(boolean visible) {

        progressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }


}
