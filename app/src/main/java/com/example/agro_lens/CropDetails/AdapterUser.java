package com.example.agro_lens.CropDetails;

import android.content.Context;
import android.content.Intent;
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

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.agro_lens.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.MyHolder> implements Filterable {
    Context context;
    List<Modeluser> userlist;
    List<Modeluser> userlistfull;

    public AdapterUser(Context context, List<Modeluser> userlist) {
        this.context = context;
        this.userlist = userlist;
        userlistfull=new ArrayList<>(userlist);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_users,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        String userImage=userlist.get(position).getImageurl();
        final String userName=userlist.get(position).getCropname();
        final String userEmail=userlist.get(position).getBasic();

        holder.mNametv.setText(userName);
        holder.mEmailtv.setText(userEmail);
        try {
            Picasso.get().load(userImage).placeholder(R.drawable.agrologo).into(holder.mAvatartv);

        }
        catch (Exception e){

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(),crop_complete_details.class);
                i.putExtra("title",userName);
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
            List<Modeluser> filteredList=new ArrayList<>();
            if(charSequence==null || charSequence.length() ==0){
                filteredList.addAll(userlistfull);
            }
            else {
                String filteredpattern=charSequence.toString().toLowerCase().trim();
                for(Modeluser item: userlistfull){
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
        ImageView mAvatartv;
        TextView mNametv,mEmailtv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mAvatartv=itemView.findViewById(R.id.avatartv);
            mNametv=itemView.findViewById(R.id.nametv);
            mEmailtv=itemView.findViewById(R.id.mailtv);
        }
    }

}
