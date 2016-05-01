package online.iiitd.edu.in.iiitd_online;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

/**
 * Created by atul on 4/29/16.
 */
public class Session {

    private SharedPreferences prefs;

    private final static String adminComm = "i_am_admin_communities";
    private final static String follComm = "i_am_following_communities";
    private String TAG = "DEBUG";
    private String URL;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
        URL = cntx.getResources().getString(R.string.backendURL);

    }

    public void setSth(String key, String val) {
        prefs.edit().putString(key, val).commit();

    }

    public void updateMyCommunities(){
        getData();

    }

    public void setSet(String key, Set val) {
        prefs.edit().putStringSet(key, val).commit();

    }

    public void printSharedPreferences() {

        Log.v(TAG, "Printing Shared Preferences");

        Log.v(TAG, "name : " + getSth("name"));
        Log.v(TAG, "auth_token : " + getSth("auth_token"));
        Log.v(TAG, "sex : " + getSth("sex"));




        Log.v(TAG, "I am admin of following");
        Set<String> sth = prefs.getStringSet(adminComm, null);
        if(sth!=null){
            List<String> list = new ArrayList<String>(sth);

            for (int i = 0; i < list.size(); i++) {
                Log.d(TAG, "community id - " + list.get(i));
            }
        }

        Log.v(TAG, "I am follwing following");
        Set<String> sth2 = prefs.getStringSet(follComm, null);
        if(sth!=null){
            List<String> list = new ArrayList<String>(sth2);

            for (int i = 0; i < list.size(); i++) {
                Log.d(TAG, "community id - " + list.get(i));
            }
        }



    }

    //printing i_am_following_communities
    public void printMyCommunities(){

        Log.v(TAG, "Printing your communities");
        Set<String> sth = prefs.getStringSet(follComm, null);
        List<String> list = new ArrayList<String>(sth);

        for (int i=0; i<list.size(); i++) {
            Log.d(TAG, "community id - " + list.get(i));
        }
        return;
    }
    public List getSet(String key) {
        Set<String> sth = prefs.getStringSet(key, null);
        List<String> list=new ArrayList<String>(sth);

        return list;
    }

    public String getSth(String key) {
        String sth = prefs.getString(""+key,"");
        return sth;
    }
    public void deletePref(){
        prefs.edit().clear().commit();
        return;
    }
    public void getData(){
        Log.v(TAG, "In GetData of Sessions");

        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL + "api/v1/users/getcurrentuser?auth_token="+this.getSth("auth_token"), new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    Log.v(TAG, "Response of current user = " + response.toString());
                    JSONArray temp = response.getJSONArray("data");
                    JSONObject obj = (JSONObject) temp.get(0);

                    JSONArray myComm = obj.getJSONArray("communities");



//                    SharedPreferences.Editor editor = prefs.edit();
//                    editor.remove("my_communities");
//                    editor.commit();

                    ArrayList<String> listdata = new ArrayList<String>();

                    if (myComm!= null) {
                        for (int i=0;i<myComm.length();i++){
                            JSONObject j = (JSONObject) myComm.get(i);
                            listdata.add(""+j.getInt("id"));
                        }
                    }
                    Set<String> set = new HashSet<String>();
                    set.addAll(listdata);
                    setSet("i_am_admin_communities", set);
                    printMyCommunities();



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            public void onFailure(Throwable error, String content) {
                error.printStackTrace();
                Log.d(TAG, "onFailure");
            }
        });

        //get current user's communities
        client.get(URL + "api/v1/users/" + getSth("id") + "/following", new JsonHttpResponseHandler(){



            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {



                    Log.v(TAG, response.toString());
//                    JSONArray temp = response.getJSONArray("data");
//                    JSONObject obj = (JSONObject) response.getJSONObject("following_communities");
//                    Log.v(TAG, obj.toString());

                    JSONArray myComm = response.getJSONArray("following_communities");




                    ArrayList<String> listdata = new ArrayList<String>();

                    if (myComm!= null) {
                        for (int i=0;i<myComm.length();i++){
                            JSONObject j = (JSONObject) myComm.get(i);
                            listdata.add(""+j.getInt("id"));
                        }
                    }
                    Set<String> set = new HashSet<String>();
                    set.addAll(listdata);
                    setSet("i_am_following_communities", set);


//                   admin.setText(obj.getString("user_id"));



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