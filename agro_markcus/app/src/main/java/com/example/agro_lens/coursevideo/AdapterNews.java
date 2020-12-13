package com.example.agro_lens.coursevideo;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agro_lens.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.ViewHolder> {

    Context context;
    List<ModelVideo> modelVideoList;

    public AdapterNews(Context context, List<ModelVideo> modelVideoList) {
        this.context = context;
        this.modelVideoList = modelVideoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.newscontainer,parent,false);
        return new AdapterNews.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String link=modelVideoList.get(position).getLink();

        try {
            Picasso.get().load(link).placeholder(R.drawable.agrologo).into(holder.imageView);
        }
        catch (Exception e){

        }

    }

    @Override
    public int getItemCount() {
        return modelVideoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.newscontainer);
        }
    }
}
