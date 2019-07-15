package elixer.com.bloodbank.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import elixer.com.bloodbank.R;
import elixer.com.bloodbank.models.Request;

public class ResponseRecyclerAdapter extends RecyclerView.Adapter<ResponseRecyclerAdapter.MyViewHolder> {

    private List<Request> responseList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, place, level, bloodgroup;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            place = (TextView) view.findViewById(R.id.place);
            level = (TextView) view.findViewById(R.id.level);
            bloodgroup = (TextView) view.findViewById(R.id.bloodgroup);

        }
    }


    public ResponseRecyclerAdapter() {

    }

    public void setResponse(List<Request> responseList) {
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(responseList.get(position).getName());
        holder.place.setText(responseList.get(position).getPlaceOfRequest());
        holder.bloodgroup.setText(responseList.get(position).getBloodRequired());

    }

    @Override
    public int getItemCount() {
        if (responseList != null) {
            return responseList.size();
        }
        return 0;
    }
}