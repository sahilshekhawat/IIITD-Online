package online.iiitd.edu.in.iiitd_online;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import online.iiitd.edu.in.iiitd_online.usercommunity.UserCommunityContent;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, UserCommunityItemFragment.OnUserCommunityFragmentInteractionListener  {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private String TAG = "DEBUG";
    private String URL;
    Session session = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        URL = getResources().getString(R.string.backendURL);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        session = new Session(getApplicationContext());
        session.printSharedPreferences();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        toolbar.setTitle("Communities");
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        Drawable icon0 = this.getResources().getDrawable(R.drawable.ic_community);
        Drawable icon1 = this.getResources().getDrawable(R.drawable.ic_notif);
        Drawable icon2 = this.getResources().getDrawable(R.drawable.ic_profile);
        icon0.setBounds(0, 0, 100, 100);
        icon1.setBounds(0, 0, 100, 100);
        icon2.setBounds(0, 0, 100, 100);
        tabLayout.getTabAt(0).setIcon(icon0);
        tabLayout.getTabAt(1).setIcon(icon1);
        tabLayout.getTabAt(2).setIcon(icon2);

        toolbar.setTitleTextColor(Color.WHITE);

        //change status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorBlue));
        }

        /*
        get current user's all data as json*/

        getData();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AllCommunities.class);
                startActivity(i);
            }
        });

        //dynamically changing action bar title
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position == 0)
                {
                    toolbar.setTitle("Communities");
                    fab.show();
                }
                if(position == 1)
                {
                    toolbar.setTitle("All Notifications");
                    fab.hide();
                }
                if(position == 2)
                {
                    toolbar.setTitle("Profile");
                    fab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        Removing floating action bar.
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    //Implementing interaction listener for user community tab
    public void onUserCommunityFragmentInteraction(UserCommunityContent.UserCommunityItem item){
        Intent i = new Intent(MainActivity.this, Community.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("id", Integer.parseInt(item.id));
        startActivity(i);
    }


    public void getData(){
        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL + "api/v1/users/getcurrentuser?auth_token="+session.getSth("auth_token"), new JsonHttpResponseHandler(){


            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View header=navigationView.getHeaderView(0);

            final TextView nameView = (TextView) header.findViewById(R.id.name);
            final TextView emailView = (TextView) header.findViewById(R.id.email);


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {



                    JSONArray temp = response.getJSONArray("data");
                    JSONObject obj = (JSONObject) temp.get(0);

                    JSONArray myComm = obj.getJSONArray("communities");




                    ArrayList<String> listdata = new ArrayList<String>();

                    if (myComm!= null) {
                        for (int i=0;i<myComm.length();i++){
                            JSONObject j = (JSONObject) myComm.get(i);
                            listdata.add(""+j.getInt("id"));
                        }
                    }
                    Set<String> set = new HashSet<String>();
                    set.addAll(listdata);
                    session.setSet("i_am_admin_communities", set);
//                    session.printMyCommunities();
//
////
//                    for (int i = 0; i < myComm.length(); i++) {
//                        JSONObject row = myComm.getJSONObject(i);
//                        row.getInt("id");
////                        id = row.getInt("id");
//                    }

                    nameView.setText(obj.getString("name")); //setting collapse bar title
                    emailView.setText(obj.getString("email")); //setting collapse bar title

                    //set session variable sin sharedPref

                    session.setSth("id", obj.getString("id"));
                    session.setSth("name", obj.getString("name"));
                    session.setSth("email", obj.getString("email"));
                    session.setSth("about", obj.getString("about"));
                    session.setSth("avatar", obj.getString("avatar"));
                    session.setSth("sex", obj.getString("sex"));


//                   admin.setText(obj.getString("user_id"));


                    //get current user's communities
                    client.get(URL + "api/v1/users/" + session.getSth("id") + "/following", new JsonHttpResponseHandler(){



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
                                session.setSet("i_am_following_communities", set);


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

//    Removing menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position){
                case 0:
                    return UserCommunityItemFragment.newInstance(1);
                default:
                    return PlaceholderFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "";
//                case 1:
//                    return "";
//                case 2:
//                    return "";
//                case 3:
//                    return "";
//            }
            return null;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.new_com) {
            // Create new activity
            Intent i = new Intent(this, CreateCommunity.class);
            startActivity(i);

        }
        else if (id == R.id.logout){
            // Logout
            logout();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.delete(URL + "api/v1/sessions?auth_token="+session.getSth("auth_token"), new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {



                    if(response.getBoolean("success") == true){

                        session.deletePref();
                        Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Could not log you out. Sorry.", Toast.LENGTH_LONG).show();
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
