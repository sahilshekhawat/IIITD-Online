package online.iiitd.edu.in.iiitd_online;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class CreateCommunity extends AppCompatActivity {

    private static final String URL = "https://immense-tundra-31422.herokuapp.com/";
    private static final String TAG = "DEBUG";
    private Session session;//global variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community);

        session = new Session(getApplicationContext());
        Button submit = (Button) findViewById(R.id.btn);
        final EditText nameView = (EditText) findViewById(R.id.name);
        final EditText aboutView = (EditText) findViewById(R.id.about);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();


                JSONObject obj = new JSONObject();



                try {
                    obj.put("name", nameView.getText());
                    obj.put("about", aboutView.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ByteArrayEntity _entity = null;
                try {
                    _entity = new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                final AsyncHttpClient client = new AsyncHttpClient();

                client.post(getApplicationContext(), URL + "api/v1/communities?auth_token="+session.getSth("auth_token"), _entity, "application/json", new AsyncHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        JSONObject response = null;
                        try {
                            response = new JSONObject(new String(responseBody));
                            Log.v(TAG, response.toString(4));
                            Log.v(TAG, "info = " + response.getString("info"));
                            if (response.getString("info").equals("success")) {
                                Toast.makeText(CreateCommunity.this, "Community created.", Toast.LENGTH_LONG).show();

                                Intent i = new Intent(CreateCommunity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(CreateCommunity.this, "Failed", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }



                });
            }
        });
    }
}
