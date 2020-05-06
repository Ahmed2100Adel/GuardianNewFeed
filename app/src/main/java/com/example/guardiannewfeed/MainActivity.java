package com.example.guardiannewfeed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private final ArrayList<articles> articlesList = new ArrayList<articles>();
    private String TAG="main";
    ListView listView;
    ProgressBar progressBar;
    TextView noIntenet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(networkIsConnected()){
            listView=findViewById(R.id.listView_main);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    articles currentArticle=(articles) parent.getItemAtPosition(position);
                    String url=currentArticle.getUrl();
                    Uri uri= Uri.parse(url);
                    Intent i = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(i);
                }
            });

            mQueue= Volley.newRequestQueue(this);
            jsonRequest();
        }else{
            progressBar=findViewById(R.id.progress_circular);
            progressBar.setVisibility(View.GONE);
            noIntenet=findViewById(R.id.noIntenet);
            noIntenet.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.actionbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                Intent i = new Intent(MainActivity.this,searching.class);
                startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

    private void jsonRequest(){

        String url="https://content.guardianapis.com/search?q=general&api-key=c0981cb6-911f-4899-9e92-6441847017e7";
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v(TAG,"onResponse");
                fillResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v(TAG,"onErrorResponse");

            }
        });
        mQueue.add(jsonObjectRequest);

    }


    private void fillResponse(JSONObject Response){

        try {
            JSONObject root= Response.optJSONObject("response");
            JSONArray jsonArray=root.getJSONArray("results");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObjectCurrent=jsonArray.getJSONObject(i);
                String id=jsonObjectCurrent.optString("id");
                String webTitle=jsonObjectCurrent.optString("webTitle");
                String type=jsonObjectCurrent.optString("type");
                String sectionName=jsonObjectCurrent.optString("sectionName");
                String webUrl=jsonObjectCurrent.optString("webUrl");
                articlesList.add(new articles(id,webTitle,type,sectionName,webUrl));

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressBar=findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        mian_list_adapter mian_list_adapter= new mian_list_adapter(this,articlesList);
        listView.setAdapter(mian_list_adapter);
    }

    private  boolean networkIsConnected(){

        ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() !=null&& cm.getActiveNetworkInfo().isConnected();
    }
}

