package online.iiitd.edu.in.iiitd_online;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by atul on 4/29/16.
 */
public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setSth(String key, String val) {
        prefs.edit().putString(key, val).commit();

    }

    public String getSth(String key) {
        String sth = prefs.getString(""+key,"");
        return sth;
    }
}