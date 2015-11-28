package space.nthompson.ruserpg.Activities.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.twitter.sdk.android.Twitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;


import space.nthompson.ruserpg.R;

public class Dashboard extends AppCompatActivity{

    Context context = this;
    private ImageButton workoutBtn;
    private ImageView profile_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //set toolbar as the acting action bar
        Toolbar actionToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        String photoUrl = intent.getStringExtra("photo");
        String userTwitterID = intent.getStringExtra("id");
        String userName = intent.getStringExtra("name");
        String userEmail = intent.getStringExtra("email");

        String[] values = new String[3];
        values[0] = userTwitterID;
        values[1] = userName;
        values[2] = userEmail;


        new UserApiProcess().execute(values);
        new BackgroundUIProcesses().execute(photoUrl);

        //Launch workout screen from 'add' button
        workoutBtn = (ImageButton) findViewById(R.id.workoutBtn);
        workoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, WorkoutActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_logout){
            logoutUser();
        }

        return super.onOptionsItemSelected(item);
    }

    //Function to close app rather than move down stack when back button is pressed from this activity
    @Override
    public void onBackPressed(){
        finish();
    }

    //Menu function to handle user logout from Twitter, stopping session and returning to login
    public boolean logoutUser(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                .setTitle("Logout?")
                .setMessage("Are you sure you want to logout?")
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Twitter.getSessionManager().clearActiveSession();
                        Twitter.logOut();
                        Intent intent = new Intent(Dashboard.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        dialog.create().show();
        return true;
    }

    private class UserApiProcess extends AsyncTask<String, Void, Void>{
        Integer totalScore;
        Integer strengthScore;
        Integer persistenceScore;
        Integer enduranceScore;
        Integer charismaScore;
        Integer luckScore;

        @Override
        protected Void doInBackground(String... strings) {
            ArrayList<String> responseArray = new ArrayList<String>();
            JSONObject obj;
            String user_id = (String) strings[0];
            String userName = (String) strings[1];
            String email = "null@null.com";
            if(strings[2] != null){
                email = (String) strings[2];
            }

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormEncodingBuilder()
                    .add("user_id", user_id)
                    .add("userName", userName)
                    .add("email", email)
                    .build();
            Request request = new Request.Builder()
                    .url("https://ruse-api.herokuapp.com/postuser")
                    .addHeader("Content-Type", "x-www-form-urlencoded")
                    .post(formBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                if(!response.isSuccessful()) throw new IOException("Unexpected code " + response);
              //  System.out.println(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String jsonData = response.body().string();
                JSONArray jsonArray = new JSONArray(jsonData);
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    totalScore = jsonObject.getInt("totalScore");
                    strengthScore = jsonObject.getInt("strength");
                    persistenceScore = jsonObject.getInt("persistence");
                    enduranceScore = jsonObject.getInt("endurance");
                    charismaScore = jsonObject.getInt("charisma");
                    luckScore = jsonObject.getInt("luck");

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void args){
            TextView txtStrength = (TextView) findViewById(R.id.str_num);
            txtStrength.setText(strengthScore.toString());

            TextView txtPers = (TextView) findViewById(R.id.ag_num);
            txtPers.setText(persistenceScore.toString());

            TextView txtEndur = (TextView) findViewById(R.id.end_num);
            txtEndur.setText(enduranceScore.toString());

            TextView txtChar= (TextView) findViewById(R.id.char_num);
            txtChar.setText(charismaScore.toString());

            TextView txtLuck = (TextView) findViewById(R.id.luck_num);
            txtLuck.setText(luckScore.toString());
        }

    }
    private class BackgroundUIProcesses extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            String src = params[0];
            try {
                Log.e("src", src);
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                Log.e("Bitmap", "returned");
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Exception", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap myBitmap){
            profile_photo = (ImageView) findViewById(R.id.imageView);
            profile_photo.setImageBitmap(myBitmap);

        }
    }
}