package online.iiitd.edu.in.iiitd_online;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class SignUp extends AppCompatActivity {

    private static final String URL = "https://immense-tundra-31422.herokuapp.com/";
    private static final String TAG = "DEBUG";
    private Session session;//global variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final ProgressBar pg = (ProgressBar) findViewById(R.id.progressBar);

        pg.setVisibility(View.INVISIBLE);


        session = new Session(getApplicationContext()); //in oncreate



        Log.v(TAG, "in else");
        Button submit = (Button) findViewById(R.id.btn);
        final EditText emailView = (EditText) findViewById(R.id.email);
        final EditText nameView = (EditText) findViewById(R.id.name);
        final EditText cpwdView = (EditText) findViewById(R.id.cpwd);
        final EditText pwdView = (EditText) findViewById(R.id.pwd);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pg.setVisibility(View.VISIBLE);
                RequestParams params = new RequestParams();

                final String email, pwd, cpwd, name;

                Log.v(TAG, "here 1");
                email = emailView.getText().toString();
                name = nameView.getText().toString();
                cpwd = cpwdView.getText().toString();
                pwd = pwdView.getText().toString();
                JSONObject obj = new JSONObject();



                JSONObject jsonParams = new JSONObject();
                try {

                    Log.v(TAG, "here 2");

                    obj.put("email", email);
                    obj.put("name", name);
                    obj.put("password", pwd);
                    obj.put("password_confirmation", cpwd);


                    jsonParams.put("user", obj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    Log.v(TAG, "here 3");
                    jsonParams.put("user", obj);


                    final AsyncHttpClient client = new AsyncHttpClient();
                    Log.v(TAG, String.valueOf(params));

                    ByteArrayEntity _entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));


                    client.post(getApplicationContext(), URL + "api/v1/registrations", _entity, "application/json", new AsyncHttpResponseHandler(){

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Log.v(TAG, "here 5a");
                            JSONObject response = null;
                            try {
                                response = new JSONObject(new String(responseBody));
                                Log.v(TAG, response.toString(4));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


//                            Log.v(TAG, responseBody.toString());


                            try {
                                response = new JSONObject(new String(responseBody));


                                Log.v(TAG, response.toString());



                                if(response.getBoolean("success") == true){
                                    Toast.makeText(SignUp.this, "Signed Up", Toast.LENGTH_LONG).show();

                                    JSONObject obj = response.getJSONObject("data");

                                    String auth_token = obj.getString("auth_token");

                                    Log.v(TAG, "auth token = " + auth_token);

                                    //and now we set sharedpreference then use this like

                                    session.setSth("auth_token", auth_token);

                                    Intent i = new Intent(SignUp.this, LoginActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                                else{
                                    Toast.makeText(SignUp.this, "Failed", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            try {
                                JSONObject response = new JSONObject(new String(responseBody));
                                Log.v(TAG, response.toString(4));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(SignUp.this, "Failure", Toast.LENGTH_LONG).show();
                            Log.v(TAG, "here 5b");
                        }

                    });

                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \""  + "\"");
                }


            }
        });
    }

}
