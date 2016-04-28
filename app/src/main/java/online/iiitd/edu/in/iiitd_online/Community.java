package online.iiitd.edu.in.iiitd_online;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class Community extends AppCompatActivity {


    TextView title = null;
    TextView about = null;
    TextView admin_name = null;
    TextView admin_email = null;
    private String TAG = "DEBUG";
    private String URL = "https://immense-tundra-31422.herokuapp.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.title);
        about= (TextView) findViewById(R.id.desc);
        admin_name = (TextView) findViewById(R.id.admin_name);
        admin_email = (TextView) findViewById(R.id.admin_email);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        getData();


    }


        public void getData(){
            final AsyncHttpClient client = new AsyncHttpClient();
            client.get(URL + "api/v1/communities/1", new JsonHttpResponseHandler(){

                final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {


                        JSONArray temp = response.getJSONArray("data");
                        JSONObject obj = (JSONObject) temp.get(0);

                        collapsingToolbar.setTitle(obj.getString("name")); //setting collapse bar title
                        about.setText(obj.getString("about"));
//                        admin.setText(obj.getString("user_id"));

                        //setting admin details
                        if(obj.getString("user_id") != null){
                            client.get(URL + "api/v1/users/"+obj.getString("user_id"), new JsonHttpResponseHandler(){

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    try {

                                        JSONArray temp = response.getJSONArray("data");
                                        JSONObject obj = (JSONObject) temp.get(0);
                                        admin_name.setText(obj.getString("name")); //admin name
                                        admin_email.setText(obj.getString("email")); //admin email


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                                public void onFailure(Throwable error, String content) {
                                    error.printStackTrace();
                                    Log.d(TAG, "onFailure");
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                public void onFailure(Throwable error, String content) {
                    error.printStackTrace();
                    Log.d(TAG, "onFailure");
                }
            });
        }
}

