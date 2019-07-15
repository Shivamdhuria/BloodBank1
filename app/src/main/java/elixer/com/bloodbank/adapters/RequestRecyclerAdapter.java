package elixer.com.bloodbank.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;

import elixer.com.bloodbank.R;
import elixer.com.bloodbank.models.Request;

public class RequestRecyclerAdapter extends RecyclerView.Adapter<RequestRecyclerAdapter.MyViewHolder> {

    private Map<String, Request> requestMap;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, place, level, bloodgroup;
        Switch aSwitch;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            place = (TextView) view.findViewById(R.id.place);
            level = (TextView) view.findViewById(R.id.level);
            bloodgroup = (TextView) view.findViewById(R.id.bloodgroup);
            aSwitch = view.findViewById(R.id.switch_button);

        }
    }


    public RequestRecyclerAdapter() {

    }

    public void setRequests(Map<String, Request> requestMap) {
        this.requestMap = requestMap;
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
        String key = (String) requestMap.keySet().toArray()[position];
        Request request = requestMap.get(key);
        holder.name.setText(request.getName());
        holder.place.setText(request.getPlaceOfRequest());
        holder.bloodgroup.setText(request.getBloodRequired());
        holder.aSwitch.setChecked(request.getStatus());

    }

    @Override
    public int getItemCount() {
        if (requestMap != null) {
            return requestMap.size();
        }
        return 0;
    }
}