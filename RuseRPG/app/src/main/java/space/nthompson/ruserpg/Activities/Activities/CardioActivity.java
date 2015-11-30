package space.nthompson.ruserpg.Activities.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import space.nthompson.ruserpg.R;

public class CardioActivity extends AppCompatActivity {
    JSONObject jsonObject;
    JSONArray jsonArray;

    ArrayList<String> cardioList;
    ArrayList<Cardio> cardio;

    String cardioURL = "http://ruse-api.herokuapp.com/lookupcardio";
    String cardioPOSTURL = "http://ruse-api.herokuapp.com/postCardio";

    Button cardioSaveBtn;

    EditText cardioTime;
    String cardioID;

    Boolean changesMade = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardio);
        //set toolbar as the acting action bar
        Toolbar actionToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionToolbar);
        getSupportActionBar().setTitle("Add Cardio");

        Intent intent = getIntent();
        final String workoutID = intent.getStringExtra("workout_id");
        final String twitterID = intent.getStringExtra("twitterID");
        String workoutDateTime = intent.getStringExtra("dateTime");

        String[] values = new String[3];
        values[0] = workoutID;
        values[1] = twitterID;
        values[2] = workoutDateTime;

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        new parseStengthAPI().execute();

        //Launch workout screen from 'add' button
        cardioSaveBtn = (Button) findViewById(R.id.cardioSaveBtn);
        cardioSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardioTime = (EditText) findViewById(R.id.cardioTimeText);
                final String rep_total = cardioTime.toString();
                changesMade = true;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //code to do the HTTP request
                        OkHttpClient client = new OkHttpClient();
                        RequestBody formBody = new FormEncodingBuilder()
                                .add("duration_seconds", rep_total)
                                .add("fk_workout_id", workoutID)
                                .add("fk_workout_user_id", twitterID)
                                .add("fk_lookupcardio_id", cardioID)
                                .build();
                        System.out.println(cardioID);
                        Request request = new Request.Builder()
                                .url("http://ruse-api.herokuapp.com/postCardio")
                                .post(formBody)
                                .build();
                        Response response = null;
                        try {
                            response = client.newCall(request).execute();
                            if (!response.isSuccessful())
                                throw new IOException("Unexpected code " + response);
                            //  System.out.println(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(CardioActivity.this, WorkoutActivity.class);
                        intent.putExtra("changes", changesMade);
                        startActivity(intent);
                    }

                });
                thread.start();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise, menu);
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

        return super.onOptionsItemSelected(item);
    }

    private class parseStengthAPI extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            JSONObject obj;
            //locate Cardio class
            cardio = new ArrayList<Cardio>();
            //create array to populate spinner with results
            cardioList = new ArrayList<String>();
            jsonArray = JSONfunctions.getJSONfromURL(cardioURL);
            try {
                for (int i = 0; i < jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);

                    Cardio car = new Cardio();

                    car.setCardioName(jsonObject.optString("cardioName"));
                    car.setYouTubeURL(jsonObject.optString("youTubeURL"));
                    cardio.add(car);

                    cardioList.add(jsonObject.optString("cardioName"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void args){
            Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
            //spinner adapter
            mySpinner.setAdapter(new ArrayAdapter<String>(CardioActivity.this, android.R.layout.simple_spinner_dropdown_item, cardioList));
            //spinner on item click listener
            mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView txtName = (TextView) findViewById(R.id.name);
                    TextView txtURL = (TextView) findViewById(R.id.youtubeURL);
                    txtURL.setText(
                            Html.fromHtml(
                                    "<a href="+ cardio.get(i).getYouTubeURL() + ">Youtube URL</a> "));
                    txtURL.setMovementMethod(LinkMovementMethod.getInstance());
                    txtName.setText(cardio.get(i).getCardioName());
                    cardioID = String.valueOf(i);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

}
