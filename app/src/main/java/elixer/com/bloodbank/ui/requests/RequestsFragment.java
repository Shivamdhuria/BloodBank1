package elixer.com.bloodbank.ui.requests;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;

import elixer.com.bloodbank.R;
import elixer.com.bloodbank.adapters.OnResponseListener;
import elixer.com.bloodbank.adapters.RequestRecyclerAdapter;
import elixer.com.bloodbank.models.Request;
import elixer.com.bloodbank.util.Resource;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class RequestsFragment extends Fragment implements OnResponseListener {

    private RequestsViewModel viewModel;
    private static final String TAG = "RequestsFragment";
    private RecyclerView recyclerView;
    private RequestRecyclerAdapter mAdapter;
    private ProgressBar progressBar;
    private TextView textviewEmpty;



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
        textviewEmpty = view.findViewById(R.id.textViewEmpty);
        initRecycler(view.getContext());

    }

    private void subscribeObservers() {
        viewModel.observeRequests().removeObservers(getViewLifecycleOwner());
        viewModel.observeRequests().observe(getViewLifecycleOwner(), new Observer<Resource<Map<String,Request>>>() {
            @Override
            public void onChanged(Resource<Map<String,Request>> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING: {
                            Log.d(TAG, "onChanged: REQUESTSFragment: LOADING...");
                            textviewEmpty.setVisibility(View.INVISIBLE);
                            showProgressBar(true);
                            break;
                        }
                        case SUCCESS: {
                            //  adapter.setPosts(listResource.data);
                            showProgressBar(false);
                            Log.d(TAG, "Request Frag onChanged: name " + listResource.data.size());
                            mAdapter.setRequests(listResource.data);
                            //Hide TextView Or  Recycler
                            checkIfEmpty(listResource.data);
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

    private void checkIfEmpty(Map<String, Request> data) {
        if (data.size() == 0) {
            textviewEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);

        } else {
            textviewEmpty.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void initRecycler(Context context) {

        mAdapter = new RequestRecyclerAdapter(this);
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


    @Override
    public void onSwitchFlippedOn(int position) {
        Log.d(TAG, "onSwitchFlippedOn: " + mAdapter.getSelectedRequestsKey(position));
        viewModel.sendResponseAndDeleteRequest(mAdapter.getSelectedRequestsKey(position));

    }
}
