package space.nthompson.ruserpg.Activities.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import space.nthompson.ruserpg.R;

public class WorkoutActivity extends AppCompatActivity {

    Context context = this;

    private Button exerciseBtn;
    private Button saveWorkoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        Intent intent = getIntent();
        final String workoutID = intent.getStringExtra("workout_id");
        final String twitterID = intent.getStringExtra("twitterID");
        final String workoutDateTime = intent.getStringExtra("dateTime");

        saveWorkoutBtn = (Button) findViewById(R.id.saveWorkoutButton);
        saveWorkoutBtn.setVisibility(View.GONE);

        exerciseBtn = (Button) findViewById(R.id.buttonAdd);
        exerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExerciseDialog(twitterID, workoutDateTime, workoutID);
            }
        });
        
    }

    public boolean addExerciseDialog(final String twitterID, final String dateTime, final String workout_id){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                .setTitle("Choose Exercise Type")
                .setCancelable(true)
                .setNegativeButton("Cardio", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(WorkoutActivity.this, CardioActivity.class);
                        intent.putExtra("twitterID", twitterID);
                        intent.putExtra("dateTime", dateTime);
                        intent.putExtra("workout_id", workout_id);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("Strength", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(WorkoutActivity.this, StrengthActivity.class);
                        intent.putExtra("twitterID", twitterID);
                        intent.putExtra("dateTime", dateTime);
                        intent.putExtra("workout_id", workout_id);
                        startActivity(intent);
                    }
                });
        dialog.create().show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workout, menu);
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
}
