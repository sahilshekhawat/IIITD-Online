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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class LoginActivity extends AppCompatActivity {

    private static final String URL = "https://immense-tundra-31422.herokuapp.com/";
    private static final String TAG = "DEBUG";
    private Session session;//global variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button submit = (Button) findViewById(R.id.btn);
        final EditText emailView = (EditText) findViewById(R.id.email);
        final EditText pwdView = (EditText) findViewById(R.id.pwd);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();

                final String email, pwd;

                email = emailView.getText().toString();
                pwd = pwdView.getText().toString();
                JSONObject obj = new JSONObject();


                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("user", obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                StringEntity entity = null;
                try {
                    entity = new StringEntity(jsonParams.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                try {

                    obj.put("email", email);
                    obj.put("password", pwd);

                    params.put("user", obj);

                    final AsyncHttpClient client = new AsyncHttpClient();
                    Log.v(TAG, entity.toString());
                    Log.v(TAG, String.valueOf(params));

                    ByteArrayEntity _entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));


                    client.post(getApplicationContext(), URL + "api/v1/sessions", _entity, "application/json", new AsyncHttpResponseHandler(){

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                            Log.v(TAG, responseBody.toString());

                            JSONObject response = null;
                            try {
                                 response = new JSONObject(new String(responseBody));


                                Log.v(TAG, response.toString());



                                if(response.getBoolean("success") == true){
                                    Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_LONG).show();

                                    JSONObject obj = response.getJSONObject("data");

                                    String auth_token = obj.getString("auth_token");

                                    Log.v(TAG, "auth token = " + auth_token);
                                    session = new Session(getApplicationContext()); //in oncreate
                                    //and now we set sharedpreference then use this like

                                    session.setSth("auth_token", auth_token);

                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }

//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                            try {
//
//
//
//                                if(response.getBoolean("success") == true){
//                                    Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_LONG).show();
//
//                                    JSONArray temp = response.getJSONArray("data");
//                                    JSONObject obj = (JSONObject) temp.get(0);
//                                    String auth_token = obj.getString("auth_token");
//
//                                    Log.v(TAG, "auth token = " + auth_token);
//                                    session = new Session(getApplicationContext()); //in oncreate
//                                    //and now we set sharedpreference then use this like
//
//                                    session.setSth("auth_token", auth_token);
//
//                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                                    startActivity(i);
//                                    finish();
//                                }
//                                else{
//                                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
//                                }
//
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }


                    });

                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \""  + "\"");
                }


            }
        });
    }
}
