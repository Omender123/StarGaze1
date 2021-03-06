package star.example.stargaze.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import star.example.stargaze.R;
import star.example.stargaze.activities.MainActivity;
import star.example.stargaze.models.SearchModalData;
import star.example.stargaze.models.response.EventResp;
import star.example.stargaze.utils.Constants;

import java.util.List;

public class SearchEventAdapter extends RecyclerView.Adapter<SearchEventAdapter.EventViewHolder> {
    private List<SearchModalData>data;
    private Context context;
   // private OnEventItemListener listener;

    public SearchEventAdapter(List<SearchModalData> data, Context context/* OnEventItemListener listener*/) {
        this.data = data;
        this.context = context;
     //   this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_recycler_row,parent,false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        String name = data.get(position).getArtistName();
        holder.event_name.setText(name);

        String image = data.get(position).getImageUrl();
        Picasso.get().load(Constants.IMG_URL +image).into(holder.img_thumbnail);

//       holder.event_price.setText("Prices From : Rs "+data.get(position).getPrice());
//        holder.img_thumbnail.setImageResource(R.drawable.concert3);




        /*if (data.get(position).getImages() !=null && data.get(position).getImages().size() !=0){
            Glide.with(context).load(Constants.IMG_URL + "" + data.get(position).getImages().get(0).getUrl()).into(holder.img_thumbnail);
            }else {
            holder.img_thumbnail.setImageResource(R.drawable.concert3);

        }*/

      holder.img_thumbnail.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              context.startActivity(new Intent(context, MainActivity.class));

          }
      });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
      TextView event_name,event_price;
      ImageView img_thumbnail;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            event_name = itemView.findViewById(R.id.event_name);
//            event_price = itemView.findViewById(R.id.event_price);
            img_thumbnail = itemView.findViewById(R.id.img_thumbnail);
        }
    }

   /* public interface OnEventItemListener{
        void onEventItemClick(List<SearchModalData> data, int position);
    }*/

}
