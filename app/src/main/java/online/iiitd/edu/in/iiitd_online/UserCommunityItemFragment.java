package online.iiitd.edu.in.iiitd_online;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import online.iiitd.edu.in.iiitd_online.usercommunity.UserCommunityContent;
import online.iiitd.edu.in.iiitd_online.usercommunity.UserCommunityContent.UserCommunityItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnUserCommunityFragmentInteractionListener}
 * interface.
 */
public class UserCommunityItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String URL;
    Session session = null;
    private ProgressBar progressBar;
    MyUserCommunityItemRecyclerViewAdapter myUserCommunityItemRecyclerViewAdapter;
    ArrayList<UserCommunityItem> userCommunityItems = new ArrayList<>();
    private String TAG = "UserCommunityItemFragment";
    View view;

    private OnUserCommunityFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserCommunityItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UserCommunityItemFragment newInstance(int columnCount) {
        UserCommunityItemFragment fragment = new UserCommunityItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        URL = getResources().getString(R.string.backendURL);
        session = new Session(getContext());
        session.printSharedPreferences();
//        userCommunityItems.add(new UserCommunityItem("1", "test community"));
        myUserCommunityItemRecyclerViewAdapter = new MyUserCommunityItemRecyclerViewAdapter(userCommunityItems, mListener);
        Log.d(TAG, Integer.toString(myUserCommunityItemRecyclerViewAdapter.getItemCount()));
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_usercommunityitem_list, container, false);
        setRetainInstance(true);

//        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        // Set the adapter
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.usercommunitylist);
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(myUserCommunityItemRecyclerViewAdapter);
        getData();

        return view;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState){
//        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
//        progressBar.setVisibility(View.INVISIBLE);
//    }

    public void getData(){
        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL + "api/v1/users/" + session.getSth("id") +"/following", new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

//                    progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
//                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d(TAG + "#####", response.toString());
                    JSONArray temp = response.getJSONArray("following_communities");
                    userCommunityItems.clear();
                    for (int i = 0; i < temp.length(); i++) {
                        JSONObject row = temp.getJSONObject(i);
                        UserCommunityItem userCommunityItem = new UserCommunityItem(row.getString("id"), row.getString("name"));
                        userCommunityItems.add(userCommunityItem);
                    }

                    for(UserCommunityItem userCommunityItem: userCommunityItems){
                        Log.d(TAG + "@@@@", userCommunityItem.toString());
                    }
                    myUserCommunityItemRecyclerViewAdapter.notifyDataSetChanged();
                    //recyclerView.setAdapter(myUserCommunityItemRecyclerViewAdapter);
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserCommunityFragmentInteractionListener) {
            mListener = (OnUserCommunityFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserCommunityFragmentInteractionListener");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        getData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnUserCommunityFragmentInteractionListener {
        // TODO: Update argument type and name
        void onUserCommunityFragmentInteraction(UserCommunityItem item);
    }
}
