package com.industrialmaster.hackplat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.industrialmaster.hackplat.helper.SQLiteHandler;
import com.industrialmaster.hackplat.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventActivity extends AppCompatActivity {

    private static final String tag = MainActivity.class.getSimpleName();
    private static final String url = "https://raw.githubusercontent.com/iCodersLab/Custom-ListView-Using-Volley/master/richman.json";
    private List<Event> list = new ArrayList<Event>();
    private ListView listView;
    private EventListAdapter adapter;

    private TextView txtName;
    private Button btnLogout;
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        txtName = (TextView) findViewById(R.id.name);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String img_url = user.get("img_url");

        // Displaying the user details on the screen
        txtName.setText(name);

        final ImageView imageView = findViewById(R.id.profile_image);

        ImageRequest request= new ImageRequest(img_url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageResource(R.drawable.propic);
                    }
                });

        AppController.getInstance().addToRequestQueue(request);


        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EventActivity.this);
                builder.setTitle("Do you really want to log out?");
//                builder.setMessage("You are about to delete all records of database. Do you really want to proceed ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutUser();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();

            }
        });



        listView = (ListView) findViewById(R.id.eventList);
        adapter = new EventListAdapter(this, list);
        listView.setAdapter(adapter);



        JsonArrayRequest jsonreq = new JsonArrayRequest("http://damsara.tk/hackplat/eventList.php",
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {



                                JSONObject obj = response.getJSONObject(i);
                                Event dataSet = new Event();

                                dataSet.setName(obj.getString("name"));
                                dataSet.setOrganizer(obj.getString("organizer"));
                                dataSet.setVenue(obj.getString("venue"));
                                dataSet.setDate_at(obj.getString("date_at"));

                                dataSet.setDate_created(obj.getString("created_at"));
//                                Toast.makeText(EventActivity.this,"Here!",Toast.LENGTH_SHORT).show();
                                dataSet.setImgURL(obj.getString("img_url"));



                                list.add(dataSet);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EventActivity.this,"Error"+error,Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder add = new AlertDialog.Builder(EventActivity.this);
                        add.setMessage(error.getMessage()).setCancelable(true);
                        AlertDialog alert = add.create();
                        alert.setTitle("Error!!!");
                        alert.show();
                    }
                });


        AppController.getInstance().addToRequestQueue(jsonreq);



    }



    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(EventActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }



    @Override
    protected void onResume() {
        super.onResume();

//        final ListView lv=findViewById(R.id.eventList);
//
//        RequestQueue queue= Volley.newRequestQueue(this);
//        String url="http://damsara.tk/hackplat/eventList.php";
//
//        final List<String> list=new ArrayList<>();
//
//        JsonArrayRequest request1=new JsonArrayRequest(
//                Request.Method.GET,
//                url,
//                null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//
//                        for(int i=0;i<response.length();i++){
//                            try{
//                                JSONObject obj=response.getJSONObject(i);
//                                list.add(obj.getString("name"));
//
//                            }catch (Exception e){
//
//                            }
//                        }
//
//                        int layout=android.R.layout.simple_list_item_1;
//                        ArrayAdapter adapter=new ArrayAdapter(EventActivity.this,layout,list);
//                        lv.setAdapter(adapter);
//
//                    }
//                },
//
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(EventActivity.this,"Error"+error,Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//        );
//
//        queue.add(request1);

////////////////////////////////////

//        int layout=android.R.layout.simple_expandable_list_item_1;
//        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,layout,list);
//
//        ListView lv2=findViewById(R.id.eventList);
//        lv2.setAdapter(adapter);
//
//        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView tv=(TextView) view;
//                String note=tv.getText().toString();
//
//                String file=lv.get(position);
//
//                Intent intent=new Intent(NotesActivity.this,NoteAddActivity.class);
//
//                intent.putExtra("note",note);
//                intent.putExtra("file",file);
//
//                startActivity(intent);
//
//            }
//        });
    }
}
