package elixer.com.bloodbank.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import elixer.com.bloodbank.R;
import elixer.com.bloodbank.models.User;

public class ResponseRecyclerAdapter extends RecyclerView.Adapter<ResponseRecyclerAdapter.MyViewHolder> {

    private List<User> responseList;
    private OnCallButtonListener mOnCallButtonListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, place, level, bloodgroup;
        private ImageButton callButton;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            place = (TextView) view.findViewById(R.id.place);
            level = (TextView) view.findViewById(R.id.level);
            bloodgroup = (TextView) view.findViewById(R.id.bloodgroup);
            callButton = itemView.findViewById(R.id.callButton);

        }
    }


    public ResponseRecyclerAdapter(OnCallButtonListener mOnCallButtonListener) {
        this.mOnCallButtonListener = mOnCallButtonListener;
        responseList = new ArrayList<>();

    }

    public void setResponse(List<User> responseList) {
        this.responseList = responseList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.response_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.name.setText(responseList.get(position).getName());
        holder.place.setText(responseList.get(position).getCity());
        holder.bloodgroup.setText(responseList.get(position).getBloodGroup());
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnCallButtonListener.onCallButtonPressed(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (responseList != null) {
            return responseList.size();
        }
        return 0;
    }

    public String getSelectedResponsePhoneNumber(int position) {
        if (responseList != null) {
            if (responseList.size() > 0) {
                return responseList.get(position).getPhoneNumber();
            }
        }
        return null;
    }
}