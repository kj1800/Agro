package com.example.agro_lens.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.agro_lens.R;
import com.example.agro_lens.Shop.Category.ModelShopCategory;
import com.example.agro_lens.Shop.Category.ShopAdapterCategory;
import com.example.agro_lens.Shop.product.AdapterProduct;
import com.example.agro_lens.Shop.product.ModelProduct;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CameraFragment extends Fragment {
    ShimmerFrameLayout shimmerFrameLayout;
    NestedScrollView nestedScrollView;
    RecyclerView shoprecycler,productrecycler;
    List<ModelShopCategory> modelShopCategoryList;
    ShopAdapterCategory shopAdapterCategory;
    KenBurnsView kenBurnsView;

    List<ModelProduct> modelProducts;
    AdapterProduct adapterProduct;
    WebView webView;



    ViewPager2 viewPager2;
    private Handler sliderhandler=new Handler();
    FloatingActionButton floatingActionButton;
    LinearLayout linearLayouts,orderhisory;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_camera, container, false);

        modelShopCategoryList=new ArrayList<>();
        shoprecycler=view.findViewById(R.id.shop_category_recycler);
        shoprecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        shoprecycler.setLayoutManager(linearLayoutManager);
webView=view.findViewById(R.id.webviewdata);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://agmarknet.gov.in/agnew/namticker.aspx");






        modelProducts=new ArrayList<>();
        productrecycler=view.findViewById(R.id.productlistcontainer);
        productrecycler.setHasFixedSize(true);
        StaggeredGridLayoutManager gridLayoutManager=new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        productrecycler.setLayoutManager(gridLayoutManager);
        getProduct();
        getRecyclerUser();







        return view;

    }

    private void getProduct() {
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("categories");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    modelShopCategoryList.clear();

                    for (DataSnapshot ds: snapshot.getChildren()){

                        ModelShopCategory modelFlipper=ds.getValue(ModelShopCategory.class);
                        modelShopCategoryList.add(modelFlipper);
                        shopAdapterCategory =new ShopAdapterCategory(getActivity(),modelShopCategoryList);
                        shoprecycler.setAdapter(shopAdapterCategory);




                    }
                }
                else {

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getRecyclerUser() {

        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("products");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    modelProducts.clear();

                    for (DataSnapshot ds: snapshot.getChildren()){

                        ModelProduct modelFlipper=ds.getValue(ModelProduct.class);
                        modelProducts.add(modelFlipper);
                        adapterProduct =new AdapterProduct(getActivity(),modelProducts);
                        productrecycler.setAdapter(adapterProduct);

                        //shimmerFrameLayout.stopShimmerAnimation();
                        //shimmerFrameLayout.setVisibility(View.GONE);
                        //nestedScrollView.setVisibility(View.VISIBLE);


                    }

                }
                else {

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


}