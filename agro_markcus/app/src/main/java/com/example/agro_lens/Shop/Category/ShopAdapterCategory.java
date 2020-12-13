package com.example.agro_lens.Shop.Category;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agro_lens.R;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ShopAdapterCategory extends RecyclerView.Adapter<ShopAdapterCategory.MyShopHolder> {
Context context;
List<ModelShopCategory> modelShopCategoryList;


    public ShopAdapterCategory(Context context, List<ModelShopCategory> modelShopCategoryList) {
        this.context = context;
        this.modelShopCategoryList = modelShopCategoryList;
    }

    @NonNull
    @Override
    public MyShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.shop_category_container,parent,false);
        return new MyShopHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyShopHolder holder, int position) {

        String imagelink=modelShopCategoryList.get(position).getLink();
        String name=modelShopCategoryList.get(position).getName();
        String treename=modelShopCategoryList.get(position).getTreename();

        holder.shopcaegoryname.setText(name);
        try {
            Picasso.get().load(imagelink).placeholder(R.drawable.loading).into(holder.shopcategoryimage);
        }
        catch (Exception e){

        }



    }

    @Override
    public int getItemCount() {
        return modelShopCategoryList.size();
    }

    class MyShopHolder extends RecyclerView.ViewHolder{

        ImageView shopcategoryimage;
        TextView shopcaegoryname;
        public MyShopHolder(@NonNull View itemView) {
            super(itemView);

            shopcaegoryname=itemView.findViewById(R.id.shopcategoryheading);
            shopcategoryimage=itemView.findViewById(R.id.shopcategoryimage);
        }
    }
}
