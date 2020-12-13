package com.example.agro_lens.Shop.product;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agro_lens.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.MyProductHolder> {

    Context context;
    List<ModelProduct> modelProducts;

    public AdapterProduct(Context context, List<ModelProduct> modelProducts) {
        this.context = context;
        this.modelProducts = modelProducts;
    }

    @NonNull
    @Override
    public MyProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.product_container,parent,false);

        return new MyProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductHolder holder, int position) {
ModelProduct modelProduct=modelProducts.get(position);
        String image=modelProduct.getProductImage();
        String name=modelProduct.getProductName();
        String offerpercent=modelProduct.getDiscountNote();
        String quantity=modelProduct.getProductQuantity();
        String discountrate=modelProduct.getDiscountPrice();
        String originalrate=modelProduct.getOriginalPrice();
        String discountavailable=modelProduct.getDiscountAvailable();
        String productId=modelProduct.getProductId();
        String treename=modelProduct.getTreename();


        holder.productname.setText(name);
        holder.productquantity.setText(quantity);
        holder.productdiscountrate.setText("₹"+discountrate);
        holder.productoriginalrate.setText("₹"+originalrate);
        holder.productofferpercentage.setText(offerpercent);
        if (discountavailable.equals("true")){
            holder.productdiscountrate.setVisibility(View.VISIBLE);
            holder.productofferpercentage.setVisibility(View.VISIBLE);
            holder.productoriginalrate.setPaintFlags(holder.productoriginalrate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }
        else {
            holder.productdiscountrate.setVisibility(View.GONE);
            holder.productofferpercentage.setVisibility(View.GONE);


        }

        try {
            Picasso.get().load(image).placeholder(R.drawable.agrologo).into(holder.productimage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
        catch (Exception e){

        }








    }


    @Override
    public int getItemCount() {
        return modelProducts.size();
    }

    public class MyProductHolder extends RecyclerView.ViewHolder{
        ImageView productimage,productcart,productfavourite;
        TextView productname,productofferpercentage,productquantity,
        productdiscountrate,productoriginalrate;
        RelativeLayout cartrelative;




    public MyProductHolder(@NonNull View itemView) {
        super(itemView);

        productdiscountrate=itemView.findViewById(R.id.productdiscountrate);
        productimage=itemView.findViewById(R.id.productimage);
        productname=itemView.findViewById(R.id.productname);
        productofferpercentage=itemView.findViewById(R.id.productofferpercentage);
        productquantity=itemView.findViewById(R.id.productquantity);
        productoriginalrate=itemView.findViewById(R.id.productoriginalrate);
        cartrelative=itemView.findViewById(R.id.cartrelative);

        productcart=itemView.findViewById(R.id.productaddtocart);
        productfavourite=itemView.findViewById(R.id.favouritebutton);

    }
}
}
