package elixer.com.bloodbank.ui.reponses;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.util.List;

import elixer.com.bloodbank.R;
import elixer.com.bloodbank.adapters.OnCallButtonListener;
import elixer.com.bloodbank.adapters.ResponseRecyclerAdapter;
import elixer.com.bloodbank.models.User;
import elixer.com.bloodbank.util.Resource;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class ResponsesFragment extends Fragment implements OnCallButtonListener {

    private ResponsesViewModel viewModel;
    private static final String TAG = "ResponseFragment";
    RecyclerView recyclerView;
    ResponseRecyclerAdapter mAdapter;
    ProgressBar progressBar;

    public static ResponsesFragment newInstance() {
        return new ResponsesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.responses_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ResponsesViewModel.class);
        subscribeObservers();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.response_recycler_view);
        progressBar = view.findViewById(R.id.progressBarRes);
        initRecycler(view.getContext());

    }

    private void subscribeObservers() {
        viewModel.observeResponses().removeObservers(getViewLifecycleOwner());
        viewModel.observeResponses().observe(getViewLifecycleOwner(), new Observer<Resource<List<User>>>() {
            @Override
            public void onChanged(Resource<List<User>> listResource) {
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
                            mAdapter.setResponse(listResource.data);
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

        mAdapter = new ResponseRecyclerAdapter(this);
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
    public void onCallButtonPressed(int position) {

        String phoneNumber = mAdapter.getSelectedResponsePhoneNumber(position);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
        startActivity(intent);
    }
}
