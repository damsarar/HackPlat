package com.industrialmaster.hackplat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class EventAddActivity extends AppCompatActivity {

    private static final String TAG = EventAddActivity.class.getSimpleName();

    private Button btnAddEvent;
    private EditText inputName;
    private EditText inputOrganizer;
    private EditText inputVenue;
    private EditText inputDate;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);

        inputName= (EditText) findViewById(R.id.name);
        inputOrganizer = (EditText) findViewById(R.id.organizer);
        inputVenue = (EditText) findViewById(R.id.venue);
        inputDate = (EditText) findViewById(R.id.dateAt);
        btnAddEvent = (Button) findViewById(R.id.btnAdd);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputName.getText().toString().trim();
                String organizer = inputOrganizer.getText().toString().trim();
                String venue = inputVenue.getText().toString().trim();
                String date_at = inputDate.getText().toString().trim();

                if (!name.isEmpty() && !organizer.isEmpty() && !venue.isEmpty() && !date_at.isEmpty()) {
                    addEvent(name, organizer, venue, date_at);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter event details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    public void addEvent(final String name, final String organizer, final String venue, final String date_at) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Creating ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.GET, "http://damsara.tk/hackplat/addEvent.php?name=" + name  + "&venue=" + venue + "&organizer=" + organizer + "&date_at=" + date_at, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Create Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(getApplicationContext(), "Event successfully created!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(EventAddActivity.this, EventActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in creation. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Creation Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
