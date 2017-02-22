package com.thilinas.mapp.activities;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.thilinas.mapp.R;
import com.thilinas.mapp.adapters.PhotoListAdapter;
import com.thilinas.mapp.adapters.recycleViewListeners.EndlessRecyclerViewScrollListener;
import com.thilinas.mapp.adapters.recycleViewListeners.RecyclerItemClickListener;
import com.thilinas.mapp.custom.WaveProgressDialog;
import com.thilinas.mapp.models.Photo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFavouritesActivity extends AppCompatActivity {

    private List<Photo> photoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PhotoListAdapter mAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeRefreshLayout;

    private WaveProgressDialog progressDialog;
    private FavoriteItemsDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favourites);
        prepareList();
        datasource = new FavoriteItemsDataSource(this);
        datasource.open();
        photoList.clear();
        photoList.addAll(datasource.getMyFavItems());
        Log.i("YYYYY"," items"+datasource.getMyFavItems());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();

    }

    private void prepareList(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new PhotoListAdapter(photoList,getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        try {
                            // final Notification notification = itemList.get(position);
                        }catch (Exception e){

                        }
                    }
                    @Override
                    public void onLongClick(View view, int position) {  }
                })
        );
        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                /*if(start<=total){
                    start+=limit;
                    send();
                }*/
            }
        };
        mAdapter.setOnIconsClickedListener(new PhotoListAdapter.OnIconsClickedListener() {
            @Override
            public void onSetWallClicked(String url) {
                setDesktop(url);
            }

            @Override
            public void onItemLiked(Photo photo) {
                photo.setFav(true);
                datasource.insertItem(photo);
            }
        });
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.pink, R.color.indigo, R.color.lime);
        swipeRefreshLayout.setOnRefreshListener(new     SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                photoList.clear();
                photoList.addAll(datasource.getMyFavItems());
                Log.i("YYYYY"," items"+datasource.getMyFavItems());
                mAdapter.notifyDataSetChanged();
                if(swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }



    public void setDesktop(String url){
        Log.i("XXXX","setting");
        progressDialog = WaveProgressDialog.getInstance(this,"Setting up the wallpaper.\nPlease wait.");
        final WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        Glide.with(getApplicationContext())
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                          @Override
                          public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                              try {
                                  myWallpaperManager.setBitmap(resource);
                              } catch (IOException e) {
                                  e.printStackTrace();
                              }finally {
                                  if (progressDialog != null) progressDialog.dismiss();
                              }
                          }
                      }
                );
    }
}
