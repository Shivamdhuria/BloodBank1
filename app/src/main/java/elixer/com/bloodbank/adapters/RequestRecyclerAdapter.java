package elixer.com.bloodbank.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import elixer.com.bloodbank.R;
import elixer.com.bloodbank.models.Request;

public class RequestRecyclerAdapter extends RecyclerView.Adapter<RequestRecyclerAdapter.MyViewHolder> {

    private List<Request> requestList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, place, level, bloodgroup;
        Switch aSwitch;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.TextView_Title);
            place = (TextView) view.findViewById(R.id.place);
            level = (TextView) view.findViewById(R.id.level);
            bloodgroup = (TextView) view.findViewById(R.id.bloodgroup);
            aSwitch = view.findViewById(R.id.switch_button);

        }
    }


    public RequestRecyclerAdapter() {

    }

    public void setRequests(List<Request> requestList) {
        this.requestList = requestList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(requestList.get(position).getName());
        holder.place.setText(requestList.get(position).getPlaceOfRequest());
        holder.bloodgroup.setText(requestList.get(position).getBloodRequired());
        holder.aSwitch.setChecked(requestList.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        if (requestList != null) {
            return requestList.size();
        }
        return 0;
    }
}