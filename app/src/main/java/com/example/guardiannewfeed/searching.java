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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class searching extends AppCompatActivity {

    private EditText tags;
    private ImageView search;
    TextView noIntenet;
    private RequestQueue mQueue;
    ListView listView_search;
    String TAG = "main";
    private final ArrayList<articles> articlesList = new ArrayList<articles>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        tags=findViewById(R.id.tags);
        search=findViewById(R.id.button);
        noIntenet=findViewById(R.id.noIntenet);
        progressBar=findViewById(R.id.progress_circular_search);
        listView_search =findViewById(R.id.listView_search);

        listView_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                articles currentartile=(articles)parent.getItemAtPosition(position);
                Uri uri=Uri.parse(currentartile.getUrl());
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }
        });


    }


    public void search(View view){
        if(!articlesList.isEmpty()){
            articlesList.clear();
            mian_list_adapter mian_list_adapter = new mian_list_adapter(this,articlesList);
            listView_search.setAdapter(mian_list_adapter);
        }

        if(netWorkIsConnected()&&!String.valueOf(tags.getText()).matches("")){
            progressBar=findViewById(R.id.progress_circular_search);
            progressBar.setVisibility(View.VISIBLE);
            String tag=String.valueOf(tags.getText());
            mQueue= Volley.newRequestQueue(this);
            jsonReqest(tag);
        }
        else if(String.valueOf(tags.getText()).matches("")){
            Toast.makeText(this, "You didn't write any thing please enter any tag", Toast.LENGTH_SHORT).show();
        }
        else{
            LinearLayout linearLayout= findViewById(R.id.wholeLayout);
            linearLayout.setVisibility(View.GONE);
            noIntenet.setVisibility(View.VISIBLE);
        }


    }

    private void jsonReqest(String tag){
        //https://content.guardianapis.com/search?q=trump&api-key=test
        StringBuilder builder=new StringBuilder();
        builder.append("https://content.guardianapis.com/search?q=");
        builder.append(tag);
        builder.append("&api-key=c0981cb6-911f-4899-9e92-6441847017e7");
        String url=String.valueOf(builder);

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

        progressBar.setVisibility(View.GONE);
        listView_search.setVisibility(View.VISIBLE);
        mian_list_adapter mian_list_adapter= new mian_list_adapter(this,articlesList);
        listView_search.setAdapter(mian_list_adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.search,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.back:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean netWorkIsConnected(){
        ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() !=null && cm.getActiveNetworkInfo().isConnected();

    }
}
