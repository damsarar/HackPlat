package com.industrialmaster.hackplat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.bumptech.glide.Glide;
import com.industrialmaster.hackplat.helper.SQLiteHandler;
import com.industrialmaster.hackplat.helper.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtName;
    private TextView txtEmail;
    private TextView txtEventCount;
    private TextView txtCreatedDate;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        txtEventCount = (TextView) findViewById(R.id.eventCount);
        txtCreatedDate = (TextView) findViewById(R.id.createdDate);

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
        String email = user.get("email");
//      String eventCount = user.get("eventcount");
        String createdDate = user.get("created_at");
        String img_url = user.get("img_url");

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = sdf1.parse(createdDate);
            createdDate = sdf2.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }



        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);
        txtCreatedDate.setText(createdDate);

        final ImageView imageView = findViewById(R.id.profile_image);
//        String url = "http://damsara.tk/hackplat/profile_images/"+ name + ".jpg";
//        String url = "http://damsara.tk/hackplat/profile_images/damsara.jpg";
//        Glide.with(this).load(url).into(imageView);

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



    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
