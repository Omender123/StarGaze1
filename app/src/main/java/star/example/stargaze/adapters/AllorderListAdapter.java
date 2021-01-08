package star.example.stargaze.adapters;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import star.example.stargaze.R;
import star.example.stargaze.models.AllOrderModelData;
import star.example.stargaze.models.response.OrderHistoryResp;
import star.example.stargaze.utils.AppUtils;
import star.example.stargaze.utils.Constants;

public class AllorderListAdapter extends RecyclerView.Adapter<AllorderListAdapter.EventViewHolder> implements Filterable {
    private List<AllOrderModelData> data;
    private Context context;
    List<AllOrderModelData> mDataFiltered;
    public AllorderListAdapter(List<AllOrderModelData> data, Context context) {
        this.data = data;
        this.context = context;
        this.mDataFiltered = data;
    }

    public AllorderListAdapter() {

    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_row, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        try {

            Spanned details = Html.fromHtml("Price : " + "<b>" +  mDataFiltered.get(position).getPrice()
                    + "</b> <br/> " +
                    "Status : " + mDataFiltered.get(position).getStatus() + "<br/>" +
                    "Order Number : " + mDataFiltered.get(position).getOrder_Number() + "<br/>" + "Payment : " + mDataFiltered.get(position).getPayment()

            );

            holder.order_details.setText(details);


            //   holder.tv_user_name.setText(data.get(position).getUser().getName());
            holder.event_name.setText("Event : " + mDataFiltered.get(position).getEventName());
            holder.event_time.setText(AppUtils.getDate(mDataFiltered.get(position).getStartDate()));
            holder.tv_artist_name.setText("Artist : " + mDataFiltered.get(position).getArtistName());



                String image = mDataFiltered.get(position).getImageurl();
               Picasso.get().load(Constants.IMG_URL +image).into(holder.img_thumbnail);


        } catch (Exception e) {

            }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String Key = charSequence.toString();
                if (Key.isEmpty()) {
                    mDataFiltered = data;
                }
                else {
                    List<AllOrderModelData> lstFiltered = new ArrayList<>();
                    for (AllOrderModelData row : data) {

                        if (row.getArtistName().toLowerCase().contains(Key.toLowerCase()) || row.getEventName().toLowerCase().contains(Key.toLowerCase())){

                            lstFiltered.add(row);
                        }

                    }

                    mDataFiltered = lstFiltered;

                }


                FilterResults filterResults = new FilterResults();
                filterResults.values= mDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDataFiltered = (List<AllOrderModelData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tv_user_name, event_name, tv_artist_name, event_time, order_details, event_presents;
        ImageView img_thumbnail;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            event_name = itemView.findViewById(R.id.event_name);
            event_time = itemView.findViewById(R.id.event_time);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            tv_artist_name = itemView.findViewById(R.id.tv_artist_name);
//            event_presents = itemView.findViewById(R.id.event_presents);
            order_details = itemView.findViewById(R.id.order_details);
            img_thumbnail = itemView.findViewById(R.id.img_thumbnail);
        }
    }
}
