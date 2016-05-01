package online.iiitd.edu.in.iiitd_online;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class AllCommunities extends AppCompatActivity {

    private String TAG = "DEBUG";
    private String URL;
    RecyclerView mRecyclerView;
    ProgressBar rotor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_communities);
        URL = getResources().getString(R.string.backendURL);
        mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
//communities
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        rotor = (ProgressBar) findViewById(R.id.progressBar);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getData();
        // specify an adapter (see also next example)

    }

    public void getData(){
        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL + "api/v1/communities", new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    rotor.setVisibility(View.INVISIBLE);
                    JSONArray temp = response.getJSONArray("data");
//                    JSONObject obj = (JSONObject) temp.get(0);

//                    for (int i = 0; i < temp.length(); i++) {
//                        JSONObject row = temp.getJSONObject(i);
////                        id = row.getInt("id");
//                    }

                    CardAdapter mAdapter = new CardAdapter(temp, getApplicationContext());
                    mRecyclerView.setAdapter(mAdapter);


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
