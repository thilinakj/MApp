package com.thilinas.mapp.activities;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.thilinas.mapp.R;
import com.thilinas.mapp.adapters.PhotoListAdapter;
import com.thilinas.mapp.adapters.recycleViewListeners.EndlessRecyclerViewScrollListener;
import com.thilinas.mapp.adapters.recycleViewListeners.RecyclerItemClickListener;
import com.thilinas.mapp.custom.WaveProgressDialog;
import com.thilinas.mapp.models.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoViewActivity extends AppCompatActivity{

    private List<Photo> gameList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PhotoListAdapter mAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    private int start=0;
    private final int limit=5;
    private static int total;
    private final String base_url = "http://192.99.54.24/ukku/api.php?";

    private  WaveProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        prepareList();
        send();
    }

    private void prepareList(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new PhotoListAdapter(gameList,getApplicationContext());
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
                if(start<=total){
                    start+=limit;
                    send();
                }
            }
        };
        mAdapter.setOnBluetoothDeviceClickedListener(new PhotoListAdapter.OnSetDesktopClickedListener() {
            @Override
            public void onButtonClicked(String url) {
                setDesktop(url);
            }
        });
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.setAdapter(mAdapter);
    }

    public String getUrl(){
        String url = base_url + "offset="+start+ "&quantity=" +limit;
        return  url;
    }

    public void send(){
        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> jsonParams = new HashMap<String, String>();
        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST,getUrl(),
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onResponceReceived(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        queue.add(postRequest);
    }

    public synchronized void onResponceReceived(JSONObject response){
        try {
            JSONObject pagination = response.getJSONObject("pagination");
            Log.i("XXXX",pagination.toString());
            total = pagination.getInt("total");
            JSONArray files = response.getJSONArray("files");
            JSONObject file;
            Photo photo;
            int size = files.length();
            if(size>0){
                for (int i=0;i<size;i++){
                    file = files.getJSONObject(i);
                    photo = new Photo();
                    photo.setUrl(file.getString("url"));
                    photo.setId(String.valueOf(i));
                    gameList.add(photo);
                }
                mAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){

        }
    }

    public void setDesktop(String url){
        Log.i("XXXX","setting");

        progressDialog = WaveProgressDialog.ctor(this);
        final WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());

        Glide.with(getApplicationContext())
                .load(url)
                .asBitmap()
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
