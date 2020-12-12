package com.example.agro_lens.CropHistory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agro_lens.R;
import com.example.agro_lens.cropComplete;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.MyHolder> implements Filterable {
    Context context;
    List<Modelhistory> userlist;
    List<Modelhistory> userlistfull;

    public AdapterHistory(Context context, List<Modelhistory> userlist) {
        this.context = context;
        this.userlist = userlist;
        userlistfull=new ArrayList<>(userlist);
    }

    @NonNull
    @Override
    public AdapterHistory.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_history,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterHistory.MyHolder holder, int position) {
        String cropImage=userlist.get(position).getImage();
        String crop_Name=userlist.get(position).getCropname();
        String crop_lat=userlist.get(position).getLatitude();
        String crop_lng=userlist.get(position).getLongitude();
        String crop_date=userlist.get(position).getDate();
        String crop_time=userlist.get(position).getTime();
        final String crop_cropid=userlist.get(position).getGeneratetext();

        holder.cropidHistory.setText(crop_cropid);
        holder.cropNamehistory.setText(crop_Name);
        holder.coordinatesHistory.setText(crop_lat+", "+crop_lng);
        holder.dateandtimeHistory.setText(crop_date+" "+crop_time);
        try {
            Picasso.get().load(cropImage).placeholder(R.drawable.logobg).into(holder.cropHistory);

        }
        catch (Exception e){

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(), cropComplete.class);
               i.putExtra("title",crop_cropid);
               view.getContext().startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return userlist.size();
    }

    @Override
    public Filter getFilter() {
        return examplefilter;
    }
    private Filter examplefilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Modelhistory> filteredList=new ArrayList<>();
            if(charSequence==null || charSequence.length() ==0){
                filteredList.addAll(userlistfull);
            }
            else {
                String filteredpattern=charSequence.toString().toLowerCase().trim();
                for(Modelhistory item: userlistfull){
                    if(item.getCropname().toLowerCase().contains(filteredpattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            userlist.clear();
            userlist.addAll((List)filterResults.values);
            notifyDataSetChanged();

        }
    };

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView cropHistory;
        TextView cropNamehistory,coordinatesHistory,dateandtimeHistory,cropidHistory;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cropHistory=itemView.findViewById(R.id.crop_image_history);
            cropNamehistory=itemView.findViewById(R.id.crop_name_history);
            coordinatesHistory=itemView.findViewById(R.id.crop_coordinates_history_txt);
            dateandtimeHistory=itemView.findViewById(R.id.crop_dateandtime_history_txt);
            cropidHistory=itemView.findViewById(R.id.crop_cropid_history_txt);
        }
    }

}
