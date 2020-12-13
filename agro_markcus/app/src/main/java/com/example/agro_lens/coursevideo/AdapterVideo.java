package com.example.agro_lens.coursevideo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agro_lens.R;

import java.util.List;

public class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.MyViewHolder> {
Context context;
List<ModelVideo> modelVideos;

    public AdapterVideo(Context context, List<ModelVideo> modelVideos) {
        this.context = context;
        this.modelVideos = modelVideos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.coursewebview,parent,false);
        return new AdapterVideo.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
String link=modelVideos.get(position).getLink();

holder.webView.loadUrl(link);
    }

    @Override
    public int getItemCount() {
        return modelVideos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
WebView webView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            webView= itemView.findViewById(R.id.courseweb);

            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setJavaScriptEnabled(true);
        }
    }
}
