package online.iiitd.edu.in.iiitd_online;

import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class Community extends AppCompatActivity {


    TextView title = null;
    TextView about = null;
    TextView admin_name = null;
    TextView admin_email = null;
    private String TAG = "DEBUG";
    private String URL = "https://immense-tundra-31422.herokuapp.com/";
    FloatingActionButton fab;
    private Session session;//global variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        session = new Session(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.title);
        about= (TextView) findViewById(R.id.desc);
        admin_name = (TextView) findViewById(R.id.admin_name);
        admin_email = (TextView) findViewById(R.id.admin_email);

        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        p.setBehavior(null); //should disable default animations
        p.setAnchorId(View.NO_ID); //should let you set visibility
        fab.setLayoutParams(p);
        fab.setVisibility(View.GONE); // View.INVISIBLE might also be worth trying




        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Integer id = extras.getInt("id");
            getData(id.toString());
        }




    }
    public void getData(final String id){
            final AsyncHttpClient client = new AsyncHttpClient();
            client.get(URL + "api/v1/communities/"+id, new JsonHttpResponseHandler(){

                final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {


                        JSONArray temp = response.getJSONArray("data");
                        JSONObject obj = (JSONObject) temp.get(0);
                        Session session = new Session(getApplicationContext());
                        Log.v(TAG, ""+ obj.getInt("user_id") + ","+session.getSth("id"));

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

                        if(!(""+ obj.getInt("user_id")).equals(session.getSth("id"))){
                            //to bring things back to normal state
                            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                            p.setBehavior(new FloatingActionButton.Behavior());
                            p.setAnchorId(R.id.app_bar);
                            fab.setLayoutParams(p);


                            fab.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    linkFollowBtn(id);
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

    private void linkFollowBtn(String id) {
        final AsyncHttpClient client = new AsyncHttpClient();
        //community_id

        JSONObject obj = new JSONObject();



        try {
            obj.put("community_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ByteArrayEntity _entity = null;
        try {
            _entity = new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        client.get(getApplicationContext(), URL + "api/v1/communities/"+id+"/followers", new AsyncHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                JSONObject response = null;
                try {
                    response = new JSONObject(new String(responseBody));


                    JSONArray temp = response.getJSONArray("data");
                    JSONObject obj = (JSONObject) temp.get(0);


                    if(response.getString("info").equals("success")){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_turned_in_black_24dp, getApplicationContext().getTheme()));
                        } else {
                            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_turned_in_black_24dp));
                        }
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
                Log.d(TAG, "onFailure");
            }
        });
    }
}

