package com.industrialmaster.hackplat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.industrialmaster.hackplat.helper.SQLiteHandler;
import com.industrialmaster.hackplat.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final ListView lv=findViewById(R.id.eventList);

        RequestQueue queue= Volley.newRequestQueue(this);
        String url="http://damsara.tk/hackplat/eventList.php";

        JsonArrayRequest request1=new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> list=new ArrayList<>();
                        for(int i=0;i<response.length();i++){
                            try{
                                JSONObject obj=response.getJSONObject(i);
                                list.add(obj.getString("name"));

                            }catch (Exception e){

                            }
                        }

                        int layout=android.R.layout.simple_list_item_1;
                        ArrayAdapter adapter=new ArrayAdapter(EventActivity.this,layout,list);
                        lv.setAdapter(adapter);

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EventActivity.this,"Error"+error,Toast.LENGTH_SHORT).show();
                    }
                }

        );

        queue.add(request1);
    }
}
