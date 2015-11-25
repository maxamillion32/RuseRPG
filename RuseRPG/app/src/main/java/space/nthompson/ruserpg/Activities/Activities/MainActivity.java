package space.nthompson.ruserpg.Activities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import io.fabric.sdk.android.Fabric;

import space.nthompson.ruserpg.R;


public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "ckUjyKUjEaRagHnQqiBPqBRyy";
    private static final String TWITTER_SECRET = "E1C9VqXltOr6vD3kvM8ciH6OrKk5qGtVIiRKx0GhaVZGNBl5Mh";

    private TwitterLoginButton loginButton;

    Intent intent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);


        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()

                TwitterSession session = result.data;
                //Twitter API call to get user data (name, email, photo)
                //will launch Dashboard from there.
                twitterAPI();
                finish();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void twitterAPI(){
        Twitter.getApiClient().getAccountService().verifyCredentials(true, false, new Callback<User>() {

            @Override
            public void success(Result<User> userResult) {
                User user = userResult.data; //retrieve Twitter user
                String name = userResult.data.name; //retrieve Twitter user name
                String email = userResult.data.email; //retrieve Twitter user email

                //Retrieve profile photo URL's of various sizes for use
                // String photoUrlNormalSize = userResult.data.profileImageUrl;
                String photoUrlBiggerSize = userResult.data.profileImageUrl.replace("_normal", "_bigger");
                // String photoUrlMiniSize = userResult.data.profileImageUrl.replace("_normal", "_mini");
                String photoUrlOriginalSize = userResult.data.profileImageUrl.replace("_normal", "");
                System.out.println(photoUrlBiggerSize);

                intent.setClass(getApplicationContext(), Dashboard.class); //set class for intent
                intent.putExtra("key", photoUrlBiggerSize); //pass along retrieved photo url to Dashboard

                startActivity(intent); //launch activity
            }

            @Override
            public void failure(TwitterException e) {
            }
        });
    }

}
