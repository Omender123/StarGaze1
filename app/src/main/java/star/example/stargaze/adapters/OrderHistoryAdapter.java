package star.example.stargaze.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import star.example.stargaze.R;
import star.example.stargaze.models.response.EventResp;
import star.example.stargaze.models.response.OrderHistoryResp;
import star.example.stargaze.utils.AppUtils;
import star.example.stargaze.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.EventViewHolder> implements Filterable {
    private List<OrderHistoryResp> data;
    private List<OrderHistoryResp> datafilter;
    private Context context;
    private OnOrderItemListener listener;

    public OrderHistoryAdapter(List<OrderHistoryResp> data, Context context, OnOrderItemListener listener) {
        this.data = data;
        this.datafilter = data;
        this.context = context;
        this.listener = listener;
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

                Spanned details = Html.fromHtml("Price : " + "<b>" + (double) datafilter.get(position).getEvent().getPrice()
                        + "</b> <br/> " +
                        "Status : " + data.get(position).getStatusDetails().get(0).getStatus() + "<br/>" +
                        "Order Number : " + data.get(position).getOrderNumber() + "<br/>" + "Payment : " + data.get(position).getPaymentStatus()

                );

                holder.order_details.setText(details);


                //   holder.tv_user_name.setText(data.get(position).getUser().getName());
                holder.event_name.setText("Event : " + data.get(position).getEvent().getName());
                holder.event_time.setText(AppUtils.getDate(data.get(position).getEvent().getStartDate()));
                holder.tv_artist_name.setText("Artist : " + data.get(position).getEvent().getArtist());
                //       holder.event_presents.setText(data.get(position).);

               // holder.img_thumbnail.setImageResource(R.drawable.concert);



           //     String image = event.getEvents().get(position).getImages().get(0).getUrl();
              // Picasso.get().load(Constants.IMG_URL + "").into(holder.img_thumbnail);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onOrderItemClick();
                    }
                });

        } catch (Exception e) {
            //  Toast.makeText(context, "No more order history available", Toast.LENGTH_SHORT).show();
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
            protected FilterResults performFiltering(CharSequence constraint) {
                String character = constraint.toString();
                if (character.isEmpty()) {
                    datafilter = data;
                } else {
                    List<OrderHistoryResp> filterlist = new ArrayList<>();
                    for (OrderHistoryResp row : data) {
                        if (row.getEvent().getName().toLowerCase().contains(character.toLowerCase()))
                            ;
                        {
                            filterlist.add(row);
                        }
                    }
                    datafilter = data;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = datafilter;


                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                datafilter=(ArrayList<OrderHistoryResp>) results.values;
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

    public interface OnOrderItemListener {
        void onOrderItemClick();
    }

}
